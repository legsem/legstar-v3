package org.legstar.cobol.generator.model;

import org.legstar.cobol.utils.BytesLenUtils;

/**
 * COBOL binary numbers (COMP, COMP-5)
 * 
 * @param cobolName   cobol name
 * @param signed      signed or unsigned
 * @param totalDigits total number of digits (include any fraction digit)
 * @param odoObject   true if this gives the dimension of a variable size array
 *                    (Occurs Depending On)
 * @param array       If this is an array, null otherwise
 * @param fieldName   java name
 * 
 */
public record RenderingBinaryNumber(String cobolName, boolean signed, int totalDigits, boolean odoObject,
		RenderingArray array, String fieldName) implements RenderingItem {

	/**
	 * The corresponding JAVA type name.
	 * 
	 * @return JAVA type name
	 */
	public String typeName() {
		if (totalDigits <= 4) {
			return "Short";
		} else if (totalDigits <= 9) {
			return "Integer";
		} else {
			return "Long";
		}
	}

	@Override
	public int maxBytesLen() {
		int maxBytesLen = BytesLenUtils.binaryNumberByteLen(totalDigits);
		return array == null ? maxBytesLen : array.maxOccurs() * maxBytesLen;
	}
}
