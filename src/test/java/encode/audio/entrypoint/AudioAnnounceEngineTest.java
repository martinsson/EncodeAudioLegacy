package encode.audio.entrypoint;

import org.approvaltests.legacycode.LegacyApprovals;
import org.junit.Test;

import flux.AudioAnnounceTmlg;
import flux.IFluxTmlg;

public class AudioAnnounceEngineTest {

	private static final String HOST = "http://somehost/";
	private static final String REMOTE_AUDIO_FILE_NAME = "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav";
	private static final String REMOTE_AUDIO_FILE_NAME2 = "10.151.156.180Tue_Nov_05_141112_CET_2013343.mp3";
	private static final String REMOTE_AUDIO_FILE_NAME3 = "10.151.156.180Tue_Nov_05_141112_CET_2013343.ogg";
	private static final String REMOTE_AUDIO_FILE_NAME4 = "10.151.156.180Tue_Nov_05_141112_CET_2013343.tmp";

	@Test
	public void coverageAudioAnnounceEngineLockdown() throws Exception {
		// Given
		String[] url = { HOST + "10.151.156.180Mon_Nov_04_140724_CET_2013343",	HOST + "10.151.156.180Tue_Nov_05_141112_CET_2013343" };
		String[] targetFormat = { "mp3", "wav", "ogg", "zip" };
		String[] remoteAudioFileName = { REMOTE_AUDIO_FILE_NAME, REMOTE_AUDIO_FILE_NAME2, REMOTE_AUDIO_FILE_NAME3, REMOTE_AUDIO_FILE_NAME4 };

		// When
		// Then
		LegacyApprovals.LockDown(this, "coverageAudioAnnounceEngine", url,	targetFormat, remoteAudioFileName);
	}

	public String coverageAudioAnnounceEngine(String url, String targetFormat, String remoteAudioFileName) {
		AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg(url,	targetFormat, remoteAudioFileName);
		DataObject configAudioTmp = new AudioDataObject("." + targetFormat);
		DataObject httpDataObj = new HttpDataObj("./src/test/resources/", "http://localhost/get");

		LocalHTTPSServer localServerFolder = new LocalHTTPSServer();
		LocalTmpFolder localTmpFolder = new LocalTmpFolder();
		AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder, localTmpFolder);

		IFluxTmlg availableEncodedAudioFile;
		try {
			availableEncodedAudioFile = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);
		} catch (AppTechnicalException e) {
			return e.getMessage();
		}
		return availableEncodedAudioFile.toString();
	}
}
