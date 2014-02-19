package com.sncf.pscs.adaptateur.ogive.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import pscs.tmlg.flux.FluxTmlg;
import pscs.tmlg.iflux.IAnnonceAudioTmlg;
import pscs.tmlg.iflux.IFluxTmlg;
import pscs.tmlg.utils.ObixTmlgExeption;

import com.sncf.pscs.services.utils.AudioFile;
import com.sncf.pscs.services.utils.CoreException;
import com.sncf.pscs.services.utils.DummyLogService;
import com.sncf.pscs.services.utils.HttpRequestFactory;
import com.sncf.pscs.services.utils.LogService;
import com.sncf.pscs.services.utils.PscsCoreUtil;

public class GererAnnonceAudio{
	
	private Boolean download = false;
	private Boolean encode = false;
	
	private LogService logger = new DummyLogService(); 
	private DataObject configAudio;
	private DataObject configHttp;
	
	/** The key to get the corresponding in DB. */
	private final static String KEY_CONF_URL_AUDIO_FOLDER = "AUDIO.ANNOUNCE.FOLDER.URL";
	
	public String demanderFlux(String message, DataObject configAudioTmp, DataObject configHttpTmp) throws PscsTechnicalException{
		try{
			this.configAudio = configAudioTmp;
			this.configHttp = configHttpTmp;
			IFluxTmlg flux;
			flux = new FluxTmlg(message);
			
			AudioFile newAudioFile = null;
			IAnnonceAudioTmlg annonceAudio = flux.getBody().getInfoVoyageur().getAnnonceAudio();

			// Traitement du message d'annonce audio
			newAudioFile = traiterAnnonceAudio(flux, annonceAudio);

			// Avertissement du bord d'une annonce audio
			flux = envoiAnnonceAudio(flux, newAudioFile);
			return flux.toString();
			
		}catch(CoreException ex){
 
			logger.log(LogService.LOG_ERROR,  ex.getMessage());
			if(!download){
 
				logger.log(LogService.LOG_ERROR,  ex.getMessage());
				throw new PscsTechnicalException(PscsCoreUtil.ERREUR_HTTP_DOWNLOAD, ex);
			}else if(!encode){	
 
				logger.log(LogService.LOG_ERROR,  ex.getMessage());
				throw new PscsTechnicalException(PscsCoreUtil.ERREUR_AUDIO_ENCODING, ex);
			}else{
 
				logger.log(LogService.LOG_ERROR,  ex.getMessage());
				throw new PscsTechnicalException(PscsCoreUtil.ERREUR_HTTP_UPLOAD, ex);
			}
		}catch (ObixTmlgExeption e) {
 
			logger.log(LogService.LOG_ERROR,  e.getMessage());
			throw new PscsTechnicalException(PscsCoreUtil.ERREUR_FORMAT_OBIX);
		} catch (Exception e) {
			logger.log(LogService.LOG_ERROR,  e.getMessage());
			throw new PscsTechnicalException(PscsCoreUtil.ERREUR_HTTP_CONFIG, e);
		}
	}
	
	
	/**
	 * Traitement optimise d'une annonce audio en verifiant si les fichiers audio ne sont pas deje accessible sur le serveur HTTPS
	 * Attention, les fichiers une fois telecharges ne sont pas supprimes en local
	 * @param flux
	 * @param annonceAudio
	 * @return AudioFile
	 * @throws CoreException 
	 * */
	public AudioFile traiterAnnonceAudio(IFluxTmlg flux, IAnnonceAudioTmlg annonceAudio) throws CoreException, Exception{
		
		String fileName = annonceAudio.getFileName();
		String fileUrl = annonceAudio.getUrl();
		String audioTemporalyPath = configHttp.getString("audio_temp_path");
		String filePath = audioTemporalyPath+fileName;
		
		AudioFile newAudioFile = PscsCoreUtil.simulateEncodedAudioFileProperties(fileName, configAudio.getString("final_audio_file_extension"));
		String encodedFilename = newAudioFile.getName();

		//Get the folder where to check or post the audio file
		String localServerFolder = getLocalServerFolder();
		
		if(verifAudioFileExistsOnServer(localServerFolder, encodedFilename)){
			logger.log(LogService.LOG_DEBUG, "Le fichier audio '"+encodedFilename+"' existe deje sur le serveur HTTPS");
		}
		else{
			logger.log(LogService.LOG_DEBUG, "Le fichier audio '"+encodedFilename+"' n'existe pas encore sur le serveur HTTPS");
			File encodedAudioFile = new File(audioTemporalyPath + encodedFilename);
			if(encodedAudioFile.exists()){
				logger.log(LogService.LOG_DEBUG, "Le fichier audio encode existe deje en local");
				download = true;
				encode = true;
			}
			else{
				downnloadAudioFile(fileName, filePath, fileUrl);
				download = true;
				
				// Encodage du fichier audio en vue de le pousser sur le serveur HTTPS
				logger.log(LogService.LOG_DEBUG, "Encodage du fichier audio :"+filePath+" (path : "+ configHttp.getString("audio_temp_path") +")");
				newAudioFile = PscsCoreUtil.encodeAudioFile(audioTemporalyPath, fileName, configAudio);
				encode = true;
			}
			
			// Recuperation des donnees du fichier audio encode
			newAudioFile.setBinary(recupDonneesAudio(newAudioFile.getName()));	
			
			// Upload du fichier audio encode sur le servuer HTTPS
			uploadAnnonceAudio(localServerFolder, newAudioFile);
		}
		
		return newAudioFile;
	}
	
	protected String getLocalServerFolder() {
		return "/pscs/";
	}


	/**
	 * Telecharge le fichier s'il n'est pas deje present dans le repertoire temporaire 
	 * @param fileName 
	 * @param fileUrl 
	 * @param filePath 
	 * 
	 * */
	public void downnloadAudioFile(String fileName, String destinationfilePath, String fileUrl) throws CoreException{
		File audioFile = new File(configHttp.getString("audio_temp_path") + fileName);
		// Le fichier audio originel existe en local sur la machine
		if(audioFile.exists()){
			logger.log(LogService.LOG_DEBUG, "Le fichier audio originel existe deje en local : '"+configHttp.getString("audio_temp_path") +fileName+"'");
		}
		// Le fichier audio originel n'existe pas en local sur la machine
		else{
			// Recuperation du fichier audio et enregistrement temporaire pour encodage
			byte[] fileData = downloadAudioFileFromHttpServer(fileUrl);
			PscsCoreUtil.saveAudioFile(destinationfilePath, fileData);
		}
	}


	public byte[] downloadAudioFileFromHttpServer(String fileUrl) throws CoreException {
		return HttpRequestFactory.getFileFromHttpServer(fileUrl);
	}
	
	public byte[] recupDonneesAudio(String filename) throws CoreException{
		try{
			File newFile = new File(configHttp.getString("audio_temp_path") + filename);
			FileInputStream fileInputStream = new FileInputStream(newFile);
			byte[] binaryFile = new byte[(int) newFile.length()];
			fileInputStream.read(binaryFile);
			logger.log(LogService.LOG_DEBUG, "Recuperation des donnees du fichier audio e envoyer: OK");
			fileInputStream.close();
			
			return binaryFile;
		} catch (IOException e) {
			throw new CoreException("Erreur lors de la recuperation des donnees du fichier audio e envoyer : " + configHttp.getString("audio_temp_path") + filename, e);
		}
	}
	
	public void uploadAnnonceAudio(String localServerFolder, AudioFile newAudioFile) throws IOException{
		logger.log(LogService.LOG_DEBUG, "Envoi du fichier audio sur le serveur HTTPS");
		FileUtils.postFile(localServerFolder, newAudioFile.getName(), newAudioFile.getBinary());
		logger.log(LogService.LOG_DEBUG, "Envoi du fichier audio sur le serveur HTTPS : OK");
	}	
	
	public IFluxTmlg envoiAnnonceAudio(IFluxTmlg flux, AudioFile newAudioFile) throws CoreException{
		String serverAudioEmbarqueURLGet =  configHttp.getString("url_embarque_server_get");
		
		// MAJ de l'annonce audio avec les infos du fichier audio sur le serveur https
		flux.getBody().getInfoVoyageur().getAnnonceAudio().setFileName(newAudioFile.getName());
		flux.getBody().getInfoVoyageur().getAnnonceAudio().setFormat(newAudioFile.getFormat());
		flux.getBody().getInfoVoyageur().getAnnonceAudio().setUrl(serverAudioEmbarqueURLGet + newAudioFile.getName());
		
		logger.log(LogService.LOG_DEBUG, "Envoi du message oBix e l'adaptateur: " + flux.toString());
		return flux;
	}
	
	/**
	 * Check if a audio file exists on the server.
	 * @param localServerFolder the url where to look for the file
	 * @param fileName the name of the file
	 * @return true if the file exists, else false
	 */
	public boolean verifAudioFileExistsOnServer(String localServerFolder, String fileName) {
		boolean isFileExist = false;
		if (localServerFolder != null){
			//Add a / at the end of the path if not present
			localServerFolder = FileUtils.checkPath(localServerFolder);
			isFileExist = FileUtils.isFileExist(localServerFolder + fileName);; 
		}
		
		return isFileExist; 
	}
	
}
