package encode.audio.entrypoint;

import java.util.HashSet;
import java.util.Set;

public class LocalHTTPSServer {

	private final Set<String> audioFilesOnServer;

	public LocalHTTPSServer() {
		super();
		this.audioFilesOnServer = new HashSet<String>();
	}

	public void addFile(String fileName) {
		audioFilesOnServer.add(fileName);
	}

	public void uploadAudioAnnounce(String audioAnnounceFileName) {
		addFile(audioAnnounceFileName);
	}

}
