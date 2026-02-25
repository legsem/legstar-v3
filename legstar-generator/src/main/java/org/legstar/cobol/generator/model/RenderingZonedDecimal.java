package org.legstar.cobol.generator.model;

import org.legstar.cobol.utils.BytesLenUtils;

/**
 * COBOL zoned decimal numbers
 * 
 * @param cobolName      cobol name
 * @param totalDigits    total number of digits (include any fraction digit)
 * @param fractionDigits number of fraction digits
 * @param signLeading    is the sign leading (trailing otherwise)
 * @param signSeparate   is the sign separate (overpunched otherwise)
 * @param blankWhenZero  is the content blank instead of zeroes
 * @param odoObject      true if this gives the dimension of a variable size
 *                       array (Occurs Depending On)
 * @param array          If this is an array, null otherwise
 * @param fieldName      java name
 * 
 */
public record RenderingZonedDecimal(String cobolName, int totalDigits, int fractionDigits, boolean signLeading,
		boolean signSeparate, boolean blankWhenZero, boolean odoObject, RenderingArray array, String fieldName)
		implements RenderingItem {

	@Override
	public int maxBytesLen() {
		int maxBytesLen = BytesLenUtils.zonedDecimalByteLen(totalDigits, signSeparate);
		return array == null ? maxBytesLen : array.maxOccurs() * maxBytesLen;
	}
}
