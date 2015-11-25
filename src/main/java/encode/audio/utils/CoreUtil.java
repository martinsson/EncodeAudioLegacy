package encode.audio.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import encode.audio.entrypoint.DataObject;

/**
 * Util class used by the business layer.
 *
 * @author FOO
 * @version 1.0
 */
public final class CoreUtil {

	private static LogService logger = new DummyLogService();

	public static final String HEADER_ERROR = "ErrorPSCS: ";
	public static final String AUDIO_ENCODING_ERROR = HEADER_ERROR
			+ "The core layer cannot encode the audio file";
	public static final String HTTP_DOWNLOAD_ERROR = HEADER_ERROR
			+ "The core layer cannot download the file";
	public static final String HTTP_UPLOAD_ERROR = HEADER_ERROR
			+ "The core layer cannot upload the file";
	public static final String OBIX_FORMAT_ERROR = HEADER_ERROR
			+ "Invalid message format";
	public static final String HTTP_CONFIG_ERROR = HEADER_ERROR
			+ "The core layer cannot download the web server configuration";

	public static final String DOWNLOAD_AUDIO_FILE_SUCCESS = "download_audio_file_success";
	public static final String MP3ENCODER_SUCESS = "mp3encoder_sucess";
	public static final String HTTP_UPLOAD_SUCCESS = "http_upload_success";
	public static final String HTTP_CONFIG_SUCCESS = "http_config_success";

	/**
	 * @param oldAudioFileName
	 * @return AudioFile
	 * */
	public static AudioFile simulateEncodedAudioFileProperties(
			String oldAudioFileName, String extension) {
		return new AudioFile(oldAudioFileName.substring(0,
				oldAudioFileName.lastIndexOf("."))
				+ extension,
				extension.substring((extension.lastIndexOf(".") + 1)));
	}

	/**
	 * Encoding an audio file to another format
	 *
	 * @param fileName
	 * @return AudioFile
	 * @throws CoreException
	 */
	public synchronized static AudioFile encodeAudioFile(String path,
			String fileName, DataObject configAudio) throws CoreException {
		AudioFile fileResult = null;

		String fileNameWithoutExtension = fileName.substring(0,
				fileName.lastIndexOf("."));
		String oldExtension = fileName
				.substring((fileName.lastIndexOf(".") + 1));

		logger.log(LogService.LOG_INFO, "Encoding audio : enable");
		String programExe = configAudio
				.getString(Configurations.PATH_PROGRAM_EXE);
		String finalEncodingAudioFileExtension = configAudio
				.getString(Configurations.FINAL_AUDIO_FILE_EXTENSION);
		String bitrate = configAudio.getString(Configurations.ENCODING_BITRATE);
		String vbrQuality = configAudio
				.getString(Configurations.ENCODING_VBR_QUALITY);

		String newFilename = fileNameWithoutExtension
				+ finalEncodingAudioFileExtension;

		String parameters = "";
		parameters = parameters.concat(" -b " + bitrate);
		parameters = parameters.concat(" -V " + vbrQuality);

		String cmd = programExe + parameters + " " + path + fileName + " "
				+ path + newFilename;

		logger.log(LogService.LOG_INFO, "Running the command: " + cmd);

		logger.log(LogService.LOG_INFO, "Simulate Mp3Encoder.launchMp3Exec("
				+ cmd + ")");
		String binaryCommand = programExe + parameters;
		int exitValue = encodeFile(binaryCommand, path, fileName, newFilename,
				oldExtension, finalEncodingAudioFileExtension);
		logger.log(LogService.LOG_INFO,
				"End of the encoding audio file with return code: " + exitValue);

		if (exitValue == 0) {
			fileResult = new AudioFile(newFilename,
					finalEncodingAudioFileExtension
							.substring((finalEncodingAudioFileExtension
									.lastIndexOf(".") + 1)));
			return fileResult;
		} else {
			logger.log(LogService.LOG_ERROR, "Error while trying to encode "
					+ fileName);
			throw new CoreException("Cant encode file!");
		}

	}

	protected static int encodeFile(String binaryCommand, String path,
			String fileName, String newFilename, String oldExtension,
			String finalEncodingAudioFileExtension) {

		int exitValue;
		try {
			String cmd = binaryCommand + " " + path + fileName + " " + path
					+ newFilename;
			logger.log(LogService.LOG_INFO, "Running the command: " + cmd);
			logger.log(LogService.LOG_INFO,
					"Simulate Mp3Encoder.launchMp3Exec(" + cmd + ")");
			byte[] fileData = readBytes(path, fileName);
			byte[] encodedData;
			if (oldExtension.equals("wav")) {
				if (finalEncodingAudioFileExtension.equals(".mp3")) {

					logger.log(LogService.LOG_INFO, "encode from wav to mp3 : "
							+ fileName);

					encodedData = encodeBytes1(fileData);
				} else if (finalEncodingAudioFileExtension.equals(".zip")) {
					logger.log(LogService.LOG_INFO, "encode from mp3 to wav : "
							+ fileName);
					encodedData = zip(fileData, fileName);
				} else {
					logger.log(LogService.LOG_ERROR,
							"encoding error, unsupported transformation from "
									+ oldExtension + " to "
									+ finalEncodingAudioFileExtension);
					return 1;
				}
			} else if (oldExtension.equals("mp3")) {
				if (finalEncodingAudioFileExtension.equals(".wav")) {
					logger.log(LogService.LOG_INFO, "encode from mp3 to wav : "
							+ fileName);
					encodedData = encodeBytes2(fileData);
				} else if (finalEncodingAudioFileExtension.equals(".zip")) {
					logger.log(LogService.LOG_INFO, "encode from mp3 to wav : "
							+ fileName);
					encodedData = zip(fileData, fileName);
				} else {
					logger.log(LogService.LOG_ERROR,
							"encoding error, unsupported transformation from "
									+ oldExtension + " to "
									+ finalEncodingAudioFileExtension);
					return 1;
				}
			} else {
				logger.log(LogService.LOG_ERROR,
						"encoding error, unsupported transformation from "
								+ oldExtension + " to "
								+ finalEncodingAudioFileExtension);
				return 1;
			}
			writeBytes(path, newFilename, encodedData);
			exitValue = 0;
		} catch (FileNotFoundException e) {
			logger.log(LogService.LOG_ERROR, "error", e);
			exitValue = 1;
		} catch (IOException e) {
			logger.log(LogService.LOG_ERROR, "error", e);
			exitValue = 1;
		}

		return exitValue;
	}

	private static byte[] zip(byte[] fileData, String filename)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		ZipEntry entry = new ZipEntry(filename);
		entry.setSize(fileData.length);
		zos.putNextEntry(entry);
		zos.write(fileData);
		zos.closeEntry();
		zos.close();
		return baos.toByteArray();
	}

	private static byte[] encodeBytes2(byte[] fileData) {
		byte[] encodedData = new byte[(int) fileData.length];

		// rotate data
		encodedData[encodedData.length - 1] = fileData[0];
		for (int i = 0; i < encodedData.length - 1; i++) {
			encodedData[i] = fileData[i + 1];
		}
		return encodedData;
	}

	protected static byte[] encodeBytes1(byte[] fileData) {
		byte[] encodedData = new byte[(int) fileData.length];

		// rotate data
		encodedData[0] = fileData[fileData.length - 1];
		for (int i = 1; i < encodedData.length; i++) {
			encodedData[i] = fileData[i - 1];
		}
		return encodedData;
	}

	protected static byte[] readBytes(String path, String fileName)
			throws FileNotFoundException, IOException {
		File newFile = new File(path + fileName);
		FileInputStream fileInputStream = new FileInputStream(newFile);
		byte[] binaryFile = new byte[(int) newFile.length()];
		fileInputStream.read(binaryFile);
		logger.log(LogService.LOG_DEBUG, "Getting audio file date to send: OK");
		fileInputStream.close();
		return binaryFile;
	}

	protected static void writeBytes(String path, String newFilename,
			byte[] encodedData) throws FileNotFoundException, IOException {
		String destinationfilePath = path + newFilename;
		writeBytes(destinationfilePath, encodedData);
	}

	public static void writeBytes(String destinationfilePath, byte[] fileData)
			throws FileNotFoundException, IOException {
		File newFile = new File(destinationfilePath);
		FileOutputStream fileInputStream = new FileOutputStream(newFile);
		fileInputStream.write(fileData);
		logger.log(LogService.LOG_DEBUG, "Getting audio file date to send: OK");
		fileInputStream.close();

	}
}
