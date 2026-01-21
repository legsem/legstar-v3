package org.legstar.cobol.generator.model;

public record RenderingBinaryNumber(String cobolName, boolean signed, int totalDigits, boolean odoObject)
		implements RenderingItem {
}
