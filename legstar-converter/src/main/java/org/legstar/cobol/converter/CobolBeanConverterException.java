package org.legstar.cobol.converter;

public class CobolBeanConverterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String cobolQualifiedName;
	
	private long bytesCounter = -1;

	public CobolBeanConverterException(Throwable cause) {
		super(cause);
	}

	public CobolBeanConverterException(String message) {
		super(message);
	}

	public CobolBeanConverterException(String message, Throwable cause) {
		super(message, cause);
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
