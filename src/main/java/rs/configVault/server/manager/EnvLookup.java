package rs.configVault.server.manager;

import java.util.Properties;

import org.apache.commons.lang3.text.StrLookup;

public class EnvLookup extends StrLookup<String> {

	private Properties envProperties;

	public EnvLookup(Properties envProperties) {
		this.envProperties = envProperties;
	}

	@Override
	public String lookup(String key) {
		return envProperties.getProperty(key);
	}

}
