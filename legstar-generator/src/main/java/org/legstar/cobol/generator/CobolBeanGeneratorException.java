package org.legstar.cobol.generator;

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

}
