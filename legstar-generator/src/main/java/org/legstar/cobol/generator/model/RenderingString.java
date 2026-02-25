package org.legstar.cobol.generator.model;

/**
 * COBOL alphanumeric
 * 
 * @param cobolName cobol name
 * @param charNum   the number of characters
 * @param array     If this is an array, null otherwise
 * @param fieldName java name
 * 
 */
public record RenderingString(String cobolName, int charNum, RenderingArray array, String fieldName)
		implements RenderingItem {

	@Override
	public int maxBytesLen() {
		int maxBytesLen = charNum;
		return array == null ? maxBytesLen : array.maxOccurs() * maxBytesLen;
	}
}
