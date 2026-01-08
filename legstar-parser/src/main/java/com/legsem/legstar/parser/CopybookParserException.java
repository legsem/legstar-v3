package com.legsem.legstar.parser;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.legsem.legstar.ccparser.ParseException;

/**
 * A wrapper around ParseException whose sole purpose is to adjust the location
 * so that the line number corresponds to the original source before cleanup.
 */
public class CopybookParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final Pattern LOCATION_PATTERN = Pattern.compile(" at (.*?):(\\d+):(\\d+)");

	private final ParseException parseException;

	private final Function<Integer, Integer> lineNumberProvider;

	public CopybookParserException(ParseException parseException, Function<Integer, Integer> lineNumberProvider) {
		this.parseException = parseException;
		this.lineNumberProvider = lineNumberProvider;
	}

	@Override
	public String getMessage() {
		String msg = parseException.getMessage();
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
