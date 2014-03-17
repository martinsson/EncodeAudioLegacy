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
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import templating.TemplateEngine;
import templating.mustache.MustacheTemplateEngine;
import encode.audio.utils.CoreException;
import encode.audio.utils.Mp3Encoder;
import flux.ObixTmlgExeption;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Mp3Encoder.class)
public class GererAnnonceAudioTest {

	private static final String TEST_MP3_FILENAME = "test.mp3";
	private static final Object[] ALL_BOOLEAN_VALUES = { true, false };

	// @Test
	// public void runOnce() throws Exception {
	// assertEquals("kbaskhjfksadjf", allGererAnnonceAudio(true, "wav", 128,
	// "lame"));
	// }

	@Test
	public void downLoadError_transformationLock() throws Exception {
		Object[] encodingActivated = ALL_BOOLEAN_VALUES;

		Object[] conversionBinaryName = { "lame" };
		Object[] bitrate = { 128 };

		Object[] audioExtension = { "mp3", "wav" };
		Object[] encoding = ALL_BOOLEAN_VALUES;

		LegacyApprovals.LockDown(this,
				"downloadError_GererAnnonceAudioCatchExceptions",
				encodingActivated, audioExtension, bitrate,
				conversionBinaryName, encoding);
	}

	@Test
	public void transformationLock() throws Exception {
		Object[] encodingActivated = ALL_BOOLEAN_VALUES;

		Object[] conversionBinaryName = { "lame" };
		Object[] bitrate = { 128 };
		Object[] audioExtension = { "mp3", "wav" };
		Object[] encoding = ALL_BOOLEAN_VALUES;
		Object[] destAudioFileUC = { new UC_DestAudioFile_NotAlreadyEncoded(),
				new UC_DestAudioFile_AlreadyEncoded(TEST_MP3_FILENAME) };

		LegacyApprovals.LockDown(this,
				"default_GererAnnonceAudioCatchExceptions", encodingActivated,
				audioExtension, bitrate, conversionBinaryName, encoding,
				destAudioFileUC);
	}

	@Test(expected = AppTechnicalException.class)
	public void cannot_publish_target_audio_file_readfileissue()
			throws Exception {
		Boolean encodingActivated = Boolean.TRUE;
		String audioExtension = "mp3";
		Integer bitrate = new Integer(128);
		String conversionBinaryName = "lame";
		Boolean encodingSuccess = Boolean.TRUE;
		UC_DestAudioFile destAudioFile = new UC_DestAudioFile_AlreadyEncoded(
				TEST_MP3_FILENAME);

		TemporaryFolder rootFolder = setup();
		final File downloadDir = rootFolder.newFolder();
		GererAnnonceAudio aa = gererAnnonceAudio_WithNoLocalServerFolder();
		gererAnnonceAudio_simulate_readissue(encodingActivated, audioExtension, bitrate,
				conversionBinaryName, encodingSuccess, aa, downloadDir,
				destAudioFile);
	}

	@Test(expected = AppTechnicalException.class)
	public void cannot_publish_target_audio_file_writefileissue()
			throws Exception {
		Boolean encodingActivated = Boolean.TRUE;
		String audioExtension = "mp3";
		Integer bitrate = new Integer(128);
		String conversionBinaryName = "lame";
		Boolean encodingSuccess = Boolean.TRUE;
		UC_DestAudioFile destAudioFile = new UC_DestAudioFile_AlreadyEncoded(
				TEST_MP3_FILENAME);

		TemporaryFolder rootFolder = setup();
		final File downloadDir = rootFolder.newFolder();
		GererAnnonceAudio aa = gererAnnonceAudio_WithNoLocalServerFolder();
		allGererAnnonceAudio(encodingActivated,
				audioExtension, bitrate, conversionBinaryName, encodingSuccess,
				aa, downloadDir, destAudioFile);

	}

	private String gererAnnonceAudio_simulate_readissue(
			Boolean encodingActivated, String audioExtension, Integer bitrate,
			String conversionBinaryName, Boolean encodingSuccess,
			GererAnnonceAudio aa, File downloadDir,
			UC_DestAudioFile destAudioFile) throws Exception {

		downloadDir.mkdir();
		HttpDataObj httpDataObj = Mockito.mock(HttpDataObj.class);
		Mockito.when(httpDataObj.getString("audio_temp_path"))
				.thenReturn(downloadDir.getAbsolutePath() + "/").thenReturn("writeissue");
		Mockito.when(httpDataObj.getString("url_embarque_server_get"))
				.thenReturn("http://localhost/get");

		DataObject configAudioTmp = new AudioDataObject(encodingActivated,
				audioExtension); // url_embarque_server_get,

		final String destinationFullPath = downloadDir.getAbsolutePath() + "/"
				+ "10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3";
		final String resourceName = TEST_MP3_FILENAME;

		Answer<Integer> encodingOperation = encodingSuccess ? successfullEncoding(
				resourceName, destinationFullPath) : encodingFailure();

		PowerMockito.mockStatic(Mp3Encoder.class);
		PowerMockito.when(Mp3Encoder.launchMp3Exec(anyString())).thenAnswer(
				encodingOperation);

		destAudioFile.activateUC(destinationFullPath);

		// When
		String flux = aa.demanderFlux(buildXmlRequest(audioExtension),
				configAudioTmp, httpDataObj);

		// Then
		return flux;
	}

	private GererAnnonceAudio gererAnnonceAudio_WithNoLocalServerFolder()
			throws IOException {
		GererAnnonceAudio aa = new GererAnnonceAudio() {
			@Override
			public byte[] downloadAudioFileFromHttpServer(String fileUrl)
					throws CoreException {
				try {
					InputStream resource = getClass()
							.getClassLoader()
							.getResourceAsStream(
									"10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");
					byte[] bts = new byte[1000000];
					resource.read(bts);
					return bts;
				} catch (Exception e) {
					throw new CoreException(e);
				}
			}

			@Override
			protected String getLocalServerFolder() {

				return "/no/local/server/folder";
			}
		};
		return aa;
	}

	public String downloadError_GererAnnonceAudioCatchExceptions(
			Boolean encodingActivated, String audioExtension, Integer bitrate,
			String conversionBinaryName, Boolean encodingSuccess) {
		try {
			TemporaryFolder rootFolder = setup();
			try {
				GererAnnonceAudio aa;
				aa = gererAnnonceAudio_DownloadError();

				final File downloadDir = rootFolder.newFolder();
				return downloadError_GererAnnonceAudio(encodingActivated,
						audioExtension, bitrate, conversionBinaryName,
						encodingSuccess, aa, downloadDir);
			} finally {
				teardown(rootFolder);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getClass().getName();
		}
	}

	public String default_GererAnnonceAudioCatchExceptions(
			Boolean encodingActivated, String audioExtension, Integer bitrate,
			String conversionBinaryName, Boolean encodingSuccess,
			UC_DestAudioFile destAudioFile) {
		try {
			TemporaryFolder rootFolder = setup();
			try {
				GererAnnonceAudio aa;
				aa = gererAnnonceAudio_DefaultCase(rootFolder);

				final File downloadDir = rootFolder.newFolder();
				return allGererAnnonceAudio(encodingActivated, audioExtension,
						bitrate, conversionBinaryName, encodingSuccess, aa,
						downloadDir, destAudioFile);
			} finally {
				teardown(rootFolder);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getClass().getName();
		}
	}

	public String downloadError_GererAnnonceAudio(Boolean encodingActivated,
			String audioExtension, Integer bitrate,
			String conversionBinaryName, Boolean encodingSuccess,
			GererAnnonceAudio aa, File downloadDir) throws Exception {

		downloadDir.mkdir();
		HttpDataObj httpDataObj = new HttpDataObj(downloadDir.getAbsolutePath()
				+ "/", "http://localhost/get");
		DataObject configAudioTmp = new AudioDataObject(encodingActivated,
				audioExtension); // url_embarque_server_get,

		PowerMockito.mockStatic(Mp3Encoder.class);
		final String destinationFullPath = downloadDir.getAbsolutePath() + "/"
				+ "10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3";
		final String resourceName = TEST_MP3_FILENAME;

		Answer<Integer> encodingOperation = encodingSuccess ? successfullEncoding(
				resourceName, destinationFullPath) : encodingFailure();

		PowerMockito.when(Mp3Encoder.launchMp3Exec(anyString())).thenAnswer(
				encodingOperation);

		// When
		String flux = aa.demanderFlux(buildXmlRequest(audioExtension),
				configAudioTmp, httpDataObj);

		// Then
		return flux;
	}

	public String allGererAnnonceAudio(Boolean encodingActivated,
			String audioExtension, Integer bitrate,
			String conversionBinaryName, Boolean encodingSuccess,
			GererAnnonceAudio aa, File downloadDir,
			UC_DestAudioFile destAudioFile) throws Exception {

		downloadDir.mkdir();
		HttpDataObj httpDataObj = new HttpDataObj(downloadDir.getAbsolutePath()
				+ "/", "http://localhost/get");
		DataObject configAudioTmp = new AudioDataObject(encodingActivated,
				audioExtension); // url_embarque_server_get,

		final String destinationFullPath = downloadDir.getAbsolutePath() + "/"
				+ "10.151.156.180Mon_Nov_04_140724_CET_2013343.mp3";
		final String resourceName = TEST_MP3_FILENAME;

		Answer<Integer> encodingOperation = encodingSuccess ? successfullEncoding(
				resourceName, destinationFullPath) : encodingFailure();

		PowerMockito.mockStatic(Mp3Encoder.class);
		PowerMockito.when(Mp3Encoder.launchMp3Exec(anyString())).thenAnswer(
				encodingOperation);

		destAudioFile.activateUC(destinationFullPath);

		// When
		String flux = aa.demanderFlux(buildXmlRequest(audioExtension),
				configAudioTmp, httpDataObj);

		// Then
		return flux;
	}

	private Answer<Integer> successfullEncoding(final String sourceName,
			final String destinationFullPath) {
		return new Answer<Integer>() {

			public Integer answer(InvocationOnMock invocation) throws Throwable {
				String testFileName = getClass().getClassLoader()
						.getResource(sourceName).getFile();
				FileUtils.copyFile(new File(testFileName).getAbsolutePath(),
						destinationFullPath);
				return 0;
			}
		};
	}

	private Answer<Integer> encodingFailure() {
		return new Answer<Integer>() {

			public Integer answer(InvocationOnMock invocation) throws Throwable {
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
		Map<String, String> vals = new HashMap<String, String>() {
			{
				put("SOURCEFORMAT", audioExtension);
			}
		};
		String message = te
				.compile(
						"src/test/resources/ogive2pscsAudio_with_transformed_audio.xml",
						vals);
		return message;
	}

	private GererAnnonceAudio gererAnnonceAudio_DefaultCase(
			final TemporaryFolder rootFolder) throws IOException {
		final File lclsrvrFolder = rootFolder.newFolder();
		lclsrvrFolder.mkdir();

		GererAnnonceAudio aa = new GererAnnonceAudio() {
			@Override
			public byte[] downloadAudioFileFromHttpServer(String fileUrl)
					throws CoreException {
				try {
					InputStream resource = getClass()
							.getClassLoader()
							.getResourceAsStream(
									"10.151.156.180Mon_Nov_04_140724_CET_2013343.wav");
					byte[] bts = new byte[1000000];
					resource.read(bts);
					return bts;
				} catch (Exception e) {
					throw new CoreException(e);
				}
			}

			@Override
			protected String getLocalServerFolder() {

				return lclsrvrFolder.getAbsolutePath();
			}
		};
		return aa;
	}

	private GererAnnonceAudio gererAnnonceAudio_DownloadError()
			throws IOException {

		GererAnnonceAudio aa = new GererAnnonceAudio() {
			@Override
			public byte[] downloadAudioFileFromHttpServer(String fileUrl)
					throws CoreException {
				throw new CoreException("cant download");
			}
		};
		return aa;
	}
}
