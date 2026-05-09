package org.legstar.cobol.converter;

/**
 * A conversion error happened when trying to convert a primitive type.
 */
public class CobolPrimitiveConverterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Wraps an original cause
	 * 
	 * @param cause the cause
	 */
	public CobolPrimitiveConverterException(Throwable cause) {
		super(cause);
	}

	/**
	 * A new exception
	 * 
	 * @param message the error description
	 */
	public CobolPrimitiveConverterException(String message) {
		super(message);
	}

}
