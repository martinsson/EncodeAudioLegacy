package encode.audio.entrypoint;



public class AppTechnicalException extends AppException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8832027447038756365L;

	public AppTechnicalException() {
		super();
		
	}

	public AppTechnicalException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public AppTechnicalException(String message) {
		super(message);
		
	}

	public AppTechnicalException(Throwable cause) {
		super(cause);
		
	}

	public AppTechnicalException(Exception e) {
		super(e);
	}
}
