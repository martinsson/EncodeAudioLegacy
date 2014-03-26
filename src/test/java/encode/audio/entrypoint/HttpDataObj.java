package encode.audio.entrypoint;

final class HttpDataObj implements DataObject {
	private final String tmpdir;
	private String urlEmbarqueGet;

	HttpDataObj(String tmpdir, String urlEmbarqueGet) {
		this.tmpdir = tmpdir;
		this.urlEmbarqueGet = urlEmbarqueGet;
	}

	public String getString(String key) {
		if (key.equals("url_embarque_server_get"))
			return urlEmbarqueGet;
		else if (key.equals("audio_temp_path"))
			return tmpdir;
		else
			throw new RuntimeException("didnt configure " + key);
	}

	public boolean getBoolean(String key) {
		return false;
	}

}