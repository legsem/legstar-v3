package org.legsem.congocc.test;

public class CopybookConfig {

	/** Fixed (false) or Free (true) format COBOL source. */
	private boolean freeCodeFormat = false;

	/** For fixed format COBOL, position of the indicator area. */
	private int startColumn = 7;

	/** For fixed format COBOL, position of the right margin. */
	private int endColumn = 72;

	/** Currency sign used (CURRENCY SIGN clause in the SPECIAL-NAMES). */
	private String currencySign = "$";

	/**
	 * Currency symbol used (CURRENCY PICTURE SYMBOL clause in the SPECIAL-NAMES).
	 */
	private String currencySymbol = "$";

	/**
	 * Whether comma is the decimal point (DECIMAL-POINT IS COMMA clause in the
	 * SPECIAL-NAMES).
	 */
	private boolean decimalPointIsComma = false;

	/** COBOL NSYMBOL(DBCS) compiler option. Assume NSYMBOL(NATIONAL) if false. */
	private boolean nSymbolDbcs = false;

	/** COBOL QUOTE|APOST compiler option. False means APOST. */
	private boolean quoteIsQuote = false;

	public boolean isFreeCodeFormat() {
		return freeCodeFormat;
	}

	public CopybookConfig setFreeCodeFormat(boolean freeCodeFormat) {
		this.freeCodeFormat = freeCodeFormat;
		return this;
	}

	public int getStartColumn() {
		return startColumn;
	}

	public CopybookConfig setStartColumn(int startColumn) {
		this.startColumn = startColumn;
		return this;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public CopybookConfig setEndColumn(int endColumn) {
		this.endColumn = endColumn;
		return this;
	}

	public String getCurrencySign() {
		return currencySign;
	}

	public CopybookConfig setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
		return this;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public CopybookConfig setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
		return this;
	}

	public boolean isDecimalPointIsComma() {
		return decimalPointIsComma;
	}

	public CopybookConfig setDecimalPointIsComma(boolean decimalPointIsComma) {
		this.decimalPointIsComma = decimalPointIsComma;
		return this;
	}

	public boolean isnSymbolDbcs() {
		return nSymbolDbcs;
	}

	public CopybookConfig setnSymbolDbcs(boolean nSymbolDbcs) {
		this.nSymbolDbcs = nSymbolDbcs;
		return this;
	}

	public boolean isQuoteIsQuote() {
		return quoteIsQuote;
	}

	public CopybookConfig setQuoteIsQuote(boolean quoteIsQuote) {
		this.quoteIsQuote = quoteIsQuote;
		return this;
	}

}
