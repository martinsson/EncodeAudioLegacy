package encode.audio.entrypoint;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils2 {

	public synchronized static void saveFile(String fileName, byte[] filecontent)
			throws FileNotFoundException, IOException {
		BufferedOutputStream filewriter;
		// fileWriter = new BufferedWriter(new
		// FileWriter(AUDIO_FILE_PATH+fileName, true));
		filewriter = new BufferedOutputStream(new FileOutputStream(fileName));
		filewriter.write(filecontent);
		filewriter.close();
	}

}
