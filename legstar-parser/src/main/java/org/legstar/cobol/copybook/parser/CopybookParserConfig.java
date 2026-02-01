package org.legstar.cobol.copybook.parser;

public class CopybookParserConfig {

	/** Fixed (false) or Free (true) format COBOL source. */
	private boolean freeCodeFormat = false;

	/** For fixed format COBOL, position of the indicator area. */
	private int startColumn = 7;

	/** For fixed format COBOL, position of the right margin. */
	private int endColumn = 72;

	public boolean isFreeCodeFormat() {
		return freeCodeFormat;
	}

	public CopybookParserConfig setFreeCodeFormat(boolean freeCodeFormat) {
		this.freeCodeFormat = freeCodeFormat;
		return this;
	}

	public int getStartColumn() {
		return startColumn;
	}

	public CopybookParserConfig setStartColumn(int startColumn) {
		this.startColumn = startColumn;
		return this;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public CopybookParserConfig setEndColumn(int endColumn) {
		this.endColumn = endColumn;
		return this;
	}

}
