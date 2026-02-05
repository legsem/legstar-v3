package org.legstar.cobol.jaxb.converter;

import org.legstar.cobol.converter.CobolConverterException;

public class CobolJaxbConverterException extends CobolConverterException {

	private static final long serialVersionUID = 1L;

	public CobolJaxbConverterException(String message) {
		super(message);
	}

	public CobolJaxbConverterException(String message, Throwable cause) {
		super(message, cause);
	}

	public CobolJaxbConverterException(Throwable cause) {
		super(cause);
	}

}
