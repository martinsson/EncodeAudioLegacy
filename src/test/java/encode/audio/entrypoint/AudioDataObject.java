package encode.audio.entrypoint;

import java.util.HashMap;

import encode.audio.utils.Configurations;

public class AudioDataObject implements DataObject {

	private final HashMap<String, String> values;

	public AudioDataObject(String tagetAudioFileExtension) {
		this(tagetAudioFileExtension, "128", "4");
	}

	public AudioDataObject(String tagetAudioFileExtension, String encodingBitrate, String encodingVbrQuality) {
		this.values = new HashMap<String, String>();
		this.values.put(Configurations.ENCODING_BITRATE, encodingBitrate);
		this.values.put(Configurations.FINAL_AUDIO_FILE_EXTENSION, tagetAudioFileExtension);
		this.values.put(Configurations.PATH_PROGRAM_EXE, "/usr/local/bin/lame");
		this.values.put(Configurations.ENCODING_VBR_QUALITY, encodingVbrQuality);
	}

	public String getString(String key) {
		return values.get(key);
	}
}
