package com.sncf.pscs.services.utils;

/**
 * @author PAG
 *
 */
public class CoreException extends Exception{

	private static final long serialVersionUID = 563728075825478349L;

	/**
	 * @param message
	 */
	public CoreException (final String message) {
		super(message);
	}

	/**
	 * @param exeption
	 */
	public CoreException (final Exception exeption) {
		super(exeption);
	}

	/**
	 * @param message
	 * @param exeption
	 */
	public CoreException (final String message, final Exception exeption) {
		super(message, exeption);
	}
	
}
