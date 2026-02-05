package org.legstar.cobol.converter;

public class CobolConverterConfig extends CobolConverterConfigBase<CobolConverterConfig> {
	
	public CobolConverterConfig(String hostCharsetName) {
		super(hostCharsetName);
	}

	public static CobolConverterConfig ebcdic() {
		return new CobolConverterConfig(DEFAULT_EBCDIC_CHARSET) 
				.setMaxCobolTypeBytesLen(DEFAULT_MAX_BYTES_LEN); 
	}

	public static CobolConverterConfig ascii() {
		return new CobolConverterConfig(DEFAULT_ASCII_CHARSET)
				.setMaxCobolTypeBytesLen(Integer.MAX_VALUE);
	}

	@Override
	public CobolConverterConfig self() {
		return this;
	}

}
