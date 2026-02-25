package org.legstar.cobol.generator.model;

import org.legstar.cobol.utils.BytesLenUtils;

/**
 * COBOL float numbers (COMP-1)
 * 
 * @param cobolName   cobol name
 * @param array       If this is an array, null otherwise
 * @param fieldName   java name
 * 
 */
public record RenderingFloat(String cobolName, RenderingArray array, String fieldName) implements RenderingItem {

	@Override
	public int maxBytesLen() {
		int maxBytesLen = BytesLenUtils.floatByteLen();
		return array == null ? maxBytesLen : array.maxOccurs() * maxBytesLen;
	}
}
