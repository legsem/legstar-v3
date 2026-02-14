package org.legstar.cobol.jaxb.converter;

import org.legstar.cobol.converter.CobolBeanConverterConfig;

public class CobolXmlConverterConfig extends CobolBeanConverterConfig {
	
	/**
	 * Whether to produce an indented formatted Xml.
	 */
	private boolean formattedOutput;

	public CobolXmlConverterConfig(String hostCharsetName) {
		super(hostCharsetName);
	}

	public static CobolXmlConverterConfig ebcdic() {
		return new CobolXmlConverterConfig(DEFAULT_EBCDIC_CHARSET)
				.setMaxCobolTypeBytesLen(DEFAULT_MAX_BYTES_LEN);
	}

	public static CobolXmlConverterConfig ascii() {
		return new CobolXmlConverterConfig(DEFAULT_ASCII_CHARSET)
				.setMaxCobolTypeBytesLen(Integer.MAX_VALUE);
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

	@Override
	public CobolXmlConverterConfig setMaxCobolTypeBytesLen(int maxCobolTypeBytesLen) {
		return (CobolXmlConverterConfig) super.setMaxCobolTypeBytesLen(maxCobolTypeBytesLen);
	}

	@Override
	public CobolXmlConverterConfig setTruncateHostStringsTrailingSpaces(boolean truncateHostStringsTrailingSpaces) {
		return (CobolXmlConverterConfig) super.setTruncateHostStringsTrailingSpaces(truncateHostStringsTrailingSpaces);
	}

	public boolean isFormattedOutput() {
		return formattedOutput;
	}

	public CobolXmlConverterConfig setFormattedOutput(boolean formattedOutput) {
		this.formattedOutput = formattedOutput;
		return this;
	}

}
