package rs.configVault.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConfigManagerClientTest {

	@Test
	public void getConfigAsString() {
		ConfigManagerClient configManagerClient = new ConfigManagerClient("http://localhost:8080/envs/dev");
		
		String configAsString = configManagerClient.getConfigAsString("dev", "hyper.properties");
		assertNotNull(configAsString);
	}

}
