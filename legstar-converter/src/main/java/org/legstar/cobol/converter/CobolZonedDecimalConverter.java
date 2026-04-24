package org.legstar.cobol.converter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.legstar.cobol.io.CobolInputStream;
import org.legstar.cobol.utils.BytesLenUtils;

/**
 * Converts between a Cobol zoned decimal and a java BigDecimal or String.
 */
public class CobolZonedDecimalConverter {

	private final int hostMinusSign;

	private final int hostPlusSign;

	private final int positiveSignNibbleValue;

	private final int negativeSignNibbleValue;

	private final int unspecifiedSignNibbleValue;

	/**
	 * Build a cobol zoned converter.
	 * 
	 * @param hostMinusSign              the minus sign cobol character encoding
	 * @param hostPlusSign               the plus sign cobol character encoding
	 * @param positiveSignNibbleValue    positive sign nibble value
	 * @param negativeSignNibbleValue    negative sign nibble value
	 * @param unspecifiedSignNibbleValue unspecified sign nibble value
	 */
	public CobolZonedDecimalConverter(int hostMinusSign, int hostPlusSign, int positiveSignNibbleValue,
			int negativeSignNibbleValue, int unspecifiedSignNibbleValue) {
		this.hostMinusSign = hostMinusSign;
		this.hostPlusSign = hostPlusSign;
		this.positiveSignNibbleValue = positiveSignNibbleValue;
		this.negativeSignNibbleValue = negativeSignNibbleValue;
		this.unspecifiedSignNibbleValue = unspecifiedSignNibbleValue;
	}

	/**
	 * Convert a COBOL zoned decimal.
	 * 
	 * @param <T>            the target java type
	 * @param is             the cobol input data
	 * @param totalDigits    the total number of digits
	 * @param fractionDigits the number of fraction digits
	 * @param signLeading    whether the sign is leading (trailing otherwise)
	 * @param signSeparate   whether the sign is separate (overpunched otherwise)
	 * @param targetClass    the target java class
	 * @return the converted java value
	 */
	@SuppressWarnings(value = "unchecked")
	public <T> T convert(CobolInputStream is, int totalDigits, int fractionDigits, boolean signLeading,
			boolean signSeparate, Class<T> targetClass) {
		if (targetClass.equals(String.class)) {
			return (T) toString(is, totalDigits, fractionDigits, signLeading, signSeparate);
		} else if (targetClass.equals(BigDecimal.class)) {
			return (T) toBigDecimal(is, totalDigits, fractionDigits, signLeading, signSeparate);
		} else {
			throw new CobolBeanConverterException("Unsupported target class " + targetClass);
		}
	}

	/**
	 * Convert to a BigDecimal and the get the plain string value from that.
	 * 
	 * @param is             host bytes to convert
	 * @param totalDigits    total number of digits (including fraction digits)
	 * @param fractionDigits the number of fraction digits
	 * @param signLeading    whether the sign is leading (trailing otherwise)
	 * @param signSeparate   whether the sign is separate (overpunched otherwise)
	 * @return a string representation of the decimal
	 */
	public String toString(CobolInputStream is, int totalDigits, int fractionDigits, boolean signLeading,
			boolean signSeparate) {
		BigDecimal dec = toBigDecimal(is, totalDigits, fractionDigits, signLeading, signSeparate);
		return dec == null ? null : dec.toPlainString();
	}

	/**
	 * Zoned decimals have one byte per digit plus one more byte if the sign is
	 * separate.
	 * <p>
	 * Each byte's low nibble (rightmost 4 bits) gives the actual numerical digit.
	 * <p>
	 * When sign is not separate, the leading or trailing byte has high nibble
	 * (leftmost 4 bits) that indicates the signum.
	 * 
	 * @param is             host bytes to convert
	 * @param totalDigits    total number of digits (including fraction digits)
	 * @param fractionDigits scale
	 * @param signLeading    true if sign is leading (otherwise sign is trailing)
	 * @param signSeparate   true if sign is separate (otherwise it is overpunched
	 *                       as the high nibble of the leading or trailing byte)
	 * @return a BigDecimal
	 */
	public BigDecimal toBigDecimal(CobolInputStream is, int totalDigits, int fractionDigits, boolean signLeading,
			boolean signSeparate) {

		try {
			StringBuilder sb = new StringBuilder();
			int signum = 0;
			int bytesLen = BytesLenUtils.zonedDecimalByteLen(totalDigits, signSeparate);

			for (int i = 0; i < bytesLen; i++) {
				int c = is.read();
				if (c == -1) {
					throw new CobolBeanConverterEOFException();
				}
				if ((i == 0 && signLeading) || (i == (bytesLen - 1) && !signLeading)) {
					if (signSeparate) {
						signum = c == hostMinusSign ? -1 : 1;
						continue;
					} else {
						signum = overpunchedSignum((byte) c);
					}
				}
				int ln = c & 0x0f;
				if (ln < 0 || ln > 9) {
					ln = 0;
				}
				sb.append(ln);
			}

			if (sb.isEmpty()) {
				return BigDecimal.valueOf(0);
			}
			sb.insert(0, signum == -1 ? "-" : "");
			return new BigDecimal(sb.toString()).scaleByPowerOfTen(-fractionDigits);
		} catch (IOException | NumberFormatException e) {
			throw new CobolBeanConverterException(e);
		}

	}

	/**
	 * Convert a java decimal to a COBOL zoned decimal (PIC 9 without editing)
	 * <p>
	 * Each digit from the java decimal becomes a COBOL digit.
	 * <p>
	 * The sign can be separate or overpunched. Furthermore the sign can be leading
	 * or trailing.
	 * <p>
	 * The low nibble of a java digit (in ASCII) is the same as the low nibble of
	 * the COBOL digit (in EBCDIC).
	 * <p>
	 * The high nibble is different though. In COBOL it is either F or C/D for
	 * overpunched signs (affects first or last byte when sign is not separate)
	 * <p>
	 * If there are not enough digits in the java decimal we left pad with zeroes.
	 * <p>
	 * If there are more digits in the java decimal then expected by COBOL, we
	 * silently truncate the most significant digits.
	 * <p>
	 * Decimal separators are not materialized for zoned decimals.
	 * 
	 * @param decimal        the java decimal value
	 * @param signed         is the zoned decimal signed
	 * @param totalDigits    total number of digits (including fraction digits)
	 * @param fractionDigits scale
	 * @param signLeading    true if sign is leading (otherwise sign is trailing)
	 * @param signSeparate   true if sign is separate (otherwise it is overpunched
	 *                       as the high nibble of the leading or trailing byte)
	 * @return a COBOL zoned decimal
	 */
	public byte[] toCobol(BigDecimal decimal, boolean signed, int totalDigits, int fractionDigits, boolean signLeading,
			boolean signSeparate) {

		String s = decimal.setScale(fractionDigits, RoundingMode.DOWN).unscaledValue().abs().toString();

		if (s.length() < totalDigits) {
			s = "0".repeat(totalDigits - s.length()) + s;
		} else if (s.length() > totalDigits) {
			s = s.substring(s.length() - totalDigits);
		}

		int hostSign = decimal.signum() == -1 ? hostMinusSign : hostPlusSign;
		int signNibble = decimal.signum() == -1 ? negativeSignNibbleValue
				: (signed ? positiveSignNibbleValue : unspecifiedSignNibbleValue);

		int bytesLen = BytesLenUtils.zonedDecimalByteLen(totalDigits, signSeparate);
		byte[] buffer = new byte[bytesLen];
		int j = bytesLen - 1;
		if (signSeparate && !signLeading) {
			buffer[j] = (byte) hostSign;
			j--;
		}
		for (int i = s.length() - 1; i >= 0; i--) {
			int highNibble = !signSeparate && ((i == s.length() - 1 && !signLeading) || (i == 0 && signLeading))
					? signNibble
					: unspecifiedSignNibbleValue;
			buffer[j] = (byte) (highNibble << 4 | s.charAt(i) & 0x0f);
			j--;
		}
		if (signSeparate && signLeading) {
			buffer[j] = (byte) hostSign;
			j--;
		}
		return buffer;

	}

	/**
	 * When sign is overpunched, it occupies the highest nibble of the byte.
	 * <p>
	 * For example: -1234 is stored as F1F2F3D4 when sign is trailing and not
	 * separate
	 * 
	 * @param b the byte to check for sign nibble
	 * @return -1 if sign is negative, 1 otherwise
	 */
	private int overpunchedSignum(byte b) {
		int hn = (b >> 4) & 0x0f;
		if (hn == positiveSignNibbleValue || hn == negativeSignNibbleValue) {
			return hn == negativeSignNibbleValue ? -1 : 1;
		} else {
			return 0;
		}
	}

}
