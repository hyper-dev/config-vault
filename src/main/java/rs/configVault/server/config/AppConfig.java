package rs.configVault.server.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import rs.configVault.server.manager.ConfigManager;

@SpringBootApplication(scanBasePackages = "rs.configVault")
public class AppConfig {

	@Value("${vaultPath}")
	private String vaultPath;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AppConfig.class, args);
	}

	@Bean
	public ConfigManager configManager() {
		return new ConfigManager(new File(vaultPath));
	}

}
