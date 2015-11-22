package encode.audio.entrypoint;

import org.approvaltests.Approvals;
import org.junit.Test;

import flux.AudioAnnounceTmlg;

public class AudioAnnounceEngineTest {

	private static final String HOST = "http://somehost/";
	private static final String REMOTE_AUDIO_FILE_NAME = "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav";
	private static final String REMOTE_AUDIO_FILE_NAME2 = "10.151.156.180Tue_Nov_05_141112_CET_2013343.mp3";

	@Test
	public void coverageAudioAnnounceEngine_wav_to_mp3() throws Exception {
		// Given
		String url = HOST + "10.151.156.180Mon_Nov_04_140724_CET_2013343";
		String targetFormat = "mp3";
		String remoteAudioFileName = REMOTE_AUDIO_FILE_NAME;

		// When
		String flux = coverageAudioAnnounceEngine(url, targetFormat, remoteAudioFileName);

		// Then
		Approvals.verify(flux);
	}

	@Test
	public void coverageAudioAnnounceEngine_mp3_to_wav() throws Exception {
	    // Given
		String url = HOST + "10.151.156.180Tue_Nov_05_141112_CET_2013343";
	    String targetFormat = "wav";
	    String remoteAudioFileName = REMOTE_AUDIO_FILE_NAME2;

	    // When
	    String flux = coverageAudioAnnounceEngine(url, targetFormat, remoteAudioFileName);

	    // Then
	    Approvals.verify(flux);
	}

	@Test(expected=AppTechnicalException.class)
	public void coverageAudioAnnounceEngine_notsupported() throws Exception {
	    // Given
		String url = HOST + "10.151.156.180Tue_Nov_05_141112_CET_2013343";
	    String targetFormat = "ogg";
	    String remoteAudioFileName = REMOTE_AUDIO_FILE_NAME2;

	    // When
	    coverageAudioAnnounceEngine(url, targetFormat, remoteAudioFileName);

	    // Then
	    // see @Test(expected=AppTechnicalException.class)
	}


	public String coverageAudioAnnounceEngine(String url, String targetFormat, String remoteAudioFileName) throws Exception {
		AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg(url, targetFormat, remoteAudioFileName);
		DataObject configAudioTmp = new AudioDataObject("." + targetFormat);

		DataObject httpDataObj = new HttpDataObj("./src/test/resources/", "http://localhost/get");

		LocalHTTPSServer localServerFolder = new LocalHTTPSServer();
		LocalTmpFolder localTmpFolder = new LocalTmpFolder();
		AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder, localTmpFolder);

		return audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);
	}
}
