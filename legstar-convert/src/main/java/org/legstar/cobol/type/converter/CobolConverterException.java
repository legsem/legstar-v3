package org.legstar.cobol.type.converter;

/**
 * TODO 
 * <ul>
 * <li>improve message with annotation content (cobolName, type)</li>
 * <li>enhance annotations to reflect location in original source</li>
 * <li>also report byte position in payload
 * </ul>
 */
public class CobolConverterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CobolConverterException(Throwable cause) {
		super(cause);
	}

	public CobolConverterException(String message) {
		super(message);
	}

	public CobolConverterException(String message, Throwable cause) {
		super(message, cause);
	}

}
