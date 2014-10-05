package encode.audio.entrypoint;

import org.approvaltests.Approvals;
import org.junit.Ignore;
import org.junit.Test;

import flux.AudioAnnounceTmlg;

public class AudioAnnounceEngineTest {

	private static final String HOST = "http://somehost/";
	private static final String REMOTE_AUDIO_FILE_NAME = "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav";
	private static final String REMOTE_AUDIO_FILE_NAME2 = "10.151.156.180Tue_Nov_05_141112_CET_2013343.mp3";

	@Test
	public void coverageAudioAnnounceEngine() throws Exception {
		// Given
		String targetFormat = "mp3";

		AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg(HOST + "10.151.156.180Mon_Nov_04_140724_CET_2013343", targetFormat, REMOTE_AUDIO_FILE_NAME);
		DataObject configAudioTmp = new AudioDataObject("." + targetFormat);

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
	public void coverageAudioAnnounceEngine_mp3_to_wav() throws Exception {
	    // Given
	    String targetFormat = "wav";

	    AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg(HOST + "10.151.156.180Tue_Nov_05_141112_CET_2013343", targetFormat, REMOTE_AUDIO_FILE_NAME2);
	    DataObject configAudioTmp = new AudioDataObject("." + targetFormat);
	    DataObject httpDataObj = new HttpDataObj("./src/test/resources/", "http://localhost/get");

	    LocalHTTPSServer localServerFolder = new LocalHTTPSServer();
	    LocalTmpFolder localTmpFolder = new LocalTmpFolder();
	    AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder, localTmpFolder);

	    // When
	    String flux = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);

	    // Then
	    Approvals.verify(flux);
	}

	@Test(expected=AppTechnicalException.class)
	public void coverageAudioAnnounceEngine_notsupported() throws Exception {
	    // Given
	    String targetFormat = "ogg";

	    AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg(HOST + "10.151.156.180Tue_Nov_05_141112_CET_2013343", targetFormat, REMOTE_AUDIO_FILE_NAME2);
	    DataObject configAudioTmp = new AudioDataObject("." + targetFormat);
	    DataObject httpDataObj = new HttpDataObj("./src/test/resources/", "http://localhost/get");

	    LocalHTTPSServer localServerFolder = new LocalHTTPSServer();
	    LocalTmpFolder localTmpFolder = new LocalTmpFolder();
	    AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder, localTmpFolder);

	    // When
	    String flux = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);

	}
}
