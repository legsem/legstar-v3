package org.legstar.cobol.converter;

import java.io.UnsupportedEncodingException;

public class CobolBeanConverterConfig {
	
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
	
	public CobolBeanConverterConfig(String hostCharsetName) {
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

	public CobolBeanConverterConfig setUnspecifiedSignNibbleValue(int unspecifiedSignNibbleValue) {
		this.unspecifiedSignNibbleValue = unspecifiedSignNibbleValue;
		return this;
	}

	public CobolBeanConverterConfig setPositiveSignNibbleValue(int positiveSignNibbleValue) {
		this.positiveSignNibbleValue = positiveSignNibbleValue;
		return this;
	}

	public CobolBeanConverterConfig setNegativeSignNibbleValue(int negativeSignNibbleValue) {
		this.negativeSignNibbleValue = negativeSignNibbleValue;
		return this;
	}

	public CobolBeanConverterConfig setHostPlusSign(int hostPlusSign) {
		this.hostPlusSign = hostPlusSign;
		return this;
	}

	public CobolBeanConverterConfig setHostMinusSign(int hostMinusSign) {
		this.hostMinusSign = hostMinusSign;
		return this;
	}

	public CobolBeanConverterConfig setHostSpaceCharCode(int hostSpaceCharCode) {
		this.hostSpaceCharCode = hostSpaceCharCode;
		return this;
	}

	public CobolBeanConverterConfig setMaxCobolTypeBytesLen(int maxCobolTypeBytesLen) {
		this.maxCobolTypeBytesLen = maxCobolTypeBytesLen;
		return this;
	}

	public CobolBeanConverterConfig setTruncateHostStringsTrailingSpaces(boolean truncateHostStringsTrailingSpaces) {
		this.truncateHostStringsTrailingSpaces = truncateHostStringsTrailingSpaces;
		return this;
	}

	public static CobolBeanConverterConfig ebcdic() {
		return new CobolBeanConverterConfig(DEFAULT_EBCDIC_CHARSET) 
				.setMaxCobolTypeBytesLen(DEFAULT_MAX_BYTES_LEN); 
	}

	public static CobolBeanConverterConfig ascii() {
		return new CobolBeanConverterConfig(DEFAULT_ASCII_CHARSET)
				.setMaxCobolTypeBytesLen(Integer.MAX_VALUE);
	}

}
