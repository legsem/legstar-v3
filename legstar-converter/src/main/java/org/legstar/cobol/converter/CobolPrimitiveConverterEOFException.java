package org.legstar.cobol.converter;

/**
 * Signals an end of file condition.
 * <p>
 * This happens when trying to convert a primitive type and may or may not be
 * acceptable depending on the context.
 */
public class CobolPrimitiveConverterEOFException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * No more data left to convert
	 */
	public CobolPrimitiveConverterEOFException() {
		super("Not enough cobol input data available");
	}

}
