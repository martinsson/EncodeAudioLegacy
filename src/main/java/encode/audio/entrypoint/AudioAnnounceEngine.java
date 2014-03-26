package encode.audio.entrypoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import encode.audio.utils.AudioFile;
import encode.audio.utils.CoreException;
import encode.audio.utils.DummyLogService;
import encode.audio.utils.HttpRequestUtil;
import encode.audio.utils.LogService;
import encode.audio.utils.CoreUtil;
import flux.FluxTmlg;
import flux.IAudioAnnounceTmlg;
import flux.IFluxTmlg;
import flux.ObixTmlgExeption;

public class AudioAnnounceEngine {

	private Boolean download = false;
	private Boolean encode = false;

	private LogService logger = new DummyLogService();
	private DataObject audioConfig;
	private DataObject httpConfig;

	/** The key to get the corresponding in DB. */
	private final static String KEY_CONF_URL_AUDIO_FOLDER = "AUDIO.ANNOUNCE.FOLDER.URL";

	public String publishAudioFile(String audioFileMessage, DataObject audioConfigTmp, DataObject httpConfigTmp) throws AppTechnicalException {
		try {
			this.audioConfig = audioConfigTmp;
			this.httpConfig = httpConfigTmp;
			IFluxTmlg targetAudioFileMessage;
			targetAudioFileMessage = new FluxTmlg(audioFileMessage);

			AudioFile newAudioFile = null;
			IAudioAnnounceTmlg audioAnnounce = targetAudioFileMessage.getBody().getTravelInfo().getAudioAnnounce();

			newAudioFile = processAudioAnnounce(targetAudioFileMessage, audioAnnounce);

			targetAudioFileMessage = updateAudioFileMessage(targetAudioFileMessage, newAudioFile);
			return targetAudioFileMessage.toString();

		} catch (CoreException ex) {

			logger.log(LogService.LOG_ERROR, ex.getMessage());
			if (!download) {

				logger.log(LogService.LOG_ERROR, ex.getMessage());
				throw new AppTechnicalException(CoreUtil.HTTP_DOWNLOAD_ERROR, ex);
			} else if (!encode) {

				logger.log(LogService.LOG_ERROR, ex.getMessage());
				throw new AppTechnicalException(CoreUtil.AUDIO_ENCODING_ERROR, ex);
			} else {

				logger.log(LogService.LOG_ERROR, ex.getMessage());
				throw new AppTechnicalException(CoreUtil.HTTP_UPLOAD_ERROR, ex);
			}
		} catch (ObixTmlgExeption e) {

			logger.log(LogService.LOG_ERROR, e.getMessage());
			throw new AppTechnicalException(CoreUtil.OBIX_FORMAT_ERROR);
		} catch (Exception e) {
			logger.log(LogService.LOG_ERROR, e.getMessage());
			throw new AppTechnicalException(CoreUtil.HTTP_CONFIG_ERROR, e);
		}
	}

	/**
	 * process audio announce
	 *
	 * @param flux
	 * @param audioAnnounce
	 * @return AudioFile
	 * @throws CoreException
	 * */
	public AudioFile processAudioAnnounce(IFluxTmlg flux, IAudioAnnounceTmlg audioAnnounce) throws CoreException, Exception {

		String fileName = audioAnnounce.getFileName();
		String fileUrl = audioAnnounce.getUrl();
		String audioTempPath = httpConfig.getString("audio_temp_path");
		String filePath = audioTempPath + fileName;

		AudioFile newAudioFile = CoreUtil.simulateEncodedAudioFileProperties(fileName, audioConfig.getString("final_audio_file_extension"));
		String encodedFilename = newAudioFile.getName();

		String localServerFolder = getLocalServerFolder();

		if (verifAudioFileExistsOnServer(localServerFolder, encodedFilename)) {
			logger.log(LogService.LOG_DEBUG, "The audio file '" + encodedFilename + "' already exists on the HTTPS server");
		} else {
			logger.log(LogService.LOG_DEBUG, "The audio file '" + encodedFilename + "' does not exist on the HTTPS server");
			File encodedAudioFile = new File(audioTempPath + encodedFilename);
			if (encodedAudioFile.exists()) {
				logger.log(LogService.LOG_DEBUG, "The encoded audio file already exists locally");
				download = true;
				encode = true;
			} else {
				downnloadAudioFile(fileName, filePath, fileUrl);
				download = true;

				logger.log(LogService.LOG_DEBUG, "Encoding audio file :" + filePath + " (path : " + httpConfig.getString("audio_temp_path") + ")");
				newAudioFile = CoreUtil.encodeAudioFile(audioTempPath, fileName, audioConfig);
				encode = true;
			}

			newAudioFile.setBinary(getAudioFileContent(newAudioFile.getName()));

			// Uploading encoded audio file to HTTPS server
			uploadAudioAnnounce(localServerFolder, newAudioFile);
		}

		return newAudioFile;
	}

	protected String getLocalServerFolder() {
		return "./src/test/resources/pscs";
	}

	/**
	 * Download the audio file if not already exists in the temp directory
	 *
	 * @param fileName
	 * @param fileUrl
	 * @param filePath
	 *
	 * */
	public void downnloadAudioFile(String fileName, String destinationfilePath, String fileUrl) throws CoreException {
		File audioFile = new File(httpConfig.getString("audio_temp_path") + fileName);
		if (audioFile.exists()) {
			logger.log(LogService.LOG_DEBUG, "the original audio file already exists in local: '" + httpConfig.getString("audio_temp_path") + fileName + "'");
		}
		else {
			logger.log(LogService.LOG_DEBUG, "simulate downloading audio file: '" + httpConfig.getString("audio_temp_path") + fileName + "' to locally path: " + destinationfilePath);
		}
	}

	public byte[] downloadAudioFileFromHttpServer(String fileUrl) throws CoreException {
		return HttpRequestUtil.getFileFromHttpServer(fileUrl);
	}

	public byte[] getAudioFileContent(String filename) throws CoreException {
		try {
			File newFile = new File(httpConfig.getString("audio_temp_path") + filename);
			FileInputStream fileInputStream = new FileInputStream(newFile);
			byte[] binaryFile = new byte[(int) newFile.length()];
			fileInputStream.read(binaryFile);
			logger.log(LogService.LOG_DEBUG, "Getting audio file date to send: OK");
			fileInputStream.close();

			return binaryFile;
		} catch (IOException e) {
			throw new CoreException("Error when getting audio file date to send: " + httpConfig.getString("audio_temp_path") + filename, e);
		}
	}

	public void uploadAudioAnnounce(String localServerFolder, AudioFile newAudioFile) throws IOException {
		logger.log(LogService.LOG_DEBUG, "Uploading audio file to HTTPS server");
		FileUtils.postFile(localServerFolder, newAudioFile.getName(), newAudioFile.getBinary());
		logger.log(LogService.LOG_DEBUG, "Uploading audio file to HTTPS server : OK");
	}

	public IFluxTmlg updateAudioFileMessage(IFluxTmlg flux, AudioFile newAudioFile) throws CoreException {
		String serverAudioEmbarqueURLGet = httpConfig.getString("url_embarque_server_get");

		flux.getBody().getTravelInfo().getAudioAnnounce().setFileName(newAudioFile.getName());
		flux.getBody().getTravelInfo().getAudioAnnounce().setFormat(newAudioFile.getFormat());
		flux.getBody().getTravelInfo().getAudioAnnounce().setUrl(serverAudioEmbarqueURLGet + newAudioFile.getName());

		logger.log(LogService.LOG_DEBUG, "Send message oBix to the adaptor: " + flux.toString());
		return flux;
	}

	/**
	 * Check if a audio file exists on the server.
	 *
	 * @param localServerFolder
	 *            the url where to look for the file
	 * @param fileName
	 *            the name of the file
	 * @return true if the file exists, else false
	 */
	public boolean verifAudioFileExistsOnServer(String localServerFolder, String fileName) {
		boolean isFileExist = false;
		if (localServerFolder != null) {
			// Add a / at the end of the path if not present
			localServerFolder = FileUtils.checkPath(localServerFolder);
			isFileExist = FileUtils.isFileExist(localServerFolder + fileName);
		}

		return isFileExist;
	}

}
