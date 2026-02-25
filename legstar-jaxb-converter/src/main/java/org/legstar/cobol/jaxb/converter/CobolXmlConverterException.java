package org.legstar.cobol.jaxb.converter;

import org.legstar.cobol.converter.CobolBeanConverterException;

/**
 * Signals a conversion error.
 */
public class CobolXmlConverterException extends CobolBeanConverterException {

	private static final long serialVersionUID = 1L;

	/**
	 * A new exception
	 * 
	 * @param message the error description
	 */
	public CobolXmlConverterException(String message) {
		super(message);
	}

	/**
	 * Wraps an original cause
	 * 
	 * @param cause the cause
	 */
	public CobolXmlConverterException(Throwable cause) {
		super(cause);
	}

}
