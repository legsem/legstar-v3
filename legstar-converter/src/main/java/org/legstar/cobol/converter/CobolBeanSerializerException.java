package org.legstar.cobol.converter;

public class CobolBeanSerializerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Serializer context when exception occurred.
	 */
	private final CobolBeanSerializerContext context;

	/**
	 * Wraps an original cause
	 * 
	 * @param context the serialization context
	 * @param cause the cause
	 */
	public CobolBeanSerializerException(CobolBeanSerializerContext context, Throwable cause) {
		super(cause);
		this.context = context;
	}

	/**
	 * A new exception
	 * 
	 * @param context the serialization context
	 * @param message the error description
	 */
	public CobolBeanSerializerException(CobolBeanSerializerContext context, String message) {
		super(message);
		this.context = context;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getMessage());
		sb.append(" {");
		sb.append("Cobol item: '");
		sb.append(context.cobolQualifiedName());
		sb.append("'");
		if (context.bytesCounter() == -1) {
			sb.append("}");
		}
		if (context.bytesCounter() > -1) {
			sb.append(", ");
			sb.append("@offset: ");
			sb.append(context.bytesCounter());
			sb.append("}");
		}
		return sb.toString();
	}


}
