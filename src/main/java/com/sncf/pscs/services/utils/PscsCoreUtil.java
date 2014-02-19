package com.sncf.pscs.services.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;








import com.sncf.pscs.adaptateur.ogive.service.DataObject;
import com.sncf.pscs.services.utils.Configurations;



/**
 * classe de definition des constantes et m���thodes utilitaires utilisees par la
 * couche applicative core
 * 
 * @author PAG
 * @version 1.0
 */
public final class PscsCoreUtil {

	/**
	 * L'instance statique singleton
	 */
	private static PscsCoreUtil instance;
	public static final String FINAL_AUDIO_FILE = "finalAudioFile";

	private static LogService logger = new DummyLogService();
	
	public enum EnumTypeEquipement {
		infodio,
		wca
	}

	/**
	 * Type equipement
	 */
	public static final String INFODIO = EnumTypeEquipement.infodio.toString();
	public static final String WCA = EnumTypeEquipement.wca.toString();	

	/**
	 * Constructeur
	 */
	private PscsCoreUtil(){

	}
	
	/**
	 * 
	 * @return PscsCoreUtil
	 */
	public static synchronized PscsCoreUtil getInstance() {
		if (instance == null) {
			instance = new PscsCoreUtil();
		}
		return instance;
	}
	
	public static final String HEADER_ERROR = "ErrorPSCS: ";
	
	public static final String MSG_ERREUR_OBIX = "Erreur OBIX : ";
	
	public static final String ERREUR_JMS_STACK = HEADER_ERROR + "probl���me avec la pile JMS.";
	
	public static final String ERREUR_AUDIO_ENCODING = HEADER_ERROR + "La couche Core n'arrive pas ��� encoder le fichier audio";

	public static final String ERREUR_IV_PARTIEL = HEADER_ERROR + "Certains ���quipements n'ont pas re���u l'information voyageur";

	public static final String ERREUR_IV_TOTAL = HEADER_ERROR + "Aucun ���quipement n'a re���u l'information voyageur";
	
	public static final String ERREUR_HTTP_DOWNLOAD = HEADER_ERROR + "La couche Core n���arrive pas ��� t���l���charger le fichier";
	
	public static final String ERREUR_HTTP_UPLOAD = HEADER_ERROR + "La couche Core n���arrive pas ��� uploader le fichier";
	
	public static final String ERREUR_FORMAT_OBIX = HEADER_ERROR + "Le format du message est invalide";
	
	public static final String ERREUR_HTTP_REQUETE = HEADER_ERROR + "La requ���te d'adresse du serveur Web ne contient aucun num���ro d'���quipement";

	public static final String ERREUR_HTTP_CONFIG = HEADER_ERROR + "La couche Core n���arrive pas ��� r���cup���rer la configuration du serveur Web";
	
	/**
	 * M���thode retournant les propri���t���s suppos���es d'un fichier audio une fois encod��� par le serveur (nouveau nom, extension du fichier)
	 * @param oldAudioFileName
	 * @return AudioFile
	 * */
	public static AudioFile simulateEncodedAudioFileProperties(String oldAudioFileName, String extension){		
		return new AudioFile(oldAudioFileName.substring(0, oldAudioFileName.lastIndexOf("."))+extension 
				, extension.substring((extension.lastIndexOf(".")+1))
				);
	}
	
	/**
	 * Encoder un fichier audio dans un autre format
	 * 
	 * @param fileName
	 * @return AudioFile
	 * @throws CoreException 
	 */
	public synchronized static AudioFile encodeAudioFile(String path, String fileName, DataObject configAudio) throws CoreException{
		// Encoder un fichier audio dans un autre format via lame
		try {		
			AudioFile fileResult = null;
			
			String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
			String oldExtension = fileName.substring( (fileName.lastIndexOf(".")+1) );
		
			// L'encodage audio n'est pas activ���
			if(!configAudio.getBoolean(Configurations.ENCODING_ACTIVATE)){
				
				//LOG.info("Encodage audio : d���sactiv���");
				logger.log(LogService.LOG_INFO, "Encodage audio : d���sactiv���");
				fileResult = new AudioFile(fileName, oldExtension);
			}
			// L'encodage audio est bien activ���
			else{
				//LOG.info("Encodage audio : activ���");
				logger.log(LogService.LOG_INFO, "Encodage audio : activ��");
				logger.log(LogService.LOG_INFO, "Encodage audio : activ���");
				String programExe = configAudio.getString(Configurations.PATH_PROGRAM_EXE);
				String finalEncodingAudioFileExtension = configAudio.getString(Configurations.FINAL_AUDIO_FILE_EXTENSION);
				String bitrate = configAudio.getString(Configurations.ENCODING_BITRATE);
				String vbrQuality = configAudio.getString(Configurations.ENCODING_VBR_QUALITY);

				
				// Le format du fichier audio ��� encoder correspond d���j��� ��� celui du fichier r���sultat
				// On n'encode pas
				if(oldExtension.equals(finalEncodingAudioFileExtension.substring( (finalEncodingAudioFileExtension.lastIndexOf(".")+1) ))){
					//LOG.info("Encodage audio : Le fichier audio ��� encoder '"+fileName+"' est d���j��� dans le bon format");
					logger.log(LogService.LOG_INFO, "Encodage audio : Le fichier audio ��� encoder '"+fileName+"' est d���j��� dans le bon format");
					fileResult = new AudioFile(fileName, oldExtension);
				}
				// Le format du fichier audio ��� encoder ne correspond pas ��� celui du fichier r���sultat
				// Lancement de la proc���dure d'encodage
				else{
					
					// Initialisation du nom du fichier r���sultant de l'encodage audio
					String newFilename = fileNameWithoutExtension+finalEncodingAudioFileExtension;
					
					// Initialisation de la commande d'encodage audio
					String parameters = "";
					if(!bitrate.equals("")){parameters = parameters.concat(" -b "+bitrate);}
					if(!vbrQuality.equals("")){parameters = parameters.concat(" -V "+vbrQuality);}
					
					String cmd = programExe+parameters
							+ " "+path+fileName
							+ " "+path+newFilename;
			
					//LOG.info("Ex���cution de la commande : "+cmd);
					logger.log(LogService.LOG_INFO, "Ex���cution de la commande : "+cmd);
				
					int exitValue = Mp3Encoder.launchMp3Exec(cmd);
					logger.log(LogService.LOG_INFO, "Fin de l'encodage du fichier audio, code retour de la commande :" + exitValue);
	
					if (exitValue == 0)	{
						// Retour en r���ponse du nom et de l'extension du fichier encod���e
						fileResult = new AudioFile(newFilename, 
							finalEncodingAudioFileExtension.substring(
								(finalEncodingAudioFileExtension.lastIndexOf(".")+1)
							)
						);
					}else{
						throw new CoreException("Erreur lors de l'ex���cution la commande d'encodage audio, code retour "+exitValue);
					}
				}
			}
			return fileResult;
		} catch (IOException e) {
			throw new CoreException("Erreur lors de l'encodage du fichier audio " + e);
		} catch (Exception e) {
			throw new CoreException("Probl���me lors de l'encodage du fichier audio final " + e);
		}
	}

	/**
	 * Enregistre un fichier
	 * @throws CoreException 
	 */
	public synchronized static void saveAudioFile(String fileName, byte[] filecontent) throws CoreException {
	
		try {
			//LOG.debug("Enregistrement temporaire en local du fichier audio : '"+fileName+"'");
			logger.log(LogService.LOG_DEBUG, "Enregistrement temporaire en local du fichier audio : '"+fileName+"'");
			// ���criture du contenu dans le fichier
			// BufferedWriter fileWriter;
			BufferedOutputStream filewriter;
			// fileWriter = new BufferedWriter(new
			// FileWriter(AUDIO_FILE_PATH+fileName, true));
			filewriter = new BufferedOutputStream(new FileOutputStream(fileName));
			filewriter.write(filecontent);
			filewriter.close();
		} catch (IOException e) {
			throw new CoreException("Erreur lors de la sauvegarde du fichier audio");
		}		
	}
	
}
