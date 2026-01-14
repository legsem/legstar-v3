package org.legstar.cobol.type.converter;

public class CobolConverterConfigAscii extends CobolConverterConfigBase<CobolConverterConfigAscii> {

    public CobolConverterConfigAscii() {
    	setHostCharsetName("ISO-8859-1");
    	setHostPlusSign(0x2b);
    	setHostMinusSign(0x2d);
    	setHostSpaceCharCode(0x20);
    	setMaxCobolTypeBytesLen(Integer.MAX_VALUE);
    }

	@Override
	public CobolConverterConfigAscii self() {
		return this;
	}
}
