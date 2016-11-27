package rs.configVault.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VaultClient {
	
	private static final Logger LOG = LoggerFactory.getLogger(VaultClient.class);

	private String configVaultUrl;

	public VaultClient(String configVaultUrl) {
		this.configVaultUrl = configVaultUrl;
	}

	public InputStream getConfigAsInputstream(String env, String configId) {
		try {
			URL url = new URL(configVaultUrl + "/configs/" + configId);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			// conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			InputStream inputStream = copyInputStream(conn.getInputStream());
			
			conn.disconnect();

			return inputStream;
		} 
		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} 
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getConfigAsString(String env, String configId) {
		try {
			InputStream inputStream = getConfigAsInputstream(env, configId);

			String configAsString = IOUtils.toString(inputStream, Charset.forName("UTF-8"));

			inputStream.close();

			return configAsString;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private ByteArrayInputStream copyInputStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// Fake code simulating the copy
		// You can generally do better with nio if you need...
		// And please, unlike me, do something about the Exceptions :D
		byte[] buffer = new byte[1024];
		int len;
		while ((len = inputStream.read(buffer)) > -1 ) {
		    baos.write(buffer, 0, len);
		}
		baos.flush();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(baos.toByteArray());
		return byteArrayInputStream;
	}

}
