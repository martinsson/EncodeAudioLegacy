package encode.audio.entrypoint;

import java.util.HashMap;

import encode.audio.entrypoint.DataObject;
import encode.audio.utils.Configurations;
import encode.audio.utils.CoreUtil;

public class AudioDataObject implements DataObject {

	private final boolean encodingActivated;
	private final boolean mp3encoderSuccess;
	private final HashMap<String, String> values;

	public AudioDataObject(String tagetAudioFileExtension) {
		this(true, true, tagetAudioFileExtension);
	}

	public AudioDataObject(Boolean encodingActivated, Boolean mp3encoderSuccess, String tagetAudioFileExtension) {
		this(encodingActivated, mp3encoderSuccess, tagetAudioFileExtension, "128", "4");
	}

	public AudioDataObject(String tagetAudioFileExtension, String encodingBitrate, String encodingVbrQuality) {
		this(true, true, tagetAudioFileExtension, encodingBitrate, encodingVbrQuality);
	}

	public AudioDataObject(Boolean encodingActivated, Boolean mp3encoderSuccess, String tagetAudioFileExtension, String encodingBitrate, String encodingVbrQuality) {
		this.encodingActivated = encodingActivated;
		this.mp3encoderSuccess = mp3encoderSuccess;
		this.values = new HashMap<String, String>();
		this.values.put(Configurations.ENCODING_BITRATE, encodingBitrate);
		this.values.put(Configurations.FINAL_AUDIO_FILE_EXTENSION, tagetAudioFileExtension);
		this.values.put(Configurations.PATH_PROGRAM_EXE, "/usr/local/bin/lame");
		this.values.put(Configurations.ENCODING_VBR_QUALITY, encodingVbrQuality);
	}

	public String getString(String key) {
		String value = values.get(key);
		if (key != null)
			return value;

		throw new RuntimeException("didnt expect " + key);
	}

	public boolean getBoolean(String key) {
		switch (key) {
		case Configurations.ENCODING_ACTIVATE:
			return encodingActivated;
		case CoreUtil.MP3ENCODER_SUCESS:
			return mp3encoderSuccess;
		default:
			throw new RuntimeException("didnt expect " + key);
		}
	}
}
