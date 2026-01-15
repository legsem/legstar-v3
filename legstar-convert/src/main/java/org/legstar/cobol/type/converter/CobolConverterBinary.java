package org.legstar.cobol.type.converter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * TO DO:
 * <ul>
 * <li>Support unsigned binaries</li>
 * </ul>
 * Known restrictions:
 * <ul>
 * <li>No support for arithmetic extended Cobol compiler option</li>
 * </ul>
 */
public class CobolConverterBinary {

	private final CobolConverterConfig config;

	public CobolConverterBinary(CobolConverterConfig config) {
		this.config = config;
	}

	@SuppressWarnings("unchecked")
	public <T> T convert(InputStream is, boolean signed, int totalDigits, Class<T> targetClass) {
		if (targetClass.equals(Short.class)) {
			return (T) toShort(is, signed, totalDigits);
		} else if (targetClass.equals(Integer.class)) {
			return (T) toInteger(is, signed, totalDigits);
		} else if (targetClass.equals(Long.class)) {
			return (T) toLong(is, signed, totalDigits);
		} else {
			throw new FromHostException("Unsupported target class " + targetClass);
		}
	}

	public Short toShort(InputStream is, boolean signed, int totalDigits) {
		return toByteBuffer(is, totalDigits).getShort();
	}

	public Integer toInteger(InputStream is, boolean signed, int totalDigits) {
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

	public Long toLong(InputStream is, boolean signed, int totalDigits) {
		ByteBuffer bb = toByteBuffer(is, totalDigits);
		if (bb.capacity() < 4) {
			return Short.valueOf(bb.getShort()).longValue();
		} else if (bb.capacity() < 8) {
			return Integer.valueOf(bb.getInt()).longValue();
		} else {
			return bb.getLong();
		}
	}

	private ByteBuffer toByteBuffer(InputStream is, int totalDigits) {
		try {
			int byteLen = byteLen(totalDigits);
			byte[] buffer = new byte[byteLen];
			is.read(buffer);
			return ByteBuffer.wrap(buffer);		
		} catch (IOException e) {
			throw new FromHostException(e);
		}
	}
	
	/**
	 * Given a byte buffer with an underflow condition, expand the byte buffer as
	 * requested.
	 */
	private ByteBuffer expand(ByteBuffer bb, int additionalCapacity) {
		int initialCapacity = bb.capacity();
		ByteBuffer newbb = ByteBuffer.allocate(initialCapacity + additionalCapacity);
		newbb.put(initialCapacity, bb, 0, initialCapacity);
		return newbb;
	}

	/**
	 * A binary number bytes occupation depends on the number of decimal digits:
	 * <ul>
	 * <li>4 or less: 2 bytes</li>
	 * <li>5 to 9: 4 bytes</li>
	 * <li>10 to 18: 8 bytes</li>
	 * </ul>
	 */
	private int byteLen(int totalDigits) {
		if (totalDigits <= 4) {
			return 2;
		} else if (totalDigits <= 9) {
			return 4;
		} else {
			return 8;
		}
	}
 
}
