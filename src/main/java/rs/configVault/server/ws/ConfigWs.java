package rs.configVault.server.ws;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import rs.configVault.server.manager.ConfigManager;

@RestController
@RequestMapping("envs")
public class ConfigWs {

	@Inject
	private ConfigManager configManager;

	@GetMapping("")
	public String[] getEnvs() {
		return configManager.getEnvs();
	}

	@GetMapping("{env}/configs")
	public String[] getConfigs(@PathVariable String env) {
		return configManager.getConfigs(env);
	}

	@PostMapping("{env}/configs")
	public void saveConfig(@PathVariable String env, @RequestParam MultipartFile file) throws IOException {
		configManager.saveConfig(env, file.getOriginalFilename(), file.getInputStream());
	}

	@GetMapping("{env}/configs/{configId:.+}")
	public String getConfig(@PathVariable String env, @PathVariable String configId) {
		return configManager.getConfig(env, configId);
	}

	@PostMapping("{env}/configs/{configId:.+}")
	public void saveConfig(@PathVariable String env, @PathVariable String configId, @RequestBody String fileContent) throws IOException {
		configManager.saveConfig(env, configId, fileContent);
	}

}
