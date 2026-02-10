package org.legstar.cobol.converter;

public class CobolBeanConverterEOFException extends CobolBeanConverterException {

	private static final long serialVersionUID = 1L;

	public CobolBeanConverterEOFException() {
		super("Not enough cobol input data available");
	}

}
