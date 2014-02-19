package encode.audio.entrypoint;

import java.util.HashMap;

import encode.audio.entrypoint.DataObject;
import encode.audio.utils.Configurations;


public class AudioDataObject implements DataObject {

	private boolean encodingActivated;
	private String audioExtension;


	public AudioDataObject(Boolean encodingActivated, String audioExtension) {
		this.audioExtension = audioExtension;
		this.encodingActivated = encodingActivated;
	}

	public String getString(String arg0) {
		HashMap<String, String> values = new HashMap<String, String>() {{
			// Configurations.ENCODING_BITRATE, Configurations.FINAL_AUDIO_FILE_EXTENSION, Configurations.PATH_PROGRAM_EXE
			// Configurations.ENCODING_ACTIVATE,
			put(Configurations.ENCODING_BITRATE,"128");
			put(Configurations.FINAL_AUDIO_FILE_EXTENSION, ".mp3");
			put(Configurations.PATH_PROGRAM_EXE, "/usr/local/bin/lame");
			put(Configurations.ENCODING_VBR_QUALITY, "4");
		}
			
		};
		return values.get(arg0);
	}


	public boolean getBoolean(String key) {
		if (key.equals(Configurations.ENCODING_ACTIVATE))
			return encodingActivated;
		else 
			throw new RuntimeException("didnt expect " + key);
	}
}
