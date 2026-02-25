package org.legstar.cobol.converter;

/**
 * Signals an end of file condition.
 */
public class CobolBeanConverterEOFException extends CobolBeanConverterException {

	private static final long serialVersionUID = 1L;

	/**
	 * No more data left to convert
	 */
	public CobolBeanConverterEOFException() {
		super("Not enough cobol input data available");
	}

}
