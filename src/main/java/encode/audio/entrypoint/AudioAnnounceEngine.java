package encode.audio.entrypoint;

import java.io.IOException;

import encode.audio.utils.AudioFile;
import encode.audio.utils.CoreException;
import encode.audio.utils.CoreUtil;
import encode.audio.utils.DummyLogService;
import encode.audio.utils.LogService;
import flux.AudioAnnounceTmlg;
import flux.FluxTmlg;
import flux.IAudioAnnounceTmlg;
import flux.IFluxTmlg;

public class AudioAnnounceEngine {

	private Boolean download = false;
	private Boolean encode = false;

	private LogService logger = new DummyLogService();
	private DataObject audioConfig;
	private DataObject httpConfig;

	private LocalHTTPSServer localHTTPSServer;
	private LocalTmpFolder localTmpFolder;

	public AudioAnnounceEngine(LocalHTTPSServer localHTTPSServer, LocalTmpFolder localTmpFolder) {
		super();
		this.localHTTPSServer = localHTTPSServer;
		this.localTmpFolder = localTmpFolder;
	}

	public String publishAudioFile(AudioAnnounceTmlg audioAnnounceTmlg, DataObject audioConfigTmp, DataObject httpConfigTmp) throws AppTechnicalException {
		try {
			this.audioConfig = audioConfigTmp;
			this.httpConfig = httpConfigTmp;
			IFluxTmlg targetAudioFileMessage = new FluxTmlg(audioAnnounceTmlg);

			IAudioAnnounceTmlg audioAnnounce = targetAudioFileMessage.getBody().getTravelInfo().getAudioAnnounce();
			AudioFile newAudioFile = processAudioAnnounce(targetAudioFileMessage, audioAnnounce);

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

		if (localHTTPSServer.isAudioFileExistsOnServer(encodedFilename)) {
			logger.log(LogService.LOG_DEBUG, "The audio file '" + encodedFilename + "' already exists on the HTTPS server");
		} else {
			logger.log(LogService.LOG_DEBUG, "The audio file '" + encodedFilename + "' does not exist on the HTTPS server");

			if (localTmpFolder.isAudioFileExistsOnTmpFolder(audioTempPath + encodedFilename)) {
				logger.log(LogService.LOG_DEBUG, "The encoded audio file already exists locally");
				download = true;
				encode = true;
			} else {
				downnloadAudioFile(localTmpFolder, fileName, filePath, fileUrl);
				download = true;

				logger.log(LogService.LOG_DEBUG, "Encoding audio file :" + filePath + " (path : " + httpConfig.getString("audio_temp_path") + ")");
				newAudioFile = CoreUtil.encodeAudioFile(audioTempPath, fileName, audioConfig);
				encode = true;
			}

			// Uploading encoded audio file to HTTPS server
			uploadAudioAnnounce(localHTTPSServer, newAudioFile);
		}

		return newAudioFile;
	}

	/**
	 * Download the audio file if not already exists in the temp directory
	 *
	 * @param fileName
	 * @param fileUrl
	 * @param filePath
	 *
	 * */
	public void downnloadAudioFile(LocalTmpFolder localTmpFolder, String fileName, String destinationfilePath, String fileUrl) throws CoreException {
		if (localTmpFolder.isAudioFileExistsOnTmpFolder(httpConfig.getString("audio_temp_path") + fileName)) {
			logger.log(LogService.LOG_DEBUG, "the original audio file already exists in local: '" + httpConfig.getString("audio_temp_path") + fileName + "'");
		} else {
			logger.log(LogService.LOG_DEBUG, "simulate downloading audio file: '" + httpConfig.getString("audio_temp_path") + fileName + "' to locally path: " + destinationfilePath);
			Boolean downloadAudioFileSuccess = httpConfig.getBoolean(CoreUtil.DOWNLOAD_AUDIO_FILE_SUCCESS);
			if (!downloadAudioFileSuccess)
				throw new CoreException(CoreUtil.HTTP_DOWNLOAD_ERROR);
		}
	}

	public void uploadAudioAnnounce(LocalHTTPSServer localServerFolder, AudioFile newAudioFile) throws IOException, CoreException {
		logger.log(LogService.LOG_DEBUG, "Uploading audio file to HTTPS server");
		localServerFolder.uploadAudioAnnounce(newAudioFile.getName());
		Boolean htpp_UploadSuccess = httpConfig.getBoolean(CoreUtil.HTTP_UPLOAD_SUCCESS);
		if (!htpp_UploadSuccess)
			throw new CoreException(CoreUtil.HTTP_UPLOAD_ERROR);
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
}
