package encode.audio.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Security;


/**
 * @author SEB Classe factory permettant de lancer des actions/verbes HTTP(s)
 */
public class HttpRequestFactory {
	
	// =======================================
	// Variables static

	//private static final Logger LOG = Logger.getLogger(HttpRequestFactory.class);
	private static final String LINE_END = "\r\n";
	private static final String TWO_HYPHENS = "--";
	private static final String BOUNDARY = "*****";

	
	// =======================================
	// Methodes

	/**
	 * Post un fichier pour le d�poser sur un serveur HTTPS
	 * 
	 * @param sUrl
	 * @return byte[]
	 * @throws CoreException 
	 */
	public static byte[] getFileFromHttpServer(String sUrl) throws CoreException{
		
		//url = http://10.20.2.178/audio/
		//fileName = ccue.mp3
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
					
							
			byte[]buff = new byte[1];
			int l = in.read(buff);
			//StringBuffer sb=new StringBuffer();
			
			while(l>0)
			{		
			   // pas d'encodage / decodage ; rapatriement du fichier binaire audio 
			   bos.write(buff);
			   l = in.read(buff);		   
			}
//			String s=sb.toString();	
			return bos.toByteArray();
		} catch (MalformedURLException e) {
			throw new CoreException("Erreur lors de la r�cup�ration du fichier a l'adresse : " + sUrl,e);
		} catch (IOException e) {
			throw new CoreException("Erreur lors de la r�cup�ration du fichier a l'adresse : " + sUrl,e);
		}
	}
}
