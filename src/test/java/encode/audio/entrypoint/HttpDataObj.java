package encode.audio.entrypoint;

import java.util.HashMap;

import encode.audio.utils.Configurations;

final class HttpDataObj implements DataObject {
	private final HashMap<String, String> values;

	public HttpDataObj(String audioTempPath, String urlEmbarqueGet) {
		this.values = new HashMap<String, String>();
		this.values.put(Configurations.URL_EMBARQUE_SERVER_GET, urlEmbarqueGet);
		this.values.put(Configurations.AUDIO_TEMP_PATH, audioTempPath);
	}

	public String getString(String key) {
		return values.get(key);
	}
}