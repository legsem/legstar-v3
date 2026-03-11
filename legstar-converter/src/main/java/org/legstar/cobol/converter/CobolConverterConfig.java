package org.legstar.cobol.converter;

/**
 * Set of parameters for the cobol converter.
 */
public interface CobolConverterConfig {

	/**
	 * Cobol character set
	 * 
	 * @return the cobol character set
	 */
	String hostCharsetName();

	/**
	 * For decimals this indicates an overpunched unsigned nibble (half byte)
	 * 
	 * @return overpunched unsigned nibble
	 */
	int unspecifiedSignNibbleValue();

	/**
	 * For decimals this indicates an overpunched positive sign nibble (half byte)
	 * 
	 * @return overpunched positive sign nibble
	 */
	int positiveSignNibbleValue();

	/**
	 * For decimals this indicates an overpunched negative sign nibble (half byte)
	 * 
	 * @return overpunched negative sign nibble
	 */
	int negativeSignNibbleValue();

	/**
	 * For numerics this a plus sign in the cobol character set
	 * 
	 * @return plus sign in the cobol character set
	 */
	int hostPlusSign();

	/**
	 * For numerics this a minus sign in the cobol character set
	 * 
	 * @return minus sign in the cobol character set
	 */
	int hostMinusSign();

	/**
	 * Space character in the cobol character set
	 * 
	 * @return space character in the cobol character set
	 */
	int hostSpaceCharCode();

	/**
	 * Should strings be right trimmed
	 * 
	 * @return true if strings should be right trimmed
	 */
	boolean truncateHostStringsTrailingSpaces();

}
