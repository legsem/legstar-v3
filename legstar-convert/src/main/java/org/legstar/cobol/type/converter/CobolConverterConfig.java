package org.legstar.cobol.type.converter;

import java.io.UnsupportedEncodingException;

public class CobolConverterConfig {

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
	
	public CobolConverterConfig(String hostCharsetName) {
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

	public static CobolConverterConfig ebcdic() {
		return new CobolConverterConfig("IBM01140") //  USA, Canada with euro
				.setMaxCobolTypeBytesLen(134217727); // Maximum size of a level 01 data item.
														// No CobolType can exceed that length.
	}

	public static CobolConverterConfig ascii() {
		return new CobolConverterConfig("ISO-8859-1") // Latin-1
				.setMaxCobolTypeBytesLen(Integer.MAX_VALUE);
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

	public CobolConverterConfig setUnspecifiedSignNibbleValue(int unspecifiedSignNibbleValue) {
		this.unspecifiedSignNibbleValue = unspecifiedSignNibbleValue;
		return this;
	}

	public CobolConverterConfig setPositiveSignNibbleValue(int positiveSignNibbleValue) {
		this.positiveSignNibbleValue = positiveSignNibbleValue;
		return this;
	}

	public CobolConverterConfig setNegativeSignNibbleValue(int negativeSignNibbleValue) {
		this.negativeSignNibbleValue = negativeSignNibbleValue;
		return this;
	}

	public CobolConverterConfig setHostPlusSign(int hostPlusSign) {
		this.hostPlusSign = hostPlusSign;
		return this;
	}

	public CobolConverterConfig setHostMinusSign(int hostMinusSign) {
		this.hostMinusSign = hostMinusSign;
		return this;
	}

	public CobolConverterConfig setHostSpaceCharCode(int hostSpaceCharCode) {
		this.hostSpaceCharCode = hostSpaceCharCode;
		return this;
	}

	public CobolConverterConfig setMaxCobolTypeBytesLen(int maxCobolTypeBytesLen) {
		this.maxCobolTypeBytesLen = maxCobolTypeBytesLen;
		return this;
	}

	public CobolConverterConfig setTruncateHostStringsTrailingSpaces(boolean truncateHostStringsTrailingSpaces) {
		this.truncateHostStringsTrailingSpaces = truncateHostStringsTrailingSpaces;
		return this;
	}

}
