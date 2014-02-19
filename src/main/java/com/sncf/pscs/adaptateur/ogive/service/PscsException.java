package com.sncf.pscs.adaptateur.ogive.service;


public class PscsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 504990729234164547L;

	public PscsException() {
		super();
	}

	public PscsException(Exception e) {
		super(e);
	}

	public PscsException(String message, Throwable cause) {
		super(message, cause);
	}

	public PscsException(String message) {
		super(message);
	}

	public PscsException(Throwable cause) {
		super(cause);
	}
}
