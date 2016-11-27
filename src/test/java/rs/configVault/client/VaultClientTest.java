package rs.configVault.client;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

public class VaultClientTest {

	@Test
	public void getConfigAsString() {
		VaultClient configManagerClient = new VaultClient("http://localhost:8080/envs/dev");
		
		String configAsString = configManagerClient.getConfigAsString("dev", "hyper.properties");
		assertNotNull(configAsString);
	}

	@Test
	public void getConfigAsInputstream() throws IOException {
		VaultClient configManagerClient = new VaultClient("http://localhost:8080/envs/dev");
		
		InputStream configAsString = configManagerClient.getConfigAsInputstream("dev", "hyper.properties");
		assertNotNull(configAsString);
		
		Properties properties = new Properties();
		properties.load(configAsString);
		
		assertEquals("127.0.0.1", properties.get("myKey"));
	}

}
