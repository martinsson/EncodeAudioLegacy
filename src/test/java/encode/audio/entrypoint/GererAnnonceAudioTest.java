
package encode.audio.entrypoint;
import static org.mockito.Matchers.anyString;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.approvaltests.legacycode.LegacyApprovals;
import org.approvaltests.reporters.UseReporter;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import templating.TemplateEngine;
import templating.mustache.MustacheTemplateEngine;
import encode.audio.entrypoint.DataObject;
import encode.audio.entrypoint.FileUtils;
import encode.audio.entrypoint.GererAnnonceAudio;
import encode.audio.utils.CoreException;
import encode.audio.utils.Mp3Encoder;
import flux.ObixTmlgExeption;


@RunWith(PowerMockRunner.class)
@PrepareForTest(Mp3Encoder.class)
@UseReporter(MeldReporter.class)

public class GererAnnonceAudioTest {

	private static final Object[] ALL_BOOLEAN_VALUES = { true, false };

//	@Test
//	public void runOnce() throws Exception {
//		assertEquals("kbaskhjfksadjf", allGererAnnonceAudio(true, "wav", 128, "lame"));
//	}
	
	@Test 
	public void transformationLock() throws Exception {
		Object[] encodingActivated = ALL_BOOLEAN_VALUES;
		

		Object[] conversionBinaryName = {"lame"};
		Object[] bitrate = {128};
		Object[] audioExtension = {"mp3", "wav"};
		Object[] encoding = ALL_BOOLEAN_VALUES;
		Object[] downLoadError = ALL_BOOLEAN_VALUES;
		
		LegacyApprovals.LockDown(this, "allGererAnnonceAudioCatchExceptions", encodingActivated, audioExtension , bitrate, conversionBinaryName, encoding, downLoadError );
	}
	
	public String allGererAnnonceAudioCatchExceptions(Boolean encodingActivated, String audioExtension , Integer bitrate, String conversionBinaryName, Boolean encodingSuccess, Boolean downloadError){
		try {
			return allGererAnnonceAudio(encodingActivated, audioExtension, bitrate, conversionBinaryName, encodingSuccess, downloadError);
		}catch (Exception e) {
			e.printStackTrace();
			return e.getClass().getName();
		}		
	}
	
	public String allGererAnnonceAudio(Boolean encodingActivated, String audioExtension , Integer bitrate, String conversionBinaryName, Boolean encodingSuccess, Boolean downloadError)
			throws Exception {
		TemporaryFolder rootFolder = setup();
		try {
			
			GererAnnonceAudio aa = testableGererAnnonceAudio(rootFolder, downloadError);

			final File downloadDir = rootFolder.newFolder();
			downloadDir.mkdir();
			HttpDataObj httpDataObj = new HttpDataObj(downloadDir.getAbsolutePath() + "/", "http://localhost/get");
			DataObject configAudioTmp = new AudioDataObject(encodingActivated, audioExtension); // url_embarque_server_get,

			
			PowerMockito.mockStatic(Mp3Encoder.class);
			final String destinationFullPath = downloadDir.getAbsolutePath() + "/" + "10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3";
			final String resourceName = "test.mp3";

			Answer<Integer> encodingOperation = encodingSuccess ? successfullEncoding(resourceName, destinationFullPath) : encodingFailure();
			
			PowerMockito.when(Mp3Encoder.launchMp3Exec(anyString())).thenAnswer(encodingOperation);
			
			//When
			String flux = aa.demanderFlux(buildXmlRequest(audioExtension), configAudioTmp, httpDataObj);
			
			//Then
			return flux;
		}  finally {
			teardown(rootFolder);
			
		}
	}

	private Answer<Integer> successfullEncoding(final String sourceName,
			final String destinationFullPath) {
		return new Answer<Integer>() {

			public Integer answer(InvocationOnMock invocation)
					throws Throwable {
				String testFileName = getClass()
								.getClassLoader()
								.getResource(sourceName).getFile();
				FileUtils.copyFile(new File(testFileName).getAbsolutePath(), destinationFullPath);
				return 0;
			}
		};
	}
	
	private Answer<Integer> encodingFailure() {
		return new Answer<Integer>() {
			
			public Integer answer(InvocationOnMock invocation)
					throws Throwable {
				return 1;
			}
		};
	}

	private void teardown(TemporaryFolder rootFolder) {
		rootFolder.delete();
	}

	private TemporaryFolder setup() throws IOException {
		TemporaryFolder rootFolder = new TemporaryFolder();
		rootFolder.create();
		return rootFolder;
	}

	private String buildXmlRequest(final String audioExtension)
			throws ObixTmlgExeption {
		TemplateEngine te = new MustacheTemplateEngine();
		@SuppressWarnings("serial")
		Map<String, String> vals = new HashMap<String, String>() {{
			put("SOURCEFORMAT", audioExtension);
		}};
		String message = te.compile("src/test/resources/ogive2pscsAudio_with_transformed_audio.xml", vals );
		return message;
	}

	private GererAnnonceAudio testableGererAnnonceAudio(final TemporaryFolder rootFolder, final boolean downloadError) throws IOException {
		final File lclsrvrFolder = rootFolder.newFolder();
		lclsrvrFolder.mkdir();

		GererAnnonceAudio aa = new GererAnnonceAudio() {
			@Override
			public byte[] downloadAudioFileFromHttpServer(String fileUrl)
					throws CoreException {
				if (downloadError)
					throw new CoreException("cant download");
				else {
					try {
						InputStream resource = getClass()
								.getClassLoader()
								.getResourceAsStream("10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");
						byte[] bts = new byte[1000000];
						resource.read(bts);
						return bts;
					} catch (Exception e) {
						throw new CoreException(e);
					}
				}
			}

			@Override
			protected String getLocalServerFolder() {

					return lclsrvrFolder.getAbsolutePath();
			}
		};
		return aa;
	}
}
