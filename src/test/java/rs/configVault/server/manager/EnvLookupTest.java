package rs.configVault.server.manager;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Test;

import rs.configVault.server.manager.EnvLookup;

public class EnvLookupTest {

	@Test
	public void lookup() {
		Properties envProperties = new Properties();
		envProperties.put("host", "127.0.0.1");
		
		EnvLookup envLookup = new EnvLookup(envProperties);
		
		assertEquals("127.0.0.1", envLookup.lookup("host"));
	}

}
