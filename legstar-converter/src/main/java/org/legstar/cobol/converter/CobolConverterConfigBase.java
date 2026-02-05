package org.legstar.cobol.converter;

import java.io.UnsupportedEncodingException;

public abstract class CobolConverterConfigBase<T extends CobolConverterConfigBase<T>> {

	/**
	 * Maximum size of a level 01 data item. No CobolType can exceed that length.
	 */
	public static final int DEFAULT_MAX_BYTES_LEN = 134217727;

	/**
	 * USA, Canada with euro
	 */
	public static final String DEFAULT_EBCDIC_CHARSET = "IBM01140";

	/**
	 *  Latin-1
	 */
	public static final String DEFAULT_ASCII_CHARSET = "ISO-8859-1";

	/**
	 * Character set used by the host to encode strings (Must be a valid java
	 * charset)
	 */
	private final String hostCharsetName;

	/**
	 * Unspecified sign nibble value (for unsigned zoned and packed decimals)
	 */
	private int unspecifiedSignNibbleValue = 0x0f;

	/**
	 * Positive sign nibble value (for unsigned zoned and packed decimals)
	 */
	private int positiveSignNibbleValue = 0x0c;

	/**
	 * Negative sign nibble value (for unsigned zoned and packed decimals)
	 */
	private int negativeSignNibbleValue = 0x0d;

	/**
	 * Plus sign character encoding (for zoned decimals with sign separate)
	 */
	private int hostPlusSign;

	/**
	 * Minus sign character encoding (for zoned decimals with sign separate)
	 */
	private int hostMinusSign;

	/**
	 * Space character encoding
	 */
	private int hostSpaceCharCode;

	/**
	 * Maximum number of bytes a level 01 item can span
	 */
	private int maxCobolTypeBytesLen;

	/**
	 * Should trailing spaces be trimmed from host strings when converted to java
	 */
	private boolean truncateHostStringsTrailingSpaces;
	
	public CobolConverterConfigBase(String hostCharsetName) {
		this.hostCharsetName = hostCharsetName;
		try {
			hostPlusSign = "+".getBytes(hostCharsetName)[0];
			hostMinusSign = "-".getBytes(hostCharsetName)[0];
			hostSpaceCharCode = " ".getBytes(hostCharsetName)[0];
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("The charset name " + hostCharsetName
					+ " is not supported by your java environment." + " You might miss lib/charsets.jar in your jre.");
		}
	}

	public String hostCharsetName() {
		return hostCharsetName;
	}

	public int unspecifiedSignNibbleValue() {
		return unspecifiedSignNibbleValue;
	}

	public int positiveSignNibbleValue() {
		return positiveSignNibbleValue;
	}

	public int negativeSignNibbleValue() {
		return negativeSignNibbleValue;
	}

	public int hostPlusSign() {
		return hostPlusSign;
	}

	public int hostMinusSign() {
		return hostMinusSign;
	}

	public int hostSpaceCharCode() {
		return hostSpaceCharCode;
	}

	public boolean truncateHostStringsTrailingSpaces() {
		return truncateHostStringsTrailingSpaces;
	}

	public int maxCobolTypeBytesLen() {
		return maxCobolTypeBytesLen;
	}

	public T setUnspecifiedSignNibbleValue(int unspecifiedSignNibbleValue) {
		this.unspecifiedSignNibbleValue = unspecifiedSignNibbleValue;
		return self();
	}

	public T setPositiveSignNibbleValue(int positiveSignNibbleValue) {
		this.positiveSignNibbleValue = positiveSignNibbleValue;
		return self();
	}

	public T setNegativeSignNibbleValue(int negativeSignNibbleValue) {
		this.negativeSignNibbleValue = negativeSignNibbleValue;
		return self();
	}

	public T setHostPlusSign(int hostPlusSign) {
		this.hostPlusSign = hostPlusSign;
		return self();
	}

	public T setHostMinusSign(int hostMinusSign) {
		this.hostMinusSign = hostMinusSign;
		return self();
	}

	public T setHostSpaceCharCode(int hostSpaceCharCode) {
		this.hostSpaceCharCode = hostSpaceCharCode;
		return self();
	}

	public T setMaxCobolTypeBytesLen(int maxCobolTypeBytesLen) {
		this.maxCobolTypeBytesLen = maxCobolTypeBytesLen;
		return self();
	}

	public T setTruncateHostStringsTrailingSpaces(boolean truncateHostStringsTrailingSpaces) {
		this.truncateHostStringsTrailingSpaces = truncateHostStringsTrailingSpaces;
		return self();
	}
	
	public abstract T self();

}
