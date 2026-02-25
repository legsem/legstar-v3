package org.legstar.cobol.utils;

/**
 * Cobol byte length for primitive numeric types.
 */
public class BytesLenUtils {

	/**
	 * Utility class.
	 */
	private BytesLenUtils() {
	}

	/**
	 * A COMP binary number bytes occupation depends on the number of decimal
	 * digits:
	 * <ul>
	 * <li>4 or less: 2 bytes</li>
	 * <li>5 to 9: 4 bytes</li>
	 * <li>10 to 18: 8 bytes</li>
	 * </ul>
	 * 
	 * @param totalDigits total number of digits
	 * @return the cobol byte length
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
	 * 
	 * @param totalDigits total number of digits
	 * @return the cobol byte length
	 */
	public static int packedDecimalByteLen(int totalDigits) {
		return (int) Math.ceil((totalDigits + 1) / 2.0);
	}

	/**
	 * Zoned decimals have one byte per digit plus one more byte if the sign is
	 * separate.
	 * 
	 * @param totalDigits  total number of digits
	 * @param signSeparate whether the sign occupies an additional byte
	 * @return the cobol byte length
	 */
	public static int zonedDecimalByteLen(int totalDigits, boolean signSeparate) {
		return totalDigits + (signSeparate ? 1 : 0);
	}

	/**
	 * COMP-1 use 4 bytes
	 * 
	 * @return the cobol byte length
	 */
	public static int floatByteLen() {
		return 4;
	}

	/**
	 * COMP-2 use 8 bytes
	 * 
	 * @return the cobol byte length
	 */
	public static int doubleByteLen() {
		return 8;
	}

}
