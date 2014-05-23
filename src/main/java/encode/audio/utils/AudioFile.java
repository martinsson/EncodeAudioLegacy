package encode.audio.utils;

public class AudioFile {

	private String name;
	private String format;
	private byte[] binary;

	public AudioFile(String fileName, String format) {
		this.name = fileName;
		this.format = format;
	}

	public String getName() {
		return name;
	}

	public String getFormat() {
		return format;
	}

	public void setBinary(byte[] audioFileContent) {
	    binary = audioFileContent;
	}

	public byte[] getBinary() {
	    return binary;
	}
}
