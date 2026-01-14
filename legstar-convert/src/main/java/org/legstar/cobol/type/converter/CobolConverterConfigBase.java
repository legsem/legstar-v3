package org.legstar.cobol.type.converter;

import java.nio.charset.Charset;

public abstract class CobolConverterConfigBase<T extends CobolConverterConfig> implements CobolConverterConfig {

	private String hostCharsetName;

	private int unspecifiedSignNibbleValue = 0x0f;

	private int positiveSignNibbleValue = 0x0c;

	private int negativeSignNibbleValue = 0x0d;

	private int hostPlusSign;

	private int hostMinusSign;

	private int hostSpaceCharCode;

	private int maxCobolTypeBytesLen;

	private boolean truncateHostStringsTrailingSpaces;

	@Override
	public String hostCharsetName() {
		return hostCharsetName;
	}

	public abstract T self();

	@Override
	public int unspecifiedSignNibbleValue() {
		return unspecifiedSignNibbleValue;
	}

	@Override
	public int positiveSignNibbleValue() {
		return positiveSignNibbleValue;
	}

	@Override
	public int negativeSignNibbleValue() {
		return negativeSignNibbleValue;
	}

	@Override
	public int hostPlusSign() {
		return hostPlusSign;
	}

	@Override
	public int hostMinusSign() {
		return hostMinusSign;
	}

	@Override
	public int hostSpaceCharCode() {
		return hostSpaceCharCode;
	}

	@Override
	public boolean truncateHostStringsTrailingSpaces() {
		return truncateHostStringsTrailingSpaces;
	}

	@Override
	public int maxCobolTypeBytesLen() {
		return maxCobolTypeBytesLen;
	}

	public T setHostCharsetName(String hostCharsetName) {
		if (!Charset.isSupported(hostCharsetName)) {
			throw new IllegalArgumentException("The charset name " + hostCharsetName
					+ " is not supported by your java environment." + " You might miss lib/charsets.jar in your jre.");
		}
		this.hostCharsetName = hostCharsetName;
		return self();
	}

	public void setUnspecifiedSignNibbleValue(int unspecifiedSignNibbleValue) {
		this.unspecifiedSignNibbleValue = unspecifiedSignNibbleValue;
	}

	public void setPositiveSignNibbleValue(int positiveSignNibbleValue) {
		this.positiveSignNibbleValue = positiveSignNibbleValue;
	}

	public void setNegativeSignNibbleValue(int negativeSignNibbleValue) {
		this.negativeSignNibbleValue = negativeSignNibbleValue;
	}

	public void setHostPlusSign(int hostPlusSign) {
		this.hostPlusSign = hostPlusSign;
	}

	public void setHostMinusSign(int hostMinusSign) {
		this.hostMinusSign = hostMinusSign;
	}

	public void setHostSpaceCharCode(int hostSpaceCharCode) {
		this.hostSpaceCharCode = hostSpaceCharCode;
	}

	public void setMaxCobolTypeBytesLen(int maxCobolTypeBytesLen) {
		this.maxCobolTypeBytesLen = maxCobolTypeBytesLen;
	}

	public void setTruncateHostStringsTrailingSpaces(boolean truncateHostStringsTrailingSpaces) {
		this.truncateHostStringsTrailingSpaces = truncateHostStringsTrailingSpaces;
	}

}
