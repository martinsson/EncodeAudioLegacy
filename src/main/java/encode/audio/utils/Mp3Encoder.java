package encode.audio.utils;

import java.io.IOException;

public class Mp3Encoder {

	public static int launchMp3Exec(String cmd) throws IOException,
			InterruptedException {
		Process p1 = Runtime.getRuntime().exec(cmd);
		p1.getOutputStream().close();
		p1.getInputStream().close();
		p1.getErrorStream().close();
		p1.waitFor();
	
		// Ex�cution de la commande d'encodage audio
		//LOG.info("Fin de l'encodage du fichier audio, code retour de la commande :" + p.exitValue());
		int exitValue = p1.exitValue();
		return exitValue;
	}

}
