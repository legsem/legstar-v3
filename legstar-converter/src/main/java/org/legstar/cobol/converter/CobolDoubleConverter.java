package org.legstar.cobol.converter;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.legstar.cobol.io.CobolInputStream;
import org.legstar.cobol.utils.BytesLenUtils;

/**
 * Converts between a Cobol COMP-2 literal and a java Double.
 */
public class CobolDoubleConverter {

	private static final long SIGN_BIT_MASK = 0x8000000000000000L; // bit 63 is the sign
	private static final long EXP_IEEE_MASK = 0x7ff0000000000000L; // bits 62 to 52 is the encoded binary exponent
	private static final long MAN_IEEE_MASK = 0x000fffffffffffffL; // bits 51 to 0 is the encoded mantissa
	private static final long EXP_COMP_2_MASK = 0x7f00000000000000L; // bits 62 to 56 is the encoded hexadecimal
																		// exponent
	private static final long MAN_COMP_2_MASK = 0x00ffffffffffffffL; // bits 56 to 0 is the mantissa

	/**
	 * Build a COBOL double converter
	 */
	public CobolDoubleConverter() {
		super();
	}

	/**
	 * Convert a COBOL double (COMP-2).
	 * 
	 * @param <T>         the target java type
	 * @param is          the cobol input data
	 * @param targetClass the target java class
	 * @return the converted java value
	 */
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
	 * @return a Double
	 */
	public Double toDouble(CobolInputStream is) {
		try {
			int bytesLen = BytesLenUtils.doubleByteLen();
			byte[] buffer = new byte[bytesLen];
			int count = is.read(buffer);
			if (count < bytesLen) {
				throw new CobolBeanConverterEOFException();
			}
			ByteBuffer bb = ByteBuffer.wrap(buffer);
			long hostLongBits = bb.getLong();

			long sign = sign(hostLongBits);
			long exponent = comp_2Exponent(hostLongBits);
			long mantissa = comp_2Mantissa(hostLongBits);

			/* Java Doubles are in IEEE 754 floating-point bit layout. */
			/*
			 * Host exponent is hexadecimal based while java is binary based. There is also
			 * an additional shift for non-zero values due to the 1-plus" normalized java
			 * specs.
			 */
			if (mantissa != 0) {
				exponent = ((4 * exponent) - 1);
			}

			/*
			 * The java mantissa is 53 bits while the host is 56. This means there is a
			 * systematic loss of precision.
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
				javaLongBits = javaLongBits | ((exponent + 1023) << 52);
				javaLongBits = javaLongBits | (sign << 63);
				result = Double.longBitsToDouble(javaLongBits);
			}

			return result;
		} catch (IOException e) {
			throw new CobolBeanConverterException(e);
		}
	}

	/**
	 * Convert a java double to a COBOL long floating point (COMP-2)
	 * <p>
	 * https://en.wikipedia.org/wiki/Double-precision_floating-point_format
	 * <p>
	 * https://en.wikipedia.org/wiki/IBM_hexadecimal_floating-point
	 * <p>
	 * Java double is in IEEE 724 format where the exponent in a binary exponent.
	 * The layout is like this:
	 * <pre>
	 * 0100000001100000101000000000000000000000000000000000000000000000
	 * -                                                                 sign
	 *  -----------                                                      exponent
	 *             ----------------------------------------------------  mantissa
	 * </pre>
	 * 
	 * <p>
	 * Otherwise a difficulty is to convert the java binary exponent into a COMP-2
	 * hexadecimal component (An hexadecimal digit is 4 bits).
	 * <p>
	 * In the normal case, there is an implicit initial 1 bit for the java fraction.
	 * This needs to be accounted for for both the exponent and the mantissa.
	 * <p>
	 * In the subnormal case there is an implicit initial 0 bit. COMP-1 requires
	 * that the first hex digit is non zero so we have to remove leading zeroes.
	 * 
	 * @param value the java double
	 * @return a COBOL long floating point
	 */
	public byte[] toCobol(double value) {

		if (Double.isNaN(value) || Double.isInfinite(value)) {
			throw new CobolBeanConverterException("Unsupported double " + value);
		}
		if (value == 0.0d || value == -0.0d) {
			return new byte[8];
		}

		long bitsIeee = Double.doubleToLongBits(value);
		long sign = sign(bitsIeee);
		long expIeee = (bitsIeee & EXP_IEEE_MASK) >> 52;
		long manIeee = bitsIeee & MAN_IEEE_MASK;
		
		/* Remove IEEE bias from exponent  */
		long expComp_2 =  expIeee - 1022;
		long manComp_2 = manIeee;
		if (expIeee == 0 && manIeee > 0) {
			/* Subnormal case. Remove leading zeroes */
			long n = Long.numberOfLeadingZeros(manComp_2) - 11 - 1;
			expComp_2 = expComp_2 - n;
			manComp_2 = manComp_2 << (n + 1);
			/* Convert binary exponent to hexadecimal */
			long shift = Math.abs(expComp_2 % 4);
			expComp_2 = expComp_2 / 4;
			/* Shift mantissa to accomodate hex component */
			manComp_2 = manComp_2 >>> shift;
		} else {
			/* Normal case. Add implied leading 1 bit */
			manComp_2 = manComp_2 | 0x0010000000000000L;
			/* Convert binary exponent to hexadecimal */
			long rest = expComp_2 % 4;
			long shift = rest <= 0 ? Math.abs(rest) : 4 - rest;
			expComp_2 = (expComp_2 + shift) / 4;
			/* Shift mantissa to accomodate hex component */
			manComp_2 = manComp_2 >>> shift;
			/* Compensate for extra 3 bits introduced by adding implicit first bit */
		    manComp_2 = manComp_2 << 3;
		}
		/* Add COMP-2 bias to exponent  */
		expComp_2 = expComp_2 + 64;
		if (expComp_2 < 0 || expComp_2 > 127) {
			throw new CobolBeanConverterException(
					"Hexadecimal biased exponent " + expComp_2 + " outside supported range (0-127)");
		}

		long bitsComp_2 = sign << 63;
		bitsComp_2 |= ((expComp_2 << 56) & EXP_COMP_2_MASK);
		bitsComp_2 |= manComp_2 & MAN_COMP_2_MASK;
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(bitsComp_2);
		return buffer.array();
	}

	/**
	 * Sign is the leftmost bit in IEEE and COMP-2
	 * 
	 * @param bits the COMP-2 or IEEE 64 bits
	 * @return 0 if positive, 1 if negative
	 */
	public static long sign(long bits) {
		return (bits & SIGN_BIT_MASK) >>> 63;
	}

	/**
	 * For COMP-2, bits 62-56 (7 bits) represents the exponent offset by 64, this
	 * number is called excess so you get the exponent as E= excess - 64
	 * 
	 * @param bits the COMP-2 64 bits
	 * @return the hexadecimal exponent
	 */
	public static long comp_2Exponent(long bits) {
		long excess = (bits & EXP_COMP_2_MASK) >>> 56;
		return excess == 0 ? 0 : (excess - 64);
	}

	/**
	 * For COMP-2 the mantissa is the trailing 56 bits.
	 * 
	 * @param bits the COMP-2 64 bits
	 * @return the mantissa
	 */
	public static long comp_2Mantissa(long bits) {
		return bits & MAN_COMP_2_MASK;
	}

}
