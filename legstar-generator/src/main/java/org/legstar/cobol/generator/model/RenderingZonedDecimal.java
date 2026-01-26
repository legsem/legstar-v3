package org.legstar.cobol.generator.model;

import org.legstar.cobol.type.utils.BytesLenUtils;

public record RenderingZonedDecimal(String cobolName, int totalDigits, int fractionDigits, boolean signLeading,
		boolean signSeparate, boolean odoObject, RenderingArray array) implements RenderingItem {

	@Override
	public int maxBytesLen() {
		int maxBytesLen = BytesLenUtils.zonedDecimalByteLen(totalDigits, signSeparate);
		return array == null ? maxBytesLen : array.maxOccurs() * maxBytesLen;
	}
}
