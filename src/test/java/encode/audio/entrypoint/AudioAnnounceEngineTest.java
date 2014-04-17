package encode.audio.entrypoint;

import org.approvaltests.Approvals;
import org.fest.assertions.api.Assertions;
import org.junit.Test;

import encode.audio.utils.CoreException;
import flux.AudioAnnounceTmlg;

public class AudioAnnounceEngineTest {

	@Test
	public void coverageAudioAnnounceEngine() throws Exception {
		// Given
		String tagetAudioFileExtension = ".mp3";

		AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg("null10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3", "mp3", "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");
		DataObject configAudioTmp = new AudioDataObject(tagetAudioFileExtension);
		DataObject httpDataObj = new HttpDataObj("./src/test/resources/", "http://localhost/get");

		LocalHTTPSServer localServerFolder = new LocalHTTPSServer();
		LocalTmpFolder localTmpFolder = new LocalTmpFolder();
		AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder, localTmpFolder);

		// When
		String flux = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);

		// Then
		Approvals.verify(flux);
	}

	@Test
	public void coverageAudioAnnounce_with_no_bitrate_and_no_vbr_quality() throws Exception {
		// Given
		String tagetAudioFileExtension = ".mp3";
		String encodingBitrate = "";
		String enocdingVbrQuality = "";

		AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg("null10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3", "mp3", "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");
		DataObject configAudioTmp = new AudioDataObject(tagetAudioFileExtension, encodingBitrate, enocdingVbrQuality);
		DataObject httpDataObj = new HttpDataObj("./src/test/resources/", "http://localhost/get");

		LocalHTTPSServer localServerFolder = new LocalHTTPSServer();
		LocalTmpFolder localTmpFolder = new LocalTmpFolder();
		AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder, localTmpFolder);

		// When
		String flux = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);

		// Then
		Approvals.verify(flux);
	}

	@Test
	public void coverageAudioAnnounceEngine_encoding_audio_disabled() throws Exception {
		// Given
		Boolean encodingActivated = false;
		Boolean mp3encoderSuccess = true;
		String tagetAudioFileExtension = ".mp3";

		AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg("null10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3", "mp3", "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");
		DataObject configAudioTmp = new AudioDataObject(encodingActivated, mp3encoderSuccess, tagetAudioFileExtension);
		DataObject httpDataObj = new HttpDataObj("./src/test/resources/", "http://localhost/get");

		LocalHTTPSServer localServerFolder = new LocalHTTPSServer();
		LocalTmpFolder localTmpFolder = new LocalTmpFolder();
		AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder, localTmpFolder);

		// When
		String flux = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);

		// Then
		Approvals.verify(flux);
	}

	@Test
	public void coverageAudioAnnounceEngine_m3pencoder_fails() throws Exception {
		// Given
		Boolean encodingActivated = true;
		Boolean mp3encoderSuccess = false;
		String tagetAudioFileExtension = ".mp3";

		AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg("null10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3", "mp3", "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");
		DataObject configAudioTmp = new AudioDataObject(encodingActivated, mp3encoderSuccess, tagetAudioFileExtension);
		DataObject httpDataObj = new HttpDataObj("./src/test/resources/nonexistingdirectory", "http://localhost/get");

		LocalHTTPSServer localServerFolder = new LocalHTTPSServer();
		LocalTmpFolder localTmpFolder = new LocalTmpFolder();
		AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder, localTmpFolder);

		try {
			// When
			String flux = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);
			Assertions.fail("expected exception");
		} catch (AppTechnicalException ate) {
			// Then
			Approvals.verify(ate.getMessage());
		}
	}

	@Test
	public void coverageAudioAnnounceEngine_download_audio_file_fails() throws Exception {
		// Given
		String tagetAudioFileExtension = ".mp3";
		Boolean downloadAudioFileSuccess = false;
		Boolean uploadAudioAnnounceSuccess = true;
		Boolean httpConfigSuccess = true;

		AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg("null10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3", "mp3", "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");
		DataObject configAudioTmp = new AudioDataObject(tagetAudioFileExtension);
		DataObject httpDataObj = new HttpDataObj(downloadAudioFileSuccess, uploadAudioAnnounceSuccess, httpConfigSuccess, "./src/test/resources/", "http://localhost/get");

		LocalHTTPSServer localServerFolder = new LocalHTTPSServer();
		LocalTmpFolder localTmpFolder = new LocalTmpFolder();
		AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder, localTmpFolder);

		try {
			// When
			String flux = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);
			Assertions.fail("expected exception");
		} catch (AppTechnicalException ate) {
			// Then
			Approvals.verify(ate.getMessage());
		}
	}

	@Test
	public void coverageAudioAnnounceEngine_encoding_audio_already_in_the_target_format() throws Exception {
		// Given
		String tagetAudioFileExtension = ".wav";

		AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg("null10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3", "mp3", "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");
		DataObject configAudioTmp = new AudioDataObject(tagetAudioFileExtension);
		DataObject httpDataObj = new HttpDataObj("./src/test/resources/", "http://localhost/get");

		LocalHTTPSServer localServerFolder = new LocalHTTPSServer();
		LocalTmpFolder localTmpFolder = new LocalTmpFolder();
		AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder, localTmpFolder);

		// When
		String flux = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);

		// Then
		Approvals.verify(flux);
	}

	@Test
	public void coverageAudioAnnounceEngine_encoding_audio_already_exists_on_Local_HTTPS_Server() throws Exception {
		// Given
		String tagetAudioFileExtension = ".wav";

		AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg("null10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3", "mp3", "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");
		DataObject configAudioTmp = new AudioDataObject(tagetAudioFileExtension);
		DataObject httpDataObj = new HttpDataObj("./src/test/resources/", "http://localhost/get");

		LocalHTTPSServer localHTTPSServer = new LocalHTTPSServer();
		localHTTPSServer.addFile("10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");
		LocalTmpFolder localTmpFolder = new LocalTmpFolder();

		AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localHTTPSServer, localTmpFolder);

		// When
		String flux = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);

		// Then
		Approvals.verify(flux);
	}

	@Test
	public void coverageAudioAnnounceEngine_the_encoded_audio_file_already_on_Local_Tmp_Folder() throws Exception {
		// Given
		String tagetAudioFileExtension = ".wav";

		AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg("null10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3", "mp3", "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");
		DataObject configAudioTmp = new AudioDataObject(tagetAudioFileExtension);
		DataObject httpDataObj = new HttpDataObj("./src/test/resources/", "http://localhost/get");

		LocalHTTPSServer localHTTPSServer = new LocalHTTPSServer();
		LocalTmpFolder localTmpFolder = new LocalTmpFolder();
		localTmpFolder.addFile(httpDataObj.getString("audio_temp_path") + "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");

		AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localHTTPSServer, localTmpFolder);

		// When
		String flux = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);

		// Then
		Approvals.verify(flux);
	}

	@Test
	public void coverageAudioAnnounceEngine_the_core_layer_cannot_upload_the_file() throws Exception {
		// Given
		String tagetAudioFileExtension = ".mp3";
		Boolean downloadAudioFileSuccess = true;
		Boolean uploadAudioAnnounceSuccess = false;
		Boolean httpConfigSuccess = true;

		AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg("null10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3", "mp3", "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");
		DataObject configAudioTmp = new AudioDataObject(tagetAudioFileExtension);
		DataObject httpDataObj = new HttpDataObj(downloadAudioFileSuccess, uploadAudioAnnounceSuccess, httpConfigSuccess,"./src/test/resources/", "http://localhost/get");

		LocalHTTPSServer localServerFolder = new LocalHTTPSServer();
		LocalTmpFolder localTmpFolder = new LocalTmpFolder();
		AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder, localTmpFolder);

		try {
			// When
			String flux = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);
			Assertions.fail("expected exception");
		} catch (AppTechnicalException ate) {
			// Then
			Approvals.verify(ate.getMessage());
		}
	}

}
