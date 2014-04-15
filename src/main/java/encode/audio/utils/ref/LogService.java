package encode.audio.utils.ref;

public interface LogService {

	public static final int	LOG_ERROR	= 1;
	public static final int	LOG_WARNING	= 2;
	public static final int	LOG_INFO	= 3;
	public static final int	LOG_DEBUG	= 4;


	public void log(int level, String message);

	void log(int level, String message, Throwable exception);

}
