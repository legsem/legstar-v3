package org.legstar.cobol.bean.generator;

import java.io.IOException;

/**
 * Denotes a generation exception.
 */
public class CobolBeanGeneratorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Build an exception.
	 * 
	 * @param message the exception reason
	 */
	public CobolBeanGeneratorException(String message) {
		super(message);
	}

	/**
	 * Build an exception.
	 * 
	 * @param e the original cause
	 */
	public CobolBeanGeneratorException(IOException e) {
		super(e);
	}

}
