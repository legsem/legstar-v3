package org.legstar.cobol.converter;

import java.io.UnsupportedEncodingException;

/**
 * Set of parameters for the cobol converter.
 */
public class CobolBeanConverterConfig {

	/**
	 * USA, Canada with euro
	 */
	public static final String DEFAULT_EBCDIC_CHARSET = "IBM01140";

	/**
	 * Latin-1
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
	 * Should trailing spaces be trimmed from host strings when converted to java
	 */
	private boolean truncateHostStringsTrailingSpaces;

	/**
	 * A set of parameters for the cobol converter.
	 * 
	 * @param hostCharsetName the cobol character set
	 */
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

	/**
	 * Cobol character set
	 * 
	 * @return the cobol character set
	 */
	public String hostCharsetName() {
		return hostCharsetName;
	}

	/**
	 * For decimals this indicates an overpunched unsigned nibble (half byte)
	 * 
	 * @return overpunched unsigned nibble
	 */
	public int unspecifiedSignNibbleValue() {
		return unspecifiedSignNibbleValue;
	}

	/**
	 * For decimals this indicates an overpunched positive sign nibble (half byte)
	 * 
	 * @return overpunched positive sign nibble
	 */
	public int positiveSignNibbleValue() {
		return positiveSignNibbleValue;
	}

	/**
	 * For decimals this indicates an overpunched negative sign nibble (half byte)
	 * 
	 * @return overpunched negative sign nibble
	 */
	public int negativeSignNibbleValue() {
		return negativeSignNibbleValue;
	}

	/**
	 * For numerics this a plus sign in the cobol character set
	 * 
	 * @return plus sign in the cobol character set
	 */
	public int hostPlusSign() {
		return hostPlusSign;
	}

	/**
	 * For numerics this a minus sign in the cobol character set
	 * 
	 * @return minus sign in the cobol character set
	 */
	public int hostMinusSign() {
		return hostMinusSign;
	}

	/**
	 * Space character in the cobol character set
	 * 
	 * @return space character in the cobol character set
	 */
	public int hostSpaceCharCode() {
		return hostSpaceCharCode;
	}

	/**
	 * Should strings be right trimmed
	 * 
	 * @return true if strings should be right trimmed
	 */
	public boolean truncateHostStringsTrailingSpaces() {
		return truncateHostStringsTrailingSpaces;
	}

	/**
	 * For decimals set the overpunched unsigned nibble (half byte)
	 * 
	 * @param unspecifiedSignNibbleValue overpunched unsigned nibble
	 * @return this
	 */
	public CobolBeanConverterConfig setUnspecifiedSignNibbleValue(int unspecifiedSignNibbleValue) {
		this.unspecifiedSignNibbleValue = unspecifiedSignNibbleValue;
		return this;
	}

	/**
	 * For decimals set the overpunched positive sign nibble (half byte)
	 * 
	 * @param positiveSignNibbleValue overpunched positive sign nibble
	 * @return this
	 */
	public CobolBeanConverterConfig setPositiveSignNibbleValue(int positiveSignNibbleValue) {
		this.positiveSignNibbleValue = positiveSignNibbleValue;
		return this;
	}

	/**
	 * For decimals set the overpunched negative sign nibble (half byte)
	 * 
	 * @param negativeSignNibbleValue overpunched negative sign nibble
	 * @return this
	 */
	public CobolBeanConverterConfig setNegativeSignNibbleValue(int negativeSignNibbleValue) {
		this.negativeSignNibbleValue = negativeSignNibbleValue;
		return this;
	}

	/**
	 * For numerics set the plus sign in the cobol character set
	 * 
	 * @param hostPlusSign plus sign in the cobol character set
	 * @return this
	 */
	public CobolBeanConverterConfig setHostPlusSign(int hostPlusSign) {
		this.hostPlusSign = hostPlusSign;
		return this;
	}

	/**
	 * For numerics set the sign in the cobol character set
	 * 
	 * @param hostMinusSign minus sign in the cobol character set
	 * @return this
	 */
	public CobolBeanConverterConfig setHostMinusSign(int hostMinusSign) {
		this.hostMinusSign = hostMinusSign;
		return this;
	}

	/**
	 * Set the space character in the cobol character set
	 * 
	 * @param hostSpaceCharCode space character in the cobol character set
	 * @return this
	 */
	public CobolBeanConverterConfig setHostSpaceCharCode(int hostSpaceCharCode) {
		this.hostSpaceCharCode = hostSpaceCharCode;
		return this;
	}

	/**
	 * Should strings be right trimmed
	 * 
	 * @param truncateHostStringsTrailingSpaces true if strings should be right
	 *                                          trimmed
	 * @return this
	 */
	public CobolBeanConverterConfig setTruncateHostStringsTrailingSpaces(boolean truncateHostStringsTrailingSpaces) {
		this.truncateHostStringsTrailingSpaces = truncateHostStringsTrailingSpaces;
		return this;
	}

	/**
	 * A default configuration assuming cobol is using ebcdic (usual)
	 * 
	 * @return an ebcdic configuration
	 */
	public static CobolBeanConverterConfig ebcdic() {
		return new CobolBeanConverterConfig(DEFAULT_EBCDIC_CHARSET);
	}

	/**
	 * A default configuration assuming cobol is using ascii (unusual)
	 * 
	 * @return an ascii configuration
	 */
	public static CobolBeanConverterConfig ascii() {
		return new CobolBeanConverterConfig(DEFAULT_ASCII_CHARSET);
	}

}
