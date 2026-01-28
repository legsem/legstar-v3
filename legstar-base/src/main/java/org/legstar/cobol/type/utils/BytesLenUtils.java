package org.legstar.cobol.type.utils;

public class BytesLenUtils {

	/**
	 * A binary number bytes occupation depends on the number of decimal digits:
	 * <ul>
	 * <li>4 or less: 2 bytes</li>
	 * <li>5 to 9: 4 bytes</li>
	 * <li>10 to 18: 8 bytes</li>
	 * </ul>
	 */
	public static int binaryNumberByteLen(int totalDigits) {
		if (totalDigits <= 4) {
			return 2;
		} else if (totalDigits <= 9) {
			return 4;
		} else {
			return 8;
		}
	}

	/**
	 * A packed decimal storage size is the number of bytes needed to store
	 * totalDigits + 1 nibbles.
	 */
	public static int packedDecimalByteLen(int totalDigits) {
		return (int) Math.ceil((totalDigits + 1) / 2.0);
	}

	/**
	 * Zoned decimals have one byte per digit plus one more byte if the sign is
	 * separate.
	 */
	public static int zonedDecimalByteLen(int totalDigits, boolean signSeparate) {
		return totalDigits + (signSeparate ? 1 : 0);
	}

	/**
	 * COMP-1 use 4 bytes
	 */
	public static int floatByteLen() {
		return 4;
	}

	/**
	 * COMP-2 use 8 bytes
	 */
	public static int doubleByteLen() {
		return 8;
	}

}
