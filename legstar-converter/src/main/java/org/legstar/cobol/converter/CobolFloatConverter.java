package org.legstar.cobol.converter;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.legstar.cobol.utils.BytesLenUtils;

/**
 * Converts between a Cobol COMP-1 literal and a java Float.
 */
public class CobolFloatConverter {

	/**
	 * Build a COBOL float converter
	 */
	public CobolFloatConverter() {
		super();
	}

	/**
	 * Convert a COBOL float (COMP-1).
	 * 
	 * @param <T>         the target java type
	 * @param is          the cobol input data
	 * @param targetClass the target java class
	 * @return the converted java value
	 */
	@SuppressWarnings("unchecked")
	public <T> T convert(CobolInputStream is, Class<T> targetClass) {
		if (targetClass.equals(Float.class)) {
			return (T) toFloat(is);
		} else {
			throw new CobolBeanConverterException("Unsupported target class " + targetClass);
		}
	}

	/**
	 * Converts a number of bytes from the input stream into a Float.
	 * <p>
	 * This is the bit layout for this type:
	 * 
	 * <pre>
	 * 33222222222211111111110000000000
	 * 10987654321098765432109876543210
	 * --------------------------------
	 * 01000011010011010010000000000000
	 * -                                 sign
	 *  -------                          excess
	 *         ------------------------  mantissa
	 * </pre>
	 * 
	 * @param is the host bytes
	 * @return a Float
	 */
	public Float toFloat(CobolInputStream is) {
		try {
			int bytesLen = BytesLenUtils.floatByteLen();
			byte[] buffer = new byte[bytesLen];
			int count = is.read(buffer);
			if (count < bytesLen) {
				throw new CobolBeanConverterEOFException();
			}
			ByteBuffer bb = ByteBuffer.wrap(buffer);
			int hostIntBits = bb.getInt();
			int sign = (hostIntBits & 0x80000000) >>> 31;

			/*
			 * Bits 30-24 (7 bits) represents the exponent offset by 64, this number is
			 * called excess so you get the exponent as E= excess - 64
			 */
			int excess = (hostIntBits & 0x7f000000) >>> 24;
			int exponent = excess == 0 ? 0 : (excess - 64);

			/* Bits 23-0 (24 bits) represents the getMantissa(). */
			int mantissa = hostIntBits & 0x00ffffff;

			/* Java Floats are in IEEE 754 floating-point bit layout. */
			/*
			 * Host exponent is hexadecimal based while java is binary based. There is also
			 * an additional shift for non-zero values due to the 1-plus" normalized java
			 * specs.
			 */
			if (mantissa != 0) {
				exponent = ((4 * exponent) - 1);
			}

			/* In java the 24th bit needs to be one */
			while ((mantissa > 0) && (mantissa & 0x00800000) == 0) {
				mantissa = mantissa << 1;
				exponent = exponent - 1;
			}

			/* First check if this is a zero value */
			float result = 0f;
			if (exponent != 0 || mantissa != 0) {
				/* Get rid of the leading 1 which needs to be implicit */
				int javaIntBits = mantissa & 0x007fffff;
				javaIntBits = javaIntBits | ((exponent + 127) << 23);
				javaIntBits = javaIntBits | (sign << 31);
				result = Float.intBitsToFloat(javaIntBits);
			}
			return result;
		} catch (IOException e) {
			throw new CobolBeanConverterException(e);
		}
	}

}
