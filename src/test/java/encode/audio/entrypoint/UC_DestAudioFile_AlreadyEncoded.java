package encode.audio.entrypoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class UC_DestAudioFile_AlreadyEncoded implements UC_DestAudioFile {

	private String resourceName;

	public UC_DestAudioFile_AlreadyEncoded(String resourceName) {
		this.resourceName = resourceName;
	}

	public void activateUC(String destinationFullPath) {
		String testFileName = getClass().getClassLoader()
				.getResource(resourceName).getFile();
		try {
			FileUtils.copyFile(new File(testFileName).getAbsolutePath(),
					destinationFullPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return " UC DestAudioFile AlreadyEncoded";
	}

}
