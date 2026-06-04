package org.legstar.cobol.converter;

/**
 * Exception raised when attempting to de-serialize Cobol data as a java bean.
 */
public class CobolBeanDeserializerException extends CobolBeanConverterExceptionBase {

	private static final long serialVersionUID = 1L;

	/**
	 * Wraps an original cause
	 * 
	 * @param context the de-serialization context
	 * @param cause   the cause
	 */
	public CobolBeanDeserializerException(CobolBeanDeserializerContext context, Throwable cause) {
		super(context, cause);
	}

	/**
	 * A new exception
	 * 
	 * @param context the de-serialization context
	 * @param message the error description
	 */
	public CobolBeanDeserializerException(CobolBeanDeserializerContext context, String message) {
		super(context, message);
	}
}
