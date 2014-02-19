package com.sncf.pscs.services.utils;

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
	 * @param sslTrustStore
	 * @param sslTrustStorePassword
	 * @param sUrl
	 * @param decoded
	 * @param fileName
	 * @throws CoreException 
	 */
	public static void postFileToHttpsServer(String sslTrustStore, String sslTrustStorePassword, String sUrl, byte[] decoded, String fileName) throws CoreException  {
		try{
			DataOutputStream dos = null;
			
			Security.setProperty("ssl.SocketFactory.provider", "com.ibm.jsse2.SSLSocketFactoryImpl");
			Security.setProperty("ssl.ServerSocketFactory.provider", "com.ibm.jsse2.SSLServerSocketFactoryImpl");
			
			System.setProperty("javax.net.ssl.trustStore", sslTrustStore);
			System.setProperty("javax.net.ssl.trustStorePassword", sslTrustStorePassword);

//			javax.net.ssl.HttpsURLConnection connection =(javax.net.ssl.HttpsURLConnection) new URL(sUrl).openConnection();
//			connection.setHostnameVerifier(new HostnameVerifier(){
//					public boolean verify(String urlHostName, SSLSession session) {
//						return true;
//					}
//				});
			URL url = new URL(sUrl);
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setAllowUserInteraction(false);
			// Allow Inputs
			connection.setDoInput(true);
			// Allow Outputs
			connection.setDoOutput(true);
			// Don't use a cached copy.
			connection.setUseCaches(false);
			// Use a post method.
			connection.setRequestProperty("Request-Method", "POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
			dos = new DataOutputStream(connection.getOutputStream());

			dos.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_END);
			dos.writeBytes("Content-Disposition: form-data; name=\"FILE1\";" + " filename=\"" + fileName + "\"" + LINE_END);
			dos.writeBytes(LINE_END);
			dos.write(decoded);
			// send multipart form data necesssary after file data...
			dos.writeBytes(LINE_END);
			dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END);

			// close streams
			dos.flush();
			dos.close();
			// ------------------ read the SERVER RESPONSE
			readServerResponse(connection);
		}catch (Exception ex){
			throw new CoreException("Probleme avec le serveur HTTP: " + ex.getMessage());
		}
			
	}

	/**
	 * Read the server response 
	 * @param conn
	 * @throws CoreException 
	 */
	public static void readServerResponse(URLConnection conn) throws CoreException{
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String str;
//			//TODO traiter les messages d'erreur HTTP
			while ((str = bufferedReader.readLine()) != null) {
//				//if (LOG.isDebugEnabled()){
//				//	LOG.debug(str + "");
//				//}
			}
			
			bufferedReader.close();

		} catch (IOException ioex) {
			throw new CoreException("Probleme avec la reponse du serveur HTTP: " + ioex.getMessage());
		} 
	}
	
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
