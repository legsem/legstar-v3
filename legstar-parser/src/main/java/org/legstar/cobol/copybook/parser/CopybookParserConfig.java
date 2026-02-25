package org.legstar.cobol.copybook.parser;

/**
 * Cobol copybook Parser parameters.
 */
public class CopybookParserConfig {

	/** Fixed (false) or Free (true) format COBOL source. */
	private boolean freeCodeFormat = false;

	/** For fixed format COBOL, position of the indicator area. */
	private int startColumn = 7;

	/** For fixed format COBOL, position of the right margin. */
	private int endColumn = 72;

	/**
	 * Create configuration parameters
	 */
	public CopybookParserConfig() {
	}

	/**
	 * Whether the code format is free or fixed
	 * 
	 * @return true if the code format is free
	 */
	public boolean isFreeCodeFormat() {
		return freeCodeFormat;
	}

	/**
	 * Set the code format.
	 * 
	 * @param freeCodeFormat Fixed (false) or Free (true) format COBOL source
	 * @return this
	 */
	public CopybookParserConfig setFreeCodeFormat(boolean freeCodeFormat) {
		this.freeCodeFormat = freeCodeFormat;
		return this;
	}

	/**
	 * Start column for fixed format
	 * 
	 * @return the start column for fixed format
	 */
	public int getStartColumn() {
		return startColumn;
	}

	/**
	 * Set the start column for fixed format
	 * 
	 * @param startColumn start column for fixed format
	 * @return this
	 */
	public CopybookParserConfig setStartColumn(int startColumn) {
		this.startColumn = startColumn;
		return this;
	}

	/**
	 * End column for fixed format
	 * 
	 * @return the end column for fixed format
	 */
	public int getEndColumn() {
		return endColumn;
	}

	/**
	 * Set the end column for fixed format
	 * 
	 * @param endColumn end column for fixed format
	 * @return this
	 */
	public CopybookParserConfig setEndColumn(int endColumn) {
		this.endColumn = endColumn;
		return this;
	}

}
