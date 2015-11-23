package encode.audio.entrypoint;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.*;

import java.io.File;
import java.io.IOException;

import org.approvaltests.legacycode.LegacyApprovals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.dreamhead.moco.HttpServer;
import com.thoughtworks.xstream.XStream;

import flux.AudioAnnounceTmlg;
import flux.IFluxTmlg;

public class AudioAnnounceEngineTest {

    private static final String BASE_URL_FOR_INITIAL_DOWNLOAD = "http://localhost:12306/";
    private static final String HOST = "http://localhost:12306/";
    private static final String TEST_RESOURCE_DIR = "./src/test/resources/";
	private static final String REMOTE_AUDIO_FILE_NAME = "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav";
	private static final String REMOTE_AUDIO_FILE_NAME2 = "10.151.156.180Tue_Nov_05_141112_CET_2013343.mp3";
	private static final String REMOTE_AUDIO_FILE_NAME3 = "10.151.156.180Tue_Nov_05_141112_CET_2013343.ogg";
	private static final String REMOTE_AUDIO_FILE_NAME4 = "10.151.156.180Tue_Nov_05_141112_CET_2013343.tmp";

    private com.github.dreamhead.moco.Runner runner;
    HttpServer server = httpserver(12306);

    @Before
    public void setuphttp() {
        runner = runner(server);
        runner.start();
    }

    @After
    public void tearDown() {
        runner.stop();
    }

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

	public String coverageAudioAnnounceEngine(String url, String targetFormat, String remoteAudioFileName) throws IOException {
	    AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg(url, targetFormat, remoteAudioFileName);
	    DataObject configAudioTmp = new AudioDataObject("." + targetFormat);

	    TemporaryFolder tempFolder = new TemporaryFolder();
	    tempFolder.create();
	    String audioTempPath = tempFolder.getRoot().getAbsolutePath();
	    DataObject httpDataObj = new HttpDataObj(audioTempPath, "http://localhost/get");

	    File localServerFolder = tempFolder.newFolder("local_server_folder");
	    localServerFolder.mkdirs();

	    AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder.getAbsolutePath());

		IFluxTmlg availableEncodedAudioFile;
		try {
			availableEncodedAudioFile = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);
		} catch (AppTechnicalException e) {
			return e.getMessage();
		}
		tempFolder.delete();
		return  new XStream().toXML(availableEncodedAudioFile);
	}
}
