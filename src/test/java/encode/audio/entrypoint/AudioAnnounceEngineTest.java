package encode.audio.entrypoint;

import static com.github.dreamhead.moco.Moco.file;
import static com.github.dreamhead.moco.Moco.httpserver;
import static com.github.dreamhead.moco.Moco.log;
import static com.github.dreamhead.moco.Moco.with;
import static com.github.dreamhead.moco.Runner.runner;

import java.io.File;
import java.io.IOException;

import org.approvaltests.legacycode.LegacyApprovals;
import org.approvaltests.reporters.ClipboardReporter;
import org.approvaltests.reporters.UseReporter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.dreamhead.moco.HttpServer;
import com.google.common.base.Joiner;
import com.thoughtworks.xstream.XStream;

import flux.AudioAnnounceTmlg;
import flux.IFluxTmlg;

public class AudioAnnounceEngineTest {
    
    
    private com.github.dreamhead.moco.Runner runner;
    HttpServer server = httpserver(12306);
    private static final String TEST_RESOURCE_DIR = "./src/test/resources/";

    @Before
    public void setuphttp() {
        runner = runner(server);
        runner.start();
    }

    @After
    public void tearDown() {
        runner.stop();
    }

	private static final String REMOTE_AUDIO_FILE_NAME2 = "10.151.156.180Tue_Nov_05_141112_CET_2013343.mp3";
	private static final String HOST = "http://localhost:12306/";
	private static final String REMOTE_AUDIO_FILE_NAME = "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav";
    
    @Test public void 
    coverageAudioAnnounceEnginLockdown() throws Exception {
         Object[] sourceFileNames = {REMOTE_AUDIO_FILE_NAME, REMOTE_AUDIO_FILE_NAME2};
        Object[] targetFormats = {"wav", "mp3", "ogg"};
        Object[] finalUrls = { "10.151.156.180Mon_Nov_04_140724_CET_2013343", "10.151.156.180Tue_Nov_05_141112_CET_2013343"} ;

        server.response(with(file(TEST_RESOURCE_DIR + "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav") ));
        
        LegacyApprovals.LockDown(this, "publishAudioFileVariations", targetFormats, finalUrls, sourceFileNames);
    }

    public String publishAudioFileVariations(String targetFormat, String finalUrl, String sourceFileName) throws AppTechnicalException, IOException {
        // Given
        AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg(HOST + finalUrl, targetFormat, sourceFileName);
        DataObject configAudioTmp = new AudioDataObject("." + targetFormat);
        
        TemporaryFolder tempFolder = new TemporaryFolder();
        tempFolder.create();
        String audioTempPath = tempFolder.getRoot().getAbsolutePath() + "/";
        DataObject httpDataObj = new HttpDataObj(audioTempPath, "http://localhost/get");

        File localServerFolder = tempFolder.newFolder("local_server_folder");
        localServerFolder.mkdirs();
        
        AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder.getAbsolutePath() + "/");
        // When
        IFluxTmlg availableEncodedAudioFile = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);
        return  new XStream().toXML(availableEncodedAudioFile);

    }
}
