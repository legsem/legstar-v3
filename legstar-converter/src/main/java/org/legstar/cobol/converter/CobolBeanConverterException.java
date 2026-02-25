package org.legstar.cobol.converter;

/**
 * Signals a conversion error.
 */
public class CobolBeanConverterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Full path to cobol item starting at the root
	 */
	private String cobolQualifiedName;

	/**
	 * location in the input data when the error was encountered
	 */
	private long bytesCounter = -1;

	/**
	 * Wraps an original cause
	 * 
	 * @param cause the cause
	 */
	public CobolBeanConverterException(Throwable cause) {
		super(cause);
	}

	/**
	 * A new exception
	 * 
	 * @param message the error description
	 */
	public CobolBeanConverterException(String message) {
		super(message);
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getMessage());
		if (cobolQualifiedName != null) {
			sb.append(" {");
			sb.append("Cobol item: '");
			sb.append(cobolQualifiedName);
			sb.append("'");
			if (bytesCounter == -1) {
				sb.append("}");
			}
		}
		if (bytesCounter > -1) {
			if (cobolQualifiedName == null) {
				sb.append(" {");
			} else {
				sb.append(", ");
			}
			sb.append("@offset: ");
			sb.append(bytesCounter);
			sb.append("}");
		}
		return sb.toString();
	}

	/**
	 * Full path to cobol item starting at the root
	 * 
	 * @return a cobol qualified name
	 */
	public String getCobolQualifiedName() {
		return cobolQualifiedName;
	}

	/**
	 * Set the cobol qualified name of the item being processed when the error
	 * occurred
	 * 
	 * @param cobolQualifiedName cobol qualified name
	 */
	public void setCobolQualifiedName(String cobolQualifiedName) {
		this.cobolQualifiedName = cobolQualifiedName;
	}

	/**
	 * Sets the location in the input data when the error was encountered
	 * 
	 * @param bytesCounter location in the input data when the error was encountered
	 */
	public void setBytesCounter(long bytesCounter) {
		this.bytesCounter = bytesCounter;
	}

}
