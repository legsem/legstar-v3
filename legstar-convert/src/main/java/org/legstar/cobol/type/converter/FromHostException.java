package org.legstar.cobol.type.converter;

/**
 * TODO 
 * <ul>
 * <li>improve message with annotation content (cobolName, type)</li>
 * <li>enhance annotations to reflect location in original source</li>
 * <li>also report byte position in payload
 * </ul>
 */
public class FromHostException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FromHostException(Throwable cause) {
		super(cause);
	}

	public FromHostException(String message) {
		super(message);
	}

	public FromHostException(String message, Throwable cause) {
		super(message, cause);
	}

}
