package encode.audio.entrypoint;

import org.approvaltests.Approvals;
import org.approvaltests.legacycode.LegacyApprovals;
import org.junit.Ignore;
import org.junit.Test;

import flux.AudioAnnounceTmlg;

public class AudioAnnounceEngineTest {

	private static final String REMOTE_AUDIO_FILE_NAME = "10.151.156.180Mon_Nov_04_140724_CET_2013343.wav";
    
	@Test public void 
    coverageAudioAnnounceEnginLockdown() throws Exception {
         Object[] sourceFileNames = {REMOTE_AUDIO_FILE_NAME, "10.151.156.180Tue_Nov_05_141112_CET_2013343.mp3"};
        Object[] targetFormats = {"wav", "mp3", "ogg"};
        Object[] finalUrls = { "null10.151.156.180Mon_Nov_04_140724_CET_2013343", "null10.151.156.180Tue_Nov_05_141112_CET_2013343"} ;
        LegacyApprovals.LockDown(this, "publishAudioFileVariations", targetFormats, finalUrls, sourceFileNames);
    }
    
    public String publishAudioFileVariations(String targetFormat, String finalUrl, String sourceFileName) throws AppTechnicalException {
        // Given
        AudioAnnounceTmlg audioFileMessage = new AudioAnnounceTmlg(finalUrl, targetFormat, sourceFileName);
        DataObject configAudioTmp = new AudioDataObject("." + targetFormat);
        DataObject httpDataObj = new HttpDataObj("./src/test/resources/", "http://localhost/get");
        
        LocalHTTPSServer localServerFolder = new LocalHTTPSServer();
        LocalTmpFolder localTmpFolder = new LocalTmpFolder();
        AudioAnnounceEngine audioAnnounceEngine = new AudioAnnounceEngine(localServerFolder, localTmpFolder);

        // When
        return  audioAnnounceEngine.publishAudioFile(audioFileMessage, configAudioTmp, httpDataObj);

    } 
    
}
