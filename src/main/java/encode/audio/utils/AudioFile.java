package encode.audio.utils;

public class AudioFile {

	private String name;
	private String format;
	private byte[] binary;

	public AudioFile() {
		this.name = "";
		this.format = "";
		this.binary = null;
	}

	public AudioFile(String fileName, String format) {
		this.name = fileName;
		this.format = format;
		this.binary = null;
	}

	public AudioFile(String fileName, String format, byte[] binary) {
		this.name = fileName;
		this.format = format;
		this.binary = binary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public byte[] getBinary() {
		return binary;
	}

	public void setBinary(byte[] binary) {
		this.binary = binary;
	}
}
