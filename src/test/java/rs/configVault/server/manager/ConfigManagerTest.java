package rs.configVault.server.manager;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class ConfigManagerTest {

	@Test
	public void getConfigProperties() {
		String configProperties = new ConfigManager(new File("src/test/vault")).getConfig("dev", "hyper.properties");
		
		assertEquals("c=127.0.0.1", configProperties);
	}

}
