package encode.audio.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

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
	 * Constructor
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
				String binaryCommand = programExe + parameters;
				int exitValue = encodeFile(binaryCommand, path, fileName, newFilename);
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

    protected static int encodeFile(String binaryCommand, String path, String fileName, String newFilename) {
        
        int exitValue;
        try {
            String cmd = binaryCommand + " " + path + fileName + " " + path + newFilename;
            logger.log(LogService.LOG_INFO, "Running the command: " + cmd);
            logger.log(LogService.LOG_INFO, "Simulate Mp3Encoder.launchMp3Exec(" + cmd +")");
            byte[] fileData = readBytes(path, fileName);
            byte[] encodedData = encodeBytes(fileData);
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

    protected static void writeBytes(String path, String newFilename, byte[] encodedData) throws FileNotFoundException, IOException {
        File newFile = new File(path + newFilename);
        FileOutputStream fileInputStream = new FileOutputStream(newFile);
        fileInputStream.write(encodedData);
        logger.log(LogService.LOG_DEBUG, "Getting audio file date to send: OK");
        fileInputStream.close();
    }

    protected static byte[] encodeBytes(byte[] fileData) {
        byte[] encodedData = new byte[(int) fileData.length];
        
        //rotate data
        encodedData[0] = fileData[fileData.length-1];
        for (int i = 1; i < encodedData.length; i++) {
            encodedData[i] = fileData[i-1];
        }
        return encodedData;
    }

    protected static byte[] readBytes(String path, String fileName) throws FileNotFoundException, IOException {
        File newFile = new File(path + fileName);
        FileInputStream fileInputStream = new FileInputStream(newFile);
        byte[] binaryFile = new byte[(int) newFile.length()];
        fileInputStream.read(binaryFile);
        logger.log(LogService.LOG_DEBUG, "Getting audio file date to send: OK");
        fileInputStream.close();
        return binaryFile;
    }

	/**
	 * Save a file
	 *
	 * @throws CoreException
	 */
	public synchronized static void saveAudioFile(String fileName, byte[] filecontent) throws CoreException {

		try {
			logger.log(LogService.LOG_DEBUG, "Save the audio file: '" + fileName + "' in the locally tempory directory");
			BufferedOutputStream filewriter;
			filewriter = new BufferedOutputStream(new FileOutputStream(fileName));
			filewriter.write(filecontent);
			filewriter.close();
		} catch (IOException e) {
			throw new CoreException("Error when saving the audio file");
		}
	}

}
