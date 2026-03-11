package org.legstar.cobol.jaxb.converter;

import org.legstar.cobol.converter.CobolConverterConfigBase;

/**
 * Set of parameters for the cobol XML converter.
 */
public class CobolJaxbConverterConfig extends CobolConverterConfigBase<CobolJaxbConverterConfig> {

	/**
	 * Whether to produce an indented formatted Xml.
	 */
	private boolean formattedOutput;

	/**
	 * A set of parameters for the cobol XML converter.
	 * 
	 * @param hostCharsetName the cobol character set
	 */
	public CobolJaxbConverterConfig(String hostCharsetName) {
		super(hostCharsetName);
	}

	/**
	 * Whether to produce an indented formatted Xml
	 * 
	 * @return true to produce an indented formatted Xml
	 */
	public boolean isFormattedOutput() {
		return formattedOutput;
	}

	/**
	 * Set whether to produce an indented formatted Xml
	 * 
	 * @param formattedOutput true to produce an indented formatted Xml
	 * @return this
	 */
	public CobolJaxbConverterConfig setFormattedOutput(boolean formattedOutput) {
		this.formattedOutput = formattedOutput;
		return this;
	}

	/**
	 * A default configuration assuming cobol is using ebcdic (usual)
	 * 
	 * @return an ebcdic configuration
	 */
	public static CobolJaxbConverterConfig ebcdic() {
		return new CobolJaxbConverterConfig(DEFAULT_EBCDIC_CHARSET);
	}

	/**
	 * A default configuration assuming cobol is using ascii (unusual)
	 * 
	 * @return an ascii configuration
	 */
	public static CobolJaxbConverterConfig ascii() {
		return new CobolJaxbConverterConfig(DEFAULT_ASCII_CHARSET);
	}

	@Override
	public CobolJaxbConverterConfig self() {
		return this;
	}

}
