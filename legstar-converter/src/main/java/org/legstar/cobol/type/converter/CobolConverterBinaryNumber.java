package org.legstar.cobol.type.converter;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.legstar.cobol.type.utils.BytesLenUtils;

/**
 * Converts between a Cobol binary number and a java Short, Integer or Long.
 * <p>
 * Known restrictions:
 * <ul>
 * <li>Host binary data is assumed to have same endianness as java (i.e.
 * BigEndian)</li>
 * <li>No support for arithmetic extended Cobol compiler option (largest numeric
 * supported is 18 digits)</li>
 * <li>No support for unsigned values greater than 7fffffffffffffff. This would require a
 * toBigInteger method.</li>
 * </ul>
 */
public class CobolConverterBinaryNumber {

	public CobolConverterBinaryNumber(CobolConverterConfig config) {

	}

	@SuppressWarnings("unchecked")
	public <T> T convert(CobolConverterInputStream is, boolean signed, int totalDigits, Class<T> targetClass) {
		if (targetClass.equals(Short.class) || targetClass.equals(short.class)) {
			return (T) toShort(is, signed, totalDigits);
		} else if (targetClass.equals(Integer.class) || targetClass.equals(int.class)) {
			return (T) toInteger(is, signed, totalDigits);
		} else if (targetClass.equals(Long.class) || targetClass.equals(long.class)) {
			return (T) toLong(is, signed, totalDigits);
		} else {
			throw new CobolConverterException("Unsupported target class " + targetClass);
		}
	}

	/**
	 * If host data is a signed short then it translates to a java short directly.
	 * <p>
	 * If host data is an unsigned short and the value is greater than 0x7fff then
	 * this translates into a negative java short. This situation may only occur if
	 * this is a native binary item (COMP-5). Use toInteger in such a case.
	 * 
	 * @param is          the host bytes
	 * @param signed      is this a signed binary (unsigned otherwise)
	 * @param totalDigits the total number of digits
	 * @return the host bytes converted to a short
	 */
	public Short toShort(CobolConverterInputStream is, boolean signed, int totalDigits) {
		return toByteBuffer(is, totalDigits).getShort();
	}

	/**
	 * If host data is a signed integer, then it translates to a java integer
	 * directly.
	 * <p>
	 * If the host data is unsigned short, then the sign must be preserved when
	 * converting to integer since java does not have an unsigned short.
	 * <p>
	 * If host data is an unsigned integer and the value is greater than 0x7fffffff
	 * then this translates into a negative java integer. This situation may only
	 * occur if this is a native binary item (COMP-5). Use toLong in such a case.
	 * 
	 * @param is          the host bytes
	 * @param signed      is this a signed binary (unsigned otherwise)
	 * @param totalDigits the total number of digits
	 * @return the host bytes converted to an integer
	 */
	public Integer toInteger(CobolConverterInputStream is, boolean signed, int totalDigits) {
		ByteBuffer bb = toByteBuffer(is, totalDigits);
		if (bb.capacity() < 4) {
			if (signed) {
				return Short.valueOf(bb.getShort()).intValue();
			} else {
				return expand(bb, 4 - bb.capacity()).getInt();
			}
		} else {
			return bb.getInt();
		}
	}

	/**
	 * If host data is a signed long, then it translates to a java long directly.
	 * <p>
	 * If the host data is unsigned short or integer then the sign must be preserved
	 * when converting to integer since java does not have an unsigned short or
	 * unsigned integer.
	 * <p>
	 * If host data is an unsigned integer and the value is greater than
	 * 0x7fffffffffffffff then this translates into a negative java integer. This
	 * situation may only occur if this is a native binary item (COMP-5).
	 * 
	 * @param is          the host bytes
	 * @param signed      is this a signed binary (unsigned otherwise)
	 * @param totalDigits the total number of digits
	 * @return the host bytes converted to an integer
	 */
	public Long toLong(CobolConverterInputStream is, boolean signed, int totalDigits) {
		ByteBuffer bb = toByteBuffer(is, totalDigits);
		if (bb.capacity() < 4) {
			if (signed) {
				return Short.valueOf(bb.getShort()).longValue();
			} else {
				return expand(bb, 8 - bb.capacity()).getLong();
			}
		} else if (bb.capacity() < 8) {
			if (signed) {
				return Integer.valueOf(bb.getInt()).longValue();
			} else {
				return expand(bb, 8 - bb.capacity()).getLong();
			}
		} else {
			return bb.getLong();
		}
	}

	/**
	 * Read the requested number of digits from the input stream.
	 */
	private ByteBuffer toByteBuffer(CobolConverterInputStream is, int totalDigits) {
		try {
			int byteLen = BytesLenUtils.binaryNumberByteLen(totalDigits);
			byte[] buffer = new byte[byteLen];
			is.read(buffer);
			return ByteBuffer.wrap(buffer);
		} catch (IOException e) {
			throw new CobolConverterException(e);
		}
	}

	/**
	 * Given a byte buffer with an underflow condition, expand the byte buffer as
	 * requested.
	 */
	private ByteBuffer expand(ByteBuffer bb, int additionalCapacity) {
		int initialCapacity = bb.capacity();
		ByteBuffer newbb = ByteBuffer.allocate(initialCapacity + additionalCapacity);
		newbb.put(additionalCapacity, bb, 0, initialCapacity);
		return newbb;
	}

}
