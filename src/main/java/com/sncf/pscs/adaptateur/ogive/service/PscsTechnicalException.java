package com.sncf.pscs.adaptateur.ogive.service;



public class PscsTechnicalException extends PscsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8832027447038756365L;

	public PscsTechnicalException() {
		super();
		
	}

	public PscsTechnicalException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public PscsTechnicalException(String message) {
		super(message);
		
	}

	public PscsTechnicalException(Throwable cause) {
		super(cause);
		
	}

	public PscsTechnicalException(Exception e) {
		super(e);
	}
}
