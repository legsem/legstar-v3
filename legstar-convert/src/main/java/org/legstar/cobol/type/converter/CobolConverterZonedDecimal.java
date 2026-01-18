package org.legstar.cobol.type.converter;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * Converts between a Cobol zoned decimal and a java BigDecimal or String.
 * <p>
 * TODO
 * <ul>
 * <li>Missing toShort, toInteger, toLong</li>
 * </ul>
 */
public class CobolConverterZonedDecimal {

	private final CobolConverterConfig config;

	public CobolConverterZonedDecimal(CobolConverterConfig config) {
		this.config = config;
	}

	@SuppressWarnings(value = "unchecked")
	public <T> T convert(InputStream is, int totalDigits, int fractionDigits, boolean signLeading, boolean signSeparate,
			Class<T> targetClass) {
		if (targetClass.equals(String.class)) {
			return (T) toString(is, totalDigits, fractionDigits, signLeading, signSeparate);
		} else if (targetClass.equals(BigDecimal.class)) {
			return (T) toBigDecimal(is, totalDigits, fractionDigits, signLeading, signSeparate);
		} else {
			throw new CobolConverterException("Unsupported target class " + targetClass);
		}
	}

	public String toString(InputStream is, int totalDigits, int fractionDigits, boolean signLeading,
			boolean signSeparate) {
		return toBigDecimal(is, totalDigits, fractionDigits, signLeading, signSeparate).toPlainString();
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
	public BigDecimal toBigDecimal(InputStream is, int totalDigits, int fractionDigits, boolean signLeading,
			boolean signSeparate) {

		try {
			StringBuilder sb = new StringBuilder();
			int signum = 0;
			int bytesLen = totalDigits + (signSeparate ? 1 : 0);

			for (int i = 0; i < bytesLen; i++) {
				int c = is.read();
				if (c == -1) {
					break;
				}
				if ((i == 0 && signLeading) || (i == (bytesLen - 1) && !signLeading)) {
					if (signSeparate) {
						signum = c == config.hostMinusSign() ? -1 : 1;
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

			sb.insert(0, signum == -1 ? "-" : "");
			return new BigDecimal(sb.toString()).scaleByPowerOfTen(-fractionDigits);
		} catch (IOException e) {
			throw new CobolConverterException(e);
		}

	}

	/**
	 * When sign is overpunched, it occupies the highest nibble of the byte.
	 * <p>
	 * For example: -1234 is stored as F1F2F3D4 when sign is trailing and not
	 * separate
	 */
	private int overpunchedSignum(byte b) {
		int hn = (b >> 4) & 0x0f;
		if (hn == config.positiveSignNibbleValue() || hn == config.negativeSignNibbleValue()) {
			return hn == config.negativeSignNibbleValue() ? -1 : 1;
		} else {
			return 0;
		}
	}

}
