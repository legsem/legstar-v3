package org.legstar.cobol.jaxb.converter;

import org.legstar.cobol.converter.CobolBeanConverterException;

public class CobolXmlConverterException extends CobolBeanConverterException {

	private static final long serialVersionUID = 1L;

	public CobolXmlConverterException(String message) {
		super(message);
	}

	public CobolXmlConverterException(String message, Throwable cause) {
		super(message, cause);
	}

	public CobolXmlConverterException(Throwable cause) {
		super(cause);
	}

}
