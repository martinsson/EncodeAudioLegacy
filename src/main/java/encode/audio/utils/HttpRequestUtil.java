package encode.audio.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequestUtil {

	/**
	 * Get a file from an http server.
	 *
	 * @param sUrl
	 * @return byte[]
	 * @throws CoreException
	 */
	public static byte[] getFileFromHttpServer(String sUrl) throws CoreException {

		try {
			URL url;
			url = new URL(sUrl);
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setAllowUserInteraction(false);
			// Allow Inputs
			connection.setDoInput(true);
			// Allow Outputs
			connection.setDoOutput(true);
			// Don't use a cached copy.
			connection.setUseCaches(false);

			// Use a GET method.
			connection.setRequestProperty("Request-Method", "GET");
			connection.setRequestProperty("Connection", "Keep-Alive");

			// ------------------ read the SERVER RESPONSE
			InputStream in = connection.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			byte[] buff = new byte[1];
			int l = in.read(buff);

			while (l > 0) {
				bos.write(buff);
				l = in.read(buff);
			}
			return bos.toByteArray();
		} catch (MalformedURLException e) {
			throw new CoreException("Error when downloading file at: " + sUrl, e);
		} catch (IOException e) {
			throw new CoreException("Error when downloading file at: " + sUrl, e);
		}
	}
}
