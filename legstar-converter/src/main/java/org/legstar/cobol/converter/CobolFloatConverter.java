package org.legstar.cobol.converter;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.legstar.cobol.io.CobolInputStream;
import org.legstar.cobol.utils.BytesLenUtils;

/**
 * Converts between a Cobol COMP-1 literal and a java Float.
 */
public class CobolFloatConverter {

	private static final int SIGN_BIT_MASK = 0x80000000; // bit 31 is the sign
	private static final int EXP_IEEE_MASK = 0x7f800000; // bits 30 to 23 is the encoded binary exponent
	private static final int MAN_IEEE_MASK = 0x007fffff; // bits 22 to 0 is the encoded mantissa
	private static final int EXP_COMP_1_MASK = 0x7f000000; // bits 30 to 24 is the encoded hexadecimal exponent
	private static final int MAN_COMP_1_MASK = 0x00ffffff; // bits 23 to 0 is the mantissa

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

			int sign = sign(hostIntBits);
			int exponent = comp_1Exponent(hostIntBits);
			int mantissa = comp_1Mantissa(hostIntBits);

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

	/**
	 * Convert a java float to a COBOL floating point (COMP-1)
	 * <p>
	 * https://en.wikipedia.org/wiki/Double-precision_floating-point_format
	 * <p>
	 * https://en.wikipedia.org/wiki/IBM_hexadecimal_floating-point
	 * <p>
	 * Java float is in IEEE 724 format where the exponent in a binary exponent. The
	 * layout is like this:
	 * 
	 * <pre>
	 * 00111110011000001010000000000000
	 * -                                 sign
	 *  --------                         exponent
	 *          -----------------------  mantissa
	 * </pre>
	 * 
	 * <p>
	 * Otherwise a difficulty is to convert the java binary exponent into a COMP-1
	 * hexadecimal component (An hexadecimal digit is 4 bits).
	 * <p>
	 * In the normal case, there is an implicit initial 1 bit for the java fraction.
	 * This needs to be accounted for for both the exponent and the mantissa.
	 * <p>
	 * In the subnormal case there is an implicit initial 0 bit. COMP-1 requires
	 * that the first hex digit is non zero so we have to remove leading zeroes.
	 * 
	 * @param value the java float
	 * @return a COBOL floating point
	 */
	public byte[] toCobol(float value) {

		if (Float.isNaN(value) || Float.isInfinite(value)) {
			throw new CobolBeanConverterException("Unsupported float " + value);
		}
		if (value == 0.0f || value == -0.0f) {
			return new byte[4];
		}

		int bitsIeee = Float.floatToIntBits(value);
		int sign = sign(bitsIeee);
		int expIeee = (bitsIeee & EXP_IEEE_MASK) >> 23;
		int manIeee = bitsIeee & MAN_IEEE_MASK;
		
		/* Remove IEEE bias from exponent  */
		int expComp_1 =  expIeee - 126;
		int manComp_1 = manIeee;
		if (expIeee == 0 && manIeee > 0) {
			/* Subnormal case. Remove leading zeroes */
			int n = Integer.numberOfLeadingZeros(manComp_1) - 8 - 1;
			expComp_1 = expComp_1 - n;
			manComp_1 = manComp_1 << (n + 1);
			/* Convert binary exponent to hexadecimal */
			int shift = Math.abs(expComp_1 % 4);
			expComp_1 = expComp_1 / 4;
			/* Shift mantissa to accomodate hex component */
			manComp_1 = manComp_1 >>> shift;
		} else {
			/* Normal case. Add implied leading 1 bit */
			manComp_1 = manComp_1 | 0x00800000;
			/* Convert binary exponent to hexadecimal */
			int rest = expComp_1 % 4;
			int shift = rest <= 0 ? Math.abs(rest) : 4 - rest;
			expComp_1 = (expComp_1 + shift) / 4;
			/* Shift mantissa to accomodate hex component */
			manComp_1 = manComp_1 >>> shift;
		}
		/* Add COMP-1 bias to exponent  */
		expComp_1 = expComp_1 + 64;

		int bitsComp_1 = sign << 31;
		bitsComp_1 |= ((expComp_1 << 24) & EXP_COMP_1_MASK);
		bitsComp_1 |= manComp_1 & MAN_COMP_1_MASK;
		ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
		buffer.putInt(bitsComp_1);
		return buffer.array();
	}

	/**
	 * Sign is the leftmost bit in IEEE and COMP-1
	 * 
	 * @param bits the COMP-1 or IEEE 32 bits
	 * @return 0 if positive, 1 if negative
	 */
	public static int sign(int bits) {
		return (bits & SIGN_BIT_MASK) >>> 31;
	}

	/**
	 * For COMP-1, bits 30-24 (7 bits) represents the exponent offset by 64, this
	 * number is called excess so you get the exponent as E= excess - 64
	 * 
	 * @param bits the COMP-1 32 bits
	 * @return the hexadecimal exponent
	 */
	public static int comp_1Exponent(int bits) {
		int excess = (bits & EXP_COMP_1_MASK) >>> 56;
		return excess == 0 ? 0 : (excess - 64);
	}

	/**
	 * For COMP-1 the mantissa is the trailing 24 bits.
	 * 
	 * @param bits the COMP-1 32 bits
	 * @return the mantissa
	 */
	public static int comp_1Mantissa(int bits) {
		return bits & MAN_COMP_1_MASK;
	}


}
