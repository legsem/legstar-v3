package org.legstar.cobol.type.converter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Converts between a Cobol alphanumeric literal and a java String.
 */
public class CobolConverterString {

	private final CobolConverterConfig config;

	public CobolConverterString(CobolConverterConfig config) {
		this.config = config;
	}

	@SuppressWarnings("unchecked")
	public <T> T convert(CobolConverterInputStream is, int charNum, Class<T> targetClass) {
		if (targetClass.equals(String.class)) {
			return (T) toString(is, charNum);
		} else {
			throw new CobolConverterException("Unsupported target class " + targetClass);
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
	 * @param is the host bytes
	 * @param charNum the maximum number of characters for the result string
	 */
	public String toString(CobolConverterInputStream is, int charNum) {
		try {
			byte[] buffer = new byte[charNum];
			int j = 0;
			for (int i = 0; i < charNum; i++) {
				int c = is.read();
				if (c == -1) {
					break;
				} else if (c == 0 || c == 0xFF) {
					continue;
				} else {
					buffer[j++] = (byte) c;
				}
			}
			String res = new String(buffer, 0, j, config.hostCharsetName());
			return config.truncateHostStringsTrailingSpaces() ? res.stripTrailing() : res;
		} catch (UnsupportedEncodingException e) {
			throw new CobolConverterException(e);
		} catch (IOException e) {
			throw new CobolConverterException(e);
		}
	}

}
