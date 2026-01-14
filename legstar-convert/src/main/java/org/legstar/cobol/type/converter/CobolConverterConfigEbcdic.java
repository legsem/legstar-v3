package org.legstar.cobol.type.converter;

public class CobolConverterConfigEbcdic extends CobolConverterConfigBase<CobolConverterConfigEbcdic> {

	public CobolConverterConfigEbcdic() {
		setHostCharsetName("IBM01140");
		setHostPlusSign(0x4e);
		setHostMinusSign(0x60);
		setHostSpaceCharCode(0x40);
		setMaxCobolTypeBytesLen(134217727); // Maximum size of a level 01 data item. No CobolType can exceed that
											// length.
	}

	@Override
	public CobolConverterConfigEbcdic self() {
		return this;
	}
}
