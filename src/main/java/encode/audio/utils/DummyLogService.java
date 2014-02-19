package encode.audio.utils;

public class DummyLogService implements LogService {

	public void log(int level, String message) {
		System.out.println(message);

	}

	public void log(int level, String message, Throwable exception) {
		System.out.println(message);
		exception.printStackTrace();
	}

}