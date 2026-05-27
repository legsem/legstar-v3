package org.legstar.cobol.converter;

import java.io.UnsupportedEncodingException;

public class CobolPrimitiveConverterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CobolPrimitiveConverterException(String message) {
		super(message);
	}

	public CobolPrimitiveConverterException(UnsupportedEncodingException e) {
		super(e);
	}

}
