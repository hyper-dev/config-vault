package rs.configVault.server.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.text.StrMatcher;
import org.apache.commons.lang3.text.StrSubstitutor;

@Named
public class VaultManager {

	private File vaultPath;
	private VaultHistory vaultHistory;

	public VaultManager(File vaultPath) {
		this.vaultPath = vaultPath;
		this.vaultHistory = new VaultHistory();
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
			
			vaultHistory.createRevision(envVaultPath, file);
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
			
			vaultHistory.createRevision(envVaultPath, file);
		}
		catch (IOException e) {
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
