package org.legstar.cobol.converter;

import java.io.IOException;
import java.math.BigDecimal;

import org.legstar.cobol.utils.BytesLenUtils;

/**
 * Converts between a Cobol packed decimal and a java BigDecimal.
 */
public class CobolPackedDecimalConverter {

	private final String spaceCharValue;
	private final int negativeSignNibbleValue;
	private final int positiveSignNibbleValue;
	private final int unspecifiedSignNibbleValue;

	public CobolPackedDecimalConverter(int hostSpaceCharCode, int positiveSignNibbleValue, int negativeSignNibbleValue,
			int unspecifiedSignNibbleValue) {
		spaceCharValue = new StringBuilder() //
				.append(highNibble(hostSpaceCharCode)) //
				.append(lowNibble(hostSpaceCharCode)) //
				.toString();
		this.negativeSignNibbleValue = negativeSignNibbleValue;
		this.positiveSignNibbleValue = positiveSignNibbleValue;
		this.unspecifiedSignNibbleValue = unspecifiedSignNibbleValue;
	}

	@SuppressWarnings(value = "unchecked")
	public <T> T convert(CobolInputStream is, boolean signed, int totalDigits, int fractionDigits,
			Class<T> targetClass) {
		if (targetClass.equals(String.class)) {
			return (T) toString(is, signed, totalDigits, fractionDigits);
		} else if (targetClass.equals(BigDecimal.class)) {
			return (T) toBigDecimal(is, signed, totalDigits, fractionDigits);
		} else {
			throw new CobolBeanConverterException("Unsupported target class " + targetClass);
		}
	}

	public String toString(CobolInputStream is, boolean signed, int totalDigits, int fractionDigits) {
		BigDecimal dec = toBigDecimal(is, signed, totalDigits, fractionDigits);
		return dec == null ? null : dec.toPlainString();
	}

	/**
	 * Packed decimals encode each digit on 4 bits (a nibble). This allows to store
	 * 2 digits in a byte (one high nibble, one low nibble).
	 * <p>
	 * This rule is true apart from the last (rightmost) byte where the low nibble
	 * holds the sign. This is true even for unsigned decimals.
	 * <p>
	 * The storage size is the number of bytes needed to store totalDigits + 1
	 * nibbles.
	 * <p>
	 * Here we allow all spaces and all low values but otherwise throw an exception
	 * if the packed decimal is malformed.
	 * 
	 * @param is             host bytes to convert
	 * @param signed         a signed decimal
	 * @param totalDigits    total number of digits (including fraction digits)
	 * @param fractionDigits scale
	 * @return a BigDecimal
	 */
	public BigDecimal toBigDecimal(CobolInputStream is, boolean signed, int totalDigits, int fractionDigits) {

		try {
			StringBuilder sb = new StringBuilder();
			int bytesLen = BytesLenUtils.packedDecimalByteLen(totalDigits);
			for (int i = 0; i < bytesLen; i++) {
				int c = is.read();
				if (c == -1) {
					throw new CobolBeanConverterEOFException();
				}
				int hn = highNibble(c);
				if (hn < 0 || hn > 9) {
					throw new CobolBeanConverterException("High nibble " + hn + " at byte position " + i
							+ " is invalid for a BigDecimal. Not in [0-9] range.");
				}
				sb.append(hn);

				int ln = lowNibble(c);
				if (ln < 0 || ln > 9) {
					if (i == bytesLen - 1) {
						if (ln == negativeSignNibbleValue) {
							sb.insert(0, "-");
						} else if (ln != positiveSignNibbleValue && ln != unspecifiedSignNibbleValue) {
							throw new CobolBeanConverterException("Low nibble " + ln + " at byte position " + i
									+ " is invalid for a BigDecimal. Not a sign indicator.");
						}
					} else {
						throw new CobolBeanConverterException("Low nibble " + ln + " at byte position " + i
								+ " is invalid for a BigDecimal. Not in [0-9] range.");
					}
				} else {
					sb.append(ln);
				}
			}

			String s = sb.toString();
			if (s.isEmpty() || isAllSpaces(s)) {
				return BigDecimal.valueOf(0);
			} else {
				return new BigDecimal(s).scaleByPowerOfTen(-fractionDigits);
			}
		} catch (IOException | NumberFormatException e) {
			throw new CobolBeanConverterException(e);
		}

	}

	/**
	 * return true if the string is a sequence of host space characters only
	 */
	private boolean isAllSpaces(String s) {
		return s.replace(spaceCharValue, "").length() == 0;
	}

	private int highNibble(int c) {
		return ((byte) c >> 4) & 0x0f;
	}

	private int lowNibble(int c) {
		return (byte) c & 0x0f;
	}

}
