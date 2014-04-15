package encode.audio.entrypoint;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

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

		flux.ref.AudioAnnounceTmlg audioFileMessageRef = new flux.ref.AudioAnnounceTmlg("null10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3", "mp3", "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");
		encode.audio.entrypoint.ref.LocalHTTPSServer localServerFolderRef = new encode.audio.entrypoint.ref.LocalHTTPSServer();
		encode.audio.entrypoint.ref.LocalTmpFolder localTmpFolderRef = new encode.audio.entrypoint.ref.LocalTmpFolder();
		encode.audio.entrypoint.ref.AudioAnnounceEngine audioAnnounceEngineRef = new encode.audio.entrypoint.ref.AudioAnnounceEngine(localServerFolderRef, localTmpFolderRef);
		
		// When
		String actualFlux = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);
		String expectedFlux = audioAnnounceEngineRef.publishAudioFile(audioFileMessageRef, configAudioTmp, httpDataObj);

		// Then
		Assertions.assertThat(actualFlux).isEqualTo(expectedFlux);
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

		flux.ref.AudioAnnounceTmlg audioFileMessageRef = new flux.ref.AudioAnnounceTmlg("null10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3", "mp3", "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");
		encode.audio.entrypoint.ref.LocalHTTPSServer localServerFolderRef = new encode.audio.entrypoint.ref.LocalHTTPSServer();
		encode.audio.entrypoint.ref.LocalTmpFolder localTmpFolderRef = new encode.audio.entrypoint.ref.LocalTmpFolder();
		encode.audio.entrypoint.ref.AudioAnnounceEngine audioAnnounceEngineRef = new encode.audio.entrypoint.ref.AudioAnnounceEngine(localServerFolderRef, localTmpFolderRef);

		// When
		String actualFlux = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);
		String expectedFlux = audioAnnounceEngineRef.publishAudioFile(audioFileMessageRef, configAudioTmp, httpDataObj);

		// Then
		Assertions.assertThat(actualFlux).isEqualTo(expectedFlux);
	}
}
