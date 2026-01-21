package org.legstar.cobol.generator.model;

public record RenderingPackedDecimal(String cobolName, boolean signed, int totalDigits, int fractionDigits, boolean odoObject)
		implements RenderingItem {
}
