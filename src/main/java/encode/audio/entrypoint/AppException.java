package encode.audio.entrypoint;


public class AppException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 504990729234164547L;

	public AppException() {
		super();
	}

	public AppException(Exception e) {
		super(e);
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppException(String message) {
		super(message);
	}

	public AppException(Throwable cause) {
		super(cause);
	}
}
