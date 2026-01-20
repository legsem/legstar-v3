package org.legstar.cobol.type.converter;

/**
 * TODO
 * <ul>
 * <li>improve message with annotation content (cobolName, type)</li>
 * <li>enhance annotations to reflect location in original source</li>
 * </ul>
 */
public class CobolConverterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String cobolQualifiedName;
	
	private long bytesCounter = -1;

	public CobolConverterException(Throwable cause) {
		super(cause);
	}

	public CobolConverterException(String message) {
		super(message);
	}

	public CobolConverterException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String getMessage() {
		String msg = super.getMessage();
		if (cobolQualifiedName != null) {
			msg += "\nCobol item: " + cobolQualifiedName + ". ";
		}
		if (bytesCounter > -1) {
			msg += "\nInput bytes location: " + bytesCounter + ". ";
		}
		return msg;
	}

	public String getCobolQualifiedName() {
		return cobolQualifiedName;
	}

	public void setCobolQualifiedName(String cobolQualifiedName) {
		this.cobolQualifiedName = cobolQualifiedName;
	}

	public void setBytesCounter(long bytesCounter) {
		this.bytesCounter = bytesCounter;
	}

}
