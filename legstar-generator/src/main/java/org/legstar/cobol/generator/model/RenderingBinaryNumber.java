package org.legstar.cobol.generator.model;

import org.legstar.cobol.type.utils.BytesLenUtils;

/**
 * TODO unsigned native binaries may require Integer/Long/BigInteger
 */
public record RenderingBinaryNumber(String cobolName, boolean signed, int totalDigits,
		boolean odoObject, RenderingArray array, String fieldName) implements RenderingItem {

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
