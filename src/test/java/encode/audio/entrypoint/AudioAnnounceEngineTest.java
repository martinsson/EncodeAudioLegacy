package encode.audio.entrypoint;

import java.util.HashMap;
import java.util.Map;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

import templating.TemplateEngine;
import templating.mustache.MustacheTemplateEngine;
import flux.ObixTmlgExeption;

public class AudioAnnounceEngineTest {

	@Test
	public void coverageAudioAnnounceEngine() throws Exception {
		// Given
		Boolean encodingActivated = true;
		String audioExtension = "wav";

		String audioFileMessage = buildAudioFileMessage(audioExtension);
		DataObject configAudioTmp = new AudioDataObject(encodingActivated, audioExtension);
		DataObject httpDataObj = new HttpDataObj("./src/test/resources/", "http://localhost/get");

		// When
		AudioAnnounceEngine aa = new AudioAnnounceEngine();
		String flux = aa.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);

		// Then
		String expectedFlux = "FluxTmlg [body=AnnounceAudioTmlg [url=http://localhost/get10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3, format=mp3, fileName=10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3]]";
		Assertions.assertThat(flux).isEqualTo(expectedFlux);

	}

	private String buildAudioFileMessage(final String audioExtension) throws ObixTmlgExeption {
		TemplateEngine te = new MustacheTemplateEngine();
		@SuppressWarnings("serial")
		Map<String, String> vals = new HashMap<String, String>() {
			{
				put("SOURCEFORMAT", audioExtension);
			}
		};

		String message = te.compile("src/test/resources/ogive2pscsAudio_with_transformed_audio.xml", vals);

		return message;
	}

}
