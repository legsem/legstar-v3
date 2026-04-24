package org.legstar.cobol.converter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.legstar.cobol.io.CobolInputStream;
import org.legstar.cobol.utils.BytesLenUtils;

/**
 * Converts between a Cobol packed decimal and a java BigDecimal.
 */
public class CobolPackedDecimalConverter {

	private final String spaceCharValue;
	private final int negativeSignNibbleValue;
	private final int positiveSignNibbleValue;
	private final int unspecifiedSignNibbleValue;

	/**
	 * Build a cobol packed decimal converter.
	 * 
	 * @param hostSpaceCharCode          the space character encoding
	 * @param positiveSignNibbleValue    positive sign nibble value
	 * @param negativeSignNibbleValue    negative sign nibble value
	 * @param unspecifiedSignNibbleValue unspecified sign nibble value
	 */
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

	/**
	 * Convert a COBOL packed decimal (COMP-3).
	 * 
	 * @param <T>            the target java type
	 * @param is             the cobol input data
	 * @param signed         a signed decimal
	 * @param totalDigits    the total number of digits
	 * @param fractionDigits the number of fraction digits
	 * @param targetClass    the target java class
	 * @return the converted java value
	 */
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

	/**
	 * Convert to a BigDecimal and the get the plain string value from that.
	 * 
	 * @param is             host bytes to convert
	 * @param signed         a signed decimal
	 * @param totalDigits    total number of digits (including fraction digits)
	 * @param fractionDigits scale
	 * @return a string representation of the decimal
	 */
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
	 * Convert a java decimal to a COBOL packed decimal (COMP-3)
	 * <p>
	 * Each digit from the java decimal becomes a 4 bit nibble.
	 * <p>
	 * The last nibble represents the sign. We start by filling this one then
	 * continue right to left, filling all other nibbles.
	 * <p>
	 * Decimal separators are not materialized for packed decimals.
	 * 
	 * @param decimal        the java decimal
	 * @param signed         a signed decimal
	 * @param totalDigits    total number of digits (including fraction digits)
	 * @param fractionDigits scale
	 * @return a COBOL packed decimal
	 */
	public byte[] toCobol(BigDecimal decimal, boolean signed, int totalDigits, int fractionDigits) {
		String s = decimal.setScale(fractionDigits, RoundingMode.DOWN).unscaledValue().abs().toString();
		int bytesLen = BytesLenUtils.packedDecimalByteLen(totalDigits);
		byte[] buffer = new byte[bytesLen];
		int j = bytesLen - 1;
		buffer[j] = (byte) (decimal.signum() == -1 ? negativeSignNibbleValue
				: (signed ? positiveSignNibbleValue : unspecifiedSignNibbleValue));
		boolean high = true;
		for (int i = s.length() - 1; i >= 0 && j >= 0; i--) {
			int nibble = lowNibble(s.charAt(i));
			if (high) {
				buffer[j] |= nibble << 4;
				high = false;
				j--;
			} else {
				buffer[j] = (byte) nibble;
				high = true;
			}
		}
		return buffer;
	}

	/**
	 * Check if the string is a sequence of host space characters only
	 * 
	 * @param s the string to check
	 * @return true if the string is a sequence of host space characters only
	 */
	private boolean isAllSpaces(String s) {
		return s.replace(spaceCharValue, "").length() == 0;
	}

	/**
	 * Extract the left half byte from a byte.
	 * 
	 * @param c the byte
	 * @return a half byte
	 */
	private int highNibble(int c) {
		return ((byte) c >> 4) & 0x0f;
	}

	/**
	 * Extract the right half byte from a byte.
	 * 
	 * @param c the byte
	 * @return a half byte
	 */
	private int lowNibble(int c) {
		return (byte) c & 0x0f;
	}

}
