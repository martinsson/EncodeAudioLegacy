package encode.audio.entrypoint;

import java.util.HashMap;

import encode.audio.utils.Configurations;
import encode.audio.utils.CoreUtil;

final class HttpDataObj implements DataObject {
	private final boolean downloadAudioFileSuccess;
	private final HashMap<String, String> values;

	public HttpDataObj(String audioTempPath, String urlEmbarqueGet) {
		this(true, audioTempPath, urlEmbarqueGet);
	}

	public HttpDataObj(Boolean downloadAudioFileSuccess, String audioTempPath, String urlEmbarqueGet) {
		this.downloadAudioFileSuccess = downloadAudioFileSuccess;
		this.values = new HashMap<String, String>();
		this.values.put(Configurations.URL_EMBARQUE_SERVER_GET, urlEmbarqueGet);
		this.values.put(Configurations.AUDIO_TEMP_PATH, audioTempPath);
	}

	public String getString(String key) {
		String value = values.get(key);
		if (key != null)
			return value;

		throw new RuntimeException("didnt expect " + key);
	}

	public boolean getBoolean(String key) {
		switch (key) {
		case CoreUtil.DOWNLOAD_AUDIO_FILE_SUCCESS:
			return downloadAudioFileSuccess;
		default:
			throw new RuntimeException("didnt expect " + key);
		}
	}

}