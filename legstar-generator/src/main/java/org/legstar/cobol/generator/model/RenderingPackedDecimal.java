package org.legstar.cobol.generator.model;

import org.legstar.cobol.type.utils.BytesLenUtils;

public record RenderingPackedDecimal(String cobolName, boolean signed, int totalDigits, int fractionDigits,
		boolean odoObject, RenderingArray array) implements RenderingItem {

	@Override
	public int maxBytesLen() {
		int maxBytesLen = BytesLenUtils.packedDecimalByteLen(totalDigits);
		return array == null ? maxBytesLen : array.maxOccurs() * maxBytesLen;
	}
}
