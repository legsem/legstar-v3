package org.legstar.cobol.copybook.parser;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A wrapper around ParseException whose sole purpose is to adjust the location
 * so that the line number corresponds to the original source before cleanup.
 */
public class CopybookParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final Pattern LOCATION_PATTERN = Pattern.compile(" at (.*?):(\\d+):(\\d+)");

	private final Function<Integer, Integer> lineNumberProvider;

	public CopybookParserException(Throwable e, Function<Integer, Integer> lineNumberProvider) {
		super(e);
		this.lineNumberProvider = lineNumberProvider;
	}

	@Override
	public String getMessage() {
		String msg = super.getMessage();
		if (msg == null) {
			return msg;
		}
		Matcher m = LOCATION_PATTERN.matcher(msg);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String s = " at $1:" + getRealLineNumber(m.group(2)) + ":$3";
			m.appendReplacement(sb, s);
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private int getRealLineNumber(String line) {
		return lineNumberProvider.apply(Integer.parseInt(line));
	}

}
