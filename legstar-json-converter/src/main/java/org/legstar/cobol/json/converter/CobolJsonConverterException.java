package org.legstar.cobol.json.converter;

import org.legstar.cobol.converter.CobolBeanConverterException;

/**
 * Signals a conversion error.
 */
public class CobolJsonConverterException extends CobolBeanConverterException {

	private static final long serialVersionUID = 1L;

	/**
	 * A new exception
	 * 
	 * @param message the error description
	 */
	public CobolJsonConverterException(String message) {
		super(message);
	}

	/**
	 * Wraps an original cause
	 * 
	 * @param cause the cause
	 */
	public CobolJsonConverterException(Throwable cause) {
		super(cause);
	}

}
