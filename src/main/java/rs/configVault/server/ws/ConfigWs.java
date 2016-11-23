package rs.configVault.server.ws;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import rs.configVault.server.manager.ConfigManager;

@RestController
@RequestMapping("envs")
public class ConfigWs {

	@Inject
	private ConfigManager configManager;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public String[] getEnvs() {
		return configManager.getEnvs();
	}

	@RequestMapping(path = "{env}/configs", method = RequestMethod.GET)
	public String[] getConfigs(@PathVariable("env") String env) {
		return configManager.getConfigs(env);
	}

	@RequestMapping(path = "{env}/configs", method = RequestMethod.POST)
	public void saveConfig(@PathVariable("env") String env, @RequestParam("file") MultipartFile file) throws IOException {
		configManager.saveConfig(env, file.getOriginalFilename(), file.getInputStream());
	}

	@RequestMapping(path = "{env}/configs/{configId:.+}", method = RequestMethod.GET)
	public String getConfig(@PathVariable("env") String env, @PathVariable("configId") String configId) {
		return configManager.getConfig(env, configId);
	}

	@RequestMapping(path = "{env}/configs/{configId:.+}", method = RequestMethod.POST)
	public void saveConfig(@PathVariable("env") String env, @PathVariable("configId") String configId, @RequestBody String fileContent) throws IOException {
		configManager.saveConfig(env, configId, fileContent);
	}

}
