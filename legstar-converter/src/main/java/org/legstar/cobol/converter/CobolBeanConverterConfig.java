package org.legstar.cobol.converter;

/**
 * Set of parameters for the cobol converter.
 */
public class CobolBeanConverterConfig extends CobolConverterConfigBase<CobolBeanConverterConfig> {

	/**
	 * A set of parameters for the cobol converter.
	 * 
	 * @param hostCharsetName the cobol character set
	 */
	public CobolBeanConverterConfig(String hostCharsetName) {
		super(hostCharsetName);
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

	@Override
	public CobolBeanConverterConfig self() {
		return this;
	}

}
