package org.legstar.cobol.generator.model;

import org.legstar.cobol.utils.BytesLenUtils;

public record RenderingDouble(String cobolName, RenderingArray array, String fieldName) implements RenderingItem {

	@Override
	public int maxBytesLen() {
		int maxBytesLen = BytesLenUtils.doubleByteLen();
		return array == null ? maxBytesLen : array.maxOccurs() * maxBytesLen;
	}
}
