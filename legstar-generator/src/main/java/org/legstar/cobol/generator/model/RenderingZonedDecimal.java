package org.legstar.cobol.generator.model;

public record RenderingZonedDecimal(String cobolName, int totalDigits, int fractionDigits, boolean signLeading,
		boolean signSeparate, boolean odoObject) implements RenderingItem {
}
