package encode.audio.utils;

import encode.audio.entrypoint.DataObject;

/**
 * Util class used by the business layer.
 *
 * @author FOO
 * @version 1.0
 */
public final class CoreUtil {

	private static CoreUtil instance;
	private static LogService logger = new DummyLogService();

	/**
	 * Constructeur
	 */
	private CoreUtil() {

	}

	/**
	 *
	 * @return PscsCoreUtil
	 */
	public static synchronized CoreUtil getInstance() {
		if (instance == null) {
			instance = new CoreUtil();
		}
		return instance;
	}

	public static final String HEADER_ERROR = "ErrorPSCS: ";
	public static final String AUDIO_ENCODING_ERROR = HEADER_ERROR + "The core layer cannot encode the audio file";
	public static final String HTTP_DOWNLOAD_ERROR = HEADER_ERROR + "The core layer cannot download the file";
	public static final String HTTP_UPLOAD_ERROR = HEADER_ERROR + "The core layer cannot upload the file";
	public static final String OBIX_FORMAT_ERROR = HEADER_ERROR + "Invalid message format";
	public static final String HTTP_CONFIG_ERROR = HEADER_ERROR + "The core layer cannot download the web server configuration";

	public static final String DOWNLOAD_AUDIO_FILE_SUCCESS = "download_audio_file_success";
	public static final String MP3ENCODER_SUCESS = "mp3encoder_sucess";
	public static final String HTTP_UPLOAD_SUCCESS = "http_upload_success";
	public static final String HTTP_CONFIG_SUCCESS = "http_config_success";

	/**
	 * @param oldAudioFileName
	 * @return AudioFile
	 * */
	public static AudioFile simulateEncodedAudioFileProperties(String oldAudioFileName, String extension) {
		return new AudioFile(oldAudioFileName.substring(0, oldAudioFileName.lastIndexOf(".")) + extension, extension.substring((extension.lastIndexOf(".") + 1)));
	}

	/**
	 * Encoding an audio file to another format
	 *
	 * @param fileName
	 * @return AudioFile
	 * @throws CoreException
	 */
	public synchronized static AudioFile encodeAudioFile(String path, String fileName, DataObject configAudio) throws CoreException {
		AudioFile fileResult = null;

		String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
		String oldExtension = fileName.substring((fileName.lastIndexOf(".") + 1));

		if (!configAudio.getBoolean(Configurations.ENCODING_ACTIVATE)) {

			logger.log(LogService.LOG_INFO, "Encoding audio : disable");
			fileResult = new AudioFile(fileName, oldExtension);
		} else {
			logger.log(LogService.LOG_INFO, "Encoding audio : enable");
			String programExe = configAudio.getString(Configurations.PATH_PROGRAM_EXE);
			String finalEncodingAudioFileExtension = configAudio.getString(Configurations.FINAL_AUDIO_FILE_EXTENSION);
			String bitrate = configAudio.getString(Configurations.ENCODING_BITRATE);
			String vbrQuality = configAudio.getString(Configurations.ENCODING_VBR_QUALITY);

			if (oldExtension.equals(finalEncodingAudioFileExtension.substring((finalEncodingAudioFileExtension.lastIndexOf(".") + 1)))) {
				logger.log(LogService.LOG_INFO, "Encodaging audio : The audio file to encode '" + fileName + "' is already in the target format");
				fileResult = new AudioFile(fileName, oldExtension);
			} else {

				String newFilename = fileNameWithoutExtension + finalEncodingAudioFileExtension;

				String parameters = "";
				if (!bitrate.equals("")) {
					parameters = parameters.concat(" -b " + bitrate);
				}
				if (!vbrQuality.equals("")) {
					parameters = parameters.concat(" -V " + vbrQuality);
				}

				String cmd = programExe + parameters + " " + path + fileName + " " + path + newFilename;

				logger.log(LogService.LOG_INFO, "Running the command: " + cmd);

				logger.log(LogService.LOG_INFO, "Simulate Mp3Encoder.launchMp3Exec(" + cmd + ")");
				Boolean m3pencoderSucess = configAudio.getBoolean(MP3ENCODER_SUCESS);
				int exitValue = (m3pencoderSucess) ? 0 : 1;
				logger.log(LogService.LOG_INFO, "End of the encoding audio file with return code: " + exitValue);

				if (exitValue == 0) {
					fileResult = new AudioFile(newFilename, finalEncodingAudioFileExtension.substring((finalEncodingAudioFileExtension.lastIndexOf(".") + 1)));
				} else {
					throw new CoreException("Error when encoding audio file with return code: " + exitValue);
				}
			}
		}
		return fileResult;
	}

}
