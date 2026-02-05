package org.legstar.cobol.jaxb.converter;

import org.legstar.cobol.converter.CobolConverterConfigBase;

public class CobolJaxbConverterConfig extends CobolConverterConfigBase<CobolJaxbConverterConfig> {

	public CobolJaxbConverterConfig(String hostCharsetName) {
		super(hostCharsetName);
	}

	public static CobolJaxbConverterConfig ebcdic() {
		return new CobolJaxbConverterConfig(DEFAULT_EBCDIC_CHARSET) 
				.setMaxCobolTypeBytesLen(DEFAULT_MAX_BYTES_LEN); 
	}

	public static CobolJaxbConverterConfig ascii() {
		return new CobolJaxbConverterConfig(DEFAULT_ASCII_CHARSET)
				.setMaxCobolTypeBytesLen(Integer.MAX_VALUE);
	}

	@Override
	public CobolJaxbConverterConfig self() {
		return this;
	}

}
