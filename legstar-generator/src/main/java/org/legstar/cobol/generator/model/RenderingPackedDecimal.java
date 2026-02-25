package org.legstar.cobol.generator.model;

import org.legstar.cobol.utils.BytesLenUtils;

/**
 * COBOL packed decimal numbers (COMP-3)
 * 
 * @param cobolName      cobol name
 * @param signed         signed or unsigned
 * @param totalDigits    total number of digits (include any fraction digit)
 * @param fractionDigits number of fraction digits
 * @param odoObject      true if this gives the dimension of a variable size
 *                       array (Occurs Depending On)
 * @param array          If this is an array, null otherwise
 * @param fieldName      java name
 * 
 */
public record RenderingPackedDecimal(String cobolName, boolean signed, int totalDigits, int fractionDigits,
		boolean odoObject, RenderingArray array, String fieldName) implements RenderingItem {

	@Override
	public int maxBytesLen() {
		int maxBytesLen = BytesLenUtils.packedDecimalByteLen(totalDigits);
		return array == null ? maxBytesLen : array.maxOccurs() * maxBytesLen;
	}
}
