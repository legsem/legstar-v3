package org.legstar.cobol.converter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Converts between a Cobol alphanumeric literal and a java String.
 */
public class CobolStringConverter {

	private final String hostCharsetName;

	private final boolean truncateHostStringsTrailingSpaces;

	/**
	 * Build a cobol string converter.
	 * 
	 * @param hostCharsetName                   the cobol character set
	 * @param truncateHostStringsTrailingSpaces true if strings should be right
	 *                                          truncated
	 */
	public CobolStringConverter(String hostCharsetName, boolean truncateHostStringsTrailingSpaces) {
		this.hostCharsetName = hostCharsetName;
		this.truncateHostStringsTrailingSpaces = truncateHostStringsTrailingSpaces;
	}

	/**
	 * Convert a COBOL alphanumeric.
	 * 
	 * @param <T>         the target java type
	 * @param is          the cobol input data
	 * @param charNum     the number of characters
	 * @param targetClass the target java class
	 * @return the converted java value
	 */
	@SuppressWarnings("unchecked")
	public <T> T convert(CobolInputStream is, int charNum, Class<T> targetClass) {
		if (targetClass.equals(String.class)) {
			return (T) toString(is, charNum);
		} else {
			throw new CobolBeanConverterException("Unsupported target class " + targetClass);
		}
	}

	/**
	 * Converts a number of bytes from the input stream into a String.
	 * <p>
	 * Host low-values and high-values are discarded so the result string may be
	 * shorter than the host data item.
	 * <p>
	 * Trailing spaces are removed if requested.
	 * 
	 * @param is      the host bytes
	 * @param charNum the maximum number of characters for the result string
	 * @return a string
	 */
	public String toString(CobolInputStream is, int charNum) {
		try {
			byte[] buffer = new byte[charNum];
			int j = 0;
			for (int i = 0; i < charNum; i++) {
				int c = is.read();
				if (c == -1) {
					if (i == 0) {
						throw new CobolBeanConverterEOFException();
					} else {
						break;
					}
				} else if (c == 0 || c == 0xFF) {
					continue;
				} else {
					buffer[j++] = (byte) c;
				}
			}
			String res = new String(buffer, 0, j, hostCharsetName);
			return truncateHostStringsTrailingSpaces ? res.stripTrailing() : res;
		} catch (UnsupportedEncodingException e) {
			throw new CobolBeanConverterException(e);
		} catch (IOException e) {
			throw new CobolBeanConverterException(e);
		}
	}

}
