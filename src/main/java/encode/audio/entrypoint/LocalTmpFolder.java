package encode.audio.entrypoint;

import java.util.HashSet;
import java.util.Set;

public class LocalTmpFolder {

	private final Set<String> audioFilesOnTmpDir;

	public LocalTmpFolder() {
		super();
		this.audioFilesOnTmpDir = new HashSet<String>();
	}
}
