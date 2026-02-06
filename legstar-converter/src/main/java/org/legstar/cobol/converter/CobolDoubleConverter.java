package org.legstar.cobol.converter;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.legstar.cobol.utils.BytesLenUtils;

/**
 * Converts between a Cobol COMP-2 literal and a java Double.
 */
public class CobolDoubleConverter {

	@SuppressWarnings("unchecked")
	public <T> T convert(CobolInputStream is, Class<T> targetClass) {
		if (targetClass.equals(Double.class)) {
			return (T) toDouble(is);
		} else {
			throw new CobolBeanConverterException("Unsupported target class " + targetClass);
		}
	}

	/**
	 * Converts a number of bytes from the input stream into a Double.
	 * <p>
	 * This is the bit layout for this type:
	 * 
	 * <pre>
	 *	6666555555555544444444443333333333222222222211111111110000000000
	 *	3210987654321098765432109876543210987654321098765432109876543210
	 *	----------------------------------------------------------------
	 *	0100010101010100001110101110100100010101101101010111001111101000
	 *	-                                                                sign
	 *	 -------                                                         excess
	 *	        -------------------------------------------------------  mantissa
	 * </pre>
	 * 
	 * @param is the host bytes
	 */
	public Double toDouble(CobolInputStream is) {
		try {
			int bytesLen = BytesLenUtils.doubleByteLen();
			byte[] buffer = new byte[bytesLen];
			int r = is.read(buffer);
			if (r != bytesLen) {
				throw new CobolBeanConverterException(
						"Not enough bytes for a double. Needed " + bytesLen + ", got " + r + " instead");
			}
			ByteBuffer bb = ByteBuffer.wrap(buffer);
	        long hostLongBits = bb.getLong();

	        /* First bit left (bit 63) is the sign: 0 = positive, 1 = negative */
	        int sign = (int) ((hostLongBits & 0x8000000000000000L) >>> 63);

	        /*
	         * Bits 62-56 (7 bits) represents the exponent offset by 64, this
	         * number is called excess so you get the exponent as
	         * E= excess - 64
	         */
	        int excess = (int) ((hostLongBits & 0x7f00000000000000L) >>> 56);
	        int exponent = excess == 0 ? 0 : (excess - 64);

	        /* Bits 55-0 (56 bits) represents the mantissa. */
	        long mantissa = hostLongBits & 0x00ffffffffffffffL;

	        /* Java Doubles are in IEEE 754 floating-point bit layout. */
	        /*
	         * Host exponent is hexadecimal based while java is binary based.
	         * There is also an additional shift for non-zero values due to
	         * the 1-plus" normalized java specs.
	         */
	        if (mantissa != 0) {
	            exponent = ((4 * exponent) - 1);
	        }

	        /*
	         * The java mantissa is 53 bits while the host is 56. This
	         * means there is a systematic loss of precision.
	         */
	        mantissa = mantissa >>> 3;
	        
	        /* In java the 53th bit needs to be one */
	        while (mantissa > 0L && (mantissa & 0x0010000000000000L) == 0) {
	            mantissa = mantissa << 1;
	            exponent = exponent - 1;
	        }

	        /* First check if this is a zero value */
	        double result = 0d;
	        if (exponent != 0 || mantissa != 0) {
	            /* Get rid of the leading 1 which needs to be implicit */
	            long javaLongBits = mantissa & 0x000fffffffffffffL;
	            javaLongBits = javaLongBits | ((long) (exponent + 1023) << 52);
	            javaLongBits = javaLongBits | ((long) sign << 63);
	            result = Double.longBitsToDouble(javaLongBits);
	        }

			return result;
		} catch (IOException e) {
			throw new CobolBeanConverterException(e);
		}
	}

}
