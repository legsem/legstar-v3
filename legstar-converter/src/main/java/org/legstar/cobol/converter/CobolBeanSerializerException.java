package org.legstar.cobol.converter;

/**
 * Exception raised when attempting to serialize a bean into a Cobol data
 * stream.
 */
public class CobolBeanSerializerException extends CobolBeanConverterExceptionBase {

	private static final long serialVersionUID = 1L;

	/**
	 * Wraps an original cause
	 * 
	 * @param context the serialization context
	 * @param cause   the cause
	 */
	public CobolBeanSerializerException(CobolBeanSerializerContext context, Throwable cause) {
		super(context, cause);
	}

	/**
	 * A new exception
	 * 
	 * @param context the serialization context
	 * @param message the error description
	 */
	public CobolBeanSerializerException(CobolBeanSerializerContext context, String message) {
		super(context, message);
	}

}
