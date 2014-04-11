package flux;


public final class AudioAnnounceTmlg implements IAudioAnnounceTmlg {
	private String url;
	private String format;
	private String fileName;


	public AudioAnnounceTmlg(AudioAnnounceTmlg audioAnnounceTmlg) {
		super();
		this.url = audioAnnounceTmlg.getUrl();
		this.format = audioAnnounceTmlg.getFormat();
		this.fileName = audioAnnounceTmlg.getFileName();
	}

	public AudioAnnounceTmlg(String url, String format, String fileName) {
		super();
		this.url = url;
		this.format = format;
		this.fileName = fileName;
	}

	public void setUrl(String string) {
		url = string;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setFileName(String name) {
		fileName = name;
	}

	public String getUrl() {
		return url;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFormat() {
		return format;
	}

	@Override
	public String toString() {
		return "AnnounceAudioTmlg [url=" + url + ", format=" + format + ", fileName=" + fileName + "]";
	}
}