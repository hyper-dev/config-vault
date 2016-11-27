package rs.configVault.server.manager;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class VaultHistory {
	
	public void createRevision(File envVaultPath, File file) throws IOException {
		try (Git git = Git.open(envVaultPath)) {
			git.add().addFilepattern(file.getName()).call();
			git.commit().setMessage("New version of " + file.getName()).call();
		}
		catch (GitAPIException e) {
			throw new RuntimeException(e);
		}
	}

}
