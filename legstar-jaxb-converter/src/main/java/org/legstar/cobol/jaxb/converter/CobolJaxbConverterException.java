package org.legstar.cobol.jaxb.converter;

/**
 * Signals a Cobol to XML error.
 */
public class CobolJaxbConverterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * A new exception
	 * 
	 * @param message the error description
	 */
	public CobolJaxbConverterException(String message) {
		super(message);
	}

	/**
	 * Wraps an original cause
	 * 
	 * @param cause the cause
	 */
	public CobolJaxbConverterException(Throwable cause) {
		super(cause);
	}

}
