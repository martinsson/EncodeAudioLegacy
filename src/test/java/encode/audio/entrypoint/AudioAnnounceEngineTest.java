package encode.audio.entrypoint;

import java.io.File;
import java.io.IOException;

import org.approvaltests.Approvals;
import org.approvaltests.legacycode.LegacyApprovals;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.dreamhead.moco.HttpServer;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.*;

import com.thoughtworks.xstream.XStream;

import flux.AudioAnnounceTmlg;
import flux.IFluxTmlg;

public class AudioAnnounceEngineTest {
    
    
    private static final String BASE_URL_FOR_INITIAL_DOWNLOAD = "http://localhost:12306/";
    private com.github.dreamhead.moco.Runner runner;
    HttpServer server = httpserver(12306, log());
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

	private static final String REMOTE_AUDIO_FILE_NAME = "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav";
    
	@Test public void 
    coverageAudioAnnounceEnginLockdown() throws Exception {
         Object[] sourceFileNames = {REMOTE_AUDIO_FILE_NAME, "10.151.156.180Tue_Nov_05_141112_CET_2013343.mp3"};
        Object[] targetFormats = {"wav", "mp3", "ogg"};
        Object[] finalUrls = { BASE_URL_FOR_INITIAL_DOWNLOAD + "10.151.156.180Mon_Nov_04_140724_CET_2013343", BASE_URL_FOR_INITIAL_DOWNLOAD + "10.151.156.180Tue_Nov_05_141112_CET_2013343"} ;

        server.response(with(file(TEST_RESOURCE_DIR + "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav") ));
        
        LegacyApprovals.LockDown(this, "publishAudioFileVariations", targetFormats, finalUrls, sourceFileNames);
    }
    
    public String publishAudioFileVariations(String targetFormat, String finalUrl, String sourceFileName) throws AppTechnicalException, IOException {
        // Given
        
        String sourceFormat = sourceFileName.substring(sourceFileName.length()-3);
        AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg(finalUrl, sourceFormat, sourceFileName);
        DataObject configAudioTmp = new AudioDataObject("." + targetFormat);
        
        TemporaryFolder tempFolder = new TemporaryFolder();
        tempFolder.create();
        String audioTempPath = tempFolder.getRoot().getAbsolutePath();
        DataObject httpDataObj = new HttpDataObj(audioTempPath, "http://localhost/get");
        File localServerFolder = tempFolder.newFolder("local_server_folder");
	localServerFolder.mkdirs();
        
        AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder.getAbsolutePath());

        // When
        IFluxTmlg flux = audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);
        tempFolder.delete();
        return new XStream().toXML(flux);

    } 
    
}
