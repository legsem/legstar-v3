package org.legstar.cobol.converter;

/**
 * Common converter exception shared by serializer and de-serializer exceptions.
 */
public abstract class CobolBeanConverterExceptionBase extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Context when exception occurred.
	 */
	private final CobolBeanConverterContextBase context;

	/**
	 * Wraps an original cause
	 * 
	 * @param context the serialization context
	 * @param cause the cause
	 */
	public CobolBeanConverterExceptionBase(CobolBeanConverterContextBase context, Throwable cause) {
		super(cause);
		this.context = context;
	}

	/**
	 * A new exception
	 * 
	 * @param context the serialization context
	 * @param message the error description
	 */
	public CobolBeanConverterExceptionBase(CobolBeanConverterContextBase context, String message) {
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
