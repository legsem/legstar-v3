package org.legstar.cobol.jaxb.converter;

import org.legstar.cobol.converter.CobolBeanConverterConfig;

/**
 * Set of parameters for the cobol XML converter.
 */
public class CobolXmlConverterConfig extends CobolBeanConverterConfig {

	/**
	 * Whether to produce an indented formatted Xml.
	 */
	private boolean formattedOutput;

	/**
	 * A set of parameters for the cobol XML converter.
	 * 
	 * @param hostCharsetName the cobol character set
	 */
	public CobolXmlConverterConfig(String hostCharsetName) {
		super(hostCharsetName);
	}

	@Override
	public CobolXmlConverterConfig setUnspecifiedSignNibbleValue(int unspecifiedSignNibbleValue) {
		return (CobolXmlConverterConfig) super.setUnspecifiedSignNibbleValue(unspecifiedSignNibbleValue);
	}

	@Override
	public CobolXmlConverterConfig setPositiveSignNibbleValue(int positiveSignNibbleValue) {
		return (CobolXmlConverterConfig) super.setPositiveSignNibbleValue(positiveSignNibbleValue);
	}

	@Override
	public CobolXmlConverterConfig setNegativeSignNibbleValue(int negativeSignNibbleValue) {
		return (CobolXmlConverterConfig) super.setNegativeSignNibbleValue(negativeSignNibbleValue);
	}

	@Override
	public CobolXmlConverterConfig setHostPlusSign(int hostPlusSign) {
		return (CobolXmlConverterConfig) super.setHostPlusSign(hostPlusSign);
	}

	@Override
	public CobolXmlConverterConfig setHostMinusSign(int hostMinusSign) {
		return (CobolXmlConverterConfig) super.setHostMinusSign(hostMinusSign);
	}

	@Override
	public CobolXmlConverterConfig setHostSpaceCharCode(int hostSpaceCharCode) {
		return (CobolXmlConverterConfig) super.setHostSpaceCharCode(hostSpaceCharCode);
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
	public CobolXmlConverterConfig setFormattedOutput(boolean formattedOutput) {
		this.formattedOutput = formattedOutput;
		return this;
	}

	/**
	 * A default configuration assuming cobol is using ebcdic (usual)
	 * 
	 * @return an ebcdic configuration
	 */
	public static CobolXmlConverterConfig ebcdic() {
		return new CobolXmlConverterConfig(DEFAULT_EBCDIC_CHARSET);
	}

	/**
	 * A default configuration assuming cobol is using ascii (unusual)
	 * 
	 * @return an ascii configuration
	 */
	public static CobolXmlConverterConfig ascii() {
		return new CobolXmlConverterConfig(DEFAULT_ASCII_CHARSET);
	}

}
