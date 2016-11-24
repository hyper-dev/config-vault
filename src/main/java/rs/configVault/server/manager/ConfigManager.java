package rs.configVault.server.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.text.StrMatcher;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * 
 * @author Richard Pula
 *
 */
public class ConfigManager {

	private File vaultPath;

	public ConfigManager(File vaultPath) {
		this.vaultPath = vaultPath;
	}

	public String[] getEnvs() {
		return vaultPath.list();
	}

	public String[] getConfigs(String env) {
		return new File(vaultPath, env).list();
	}

	public String getConfig(String env, String configId) {
		File configFile = new File(vaultPath, env + "/" + configId);

		try {
			Properties envProperties = getEnvProperties(env);
			EnvLookup variableResolver = new EnvLookup(envProperties);
			StrSubstitutor strSubstitutor = new StrSubstitutor(variableResolver, StrMatcher.stringMatcher("${env:"), StrSubstitutor.DEFAULT_SUFFIX, StrSubstitutor.DEFAULT_ESCAPE);

			String configContent = FileUtils.readFileToString(configFile, Charset.forName("UTF-8"));

			return strSubstitutor.replace(configContent);
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException("Unable to find the config file into: " + configFile.getAbsolutePath(), e);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void saveConfig(String env, String configId, InputStream inputStream) {
		try {
			File envVaultPath = new File(vaultPath, env);
			File file = new File(envVaultPath, configId);
			
			FileUtils.copyInputStreamToFile(inputStream, file);
			
			createRevision(envVaultPath, file);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void saveConfig(String env, String configId, String fileContent) {
		try {
			File envVaultPath = new File(vaultPath, env);
			File file = new File(envVaultPath, configId);
			
			FileUtils.write(file, fileContent, Charset.forName("UTF-8"));
			
			createRevision(envVaultPath, file);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void createRevision(File envVaultPath, File file) throws IOException {
		try (Git git = Git.open(envVaultPath)) {
			git.add().addFilepattern(file.getName()).call();
			git.commit().setMessage("New version of " + file.getName()).call();
		}
		catch (NoFilepatternException e) {
			throw new RuntimeException(e);
		}
		catch (GitAPIException e) {
			throw new RuntimeException(e);
		}
	}

	public Properties getEnvProperties(String env) throws IOException {
		File file = new File(vaultPath, env + "/vault.env.properties");

		FileInputStream fileInputStream = new FileInputStream(file);
		Properties envProperties = new Properties();
		envProperties.load(fileInputStream);

		fileInputStream.close();

		return envProperties;
	}

}
