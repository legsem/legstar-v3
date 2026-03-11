package org.legstar.cobol.converter;

import java.io.UnsupportedEncodingException;

/**
 * Set of parameters for the cobol converter.
 */
public abstract class CobolConverterConfigBase<T extends CobolConverterConfigBase<T>> implements CobolConverterConfig {

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
	private final int unspecifiedSignNibbleValue = 0x0f;

	/**
	 * Positive sign nibble value (for unsigned zoned and packed decimals)
	 */
	private final int positiveSignNibbleValue = 0x0c;

	/**
	 * Negative sign nibble value (for unsigned zoned and packed decimals)
	 */
	private final int negativeSignNibbleValue = 0x0d;

	/**
	 * Plus sign character encoding (for zoned decimals with sign separate)
	 */
	private final int hostPlusSign;

	/**
	 * Minus sign character encoding (for zoned decimals with sign separate)
	 */
	private final int hostMinusSign;

	/**
	 * Space character encoding
	 */
	private final int hostSpaceCharCode;

	/**
	 * Should trailing spaces be trimmed from host strings when converted to java
	 */
	private boolean truncateHostStringsTrailingSpaces;

	/**
	 * A set of parameters for the cobol converter.
	 * 
	 * @param hostCharsetName the cobol character set
	 */
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
	 * Should strings be right trimmed
	 * 
	 * @param truncateHostStringsTrailingSpaces true if strings should be right
	 *                                          trimmed
	 * @return this
	 */
	public T setTruncateHostStringsTrailingSpaces(boolean truncateHostStringsTrailingSpaces) {
		this.truncateHostStringsTrailingSpaces = truncateHostStringsTrailingSpaces;
		return self();
	}

	/**
	 * Fluent interface. 
	 * @return the actual instance
	 */
	public abstract T self();
	
}
