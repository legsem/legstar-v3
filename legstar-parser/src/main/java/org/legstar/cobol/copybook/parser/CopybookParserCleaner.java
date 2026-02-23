package org.legstar.cobol.copybook.parser;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * In order to reduce the lexer/parser grammar complexity, this class will
 * remove all unnecessary characters from the original source. This way, the
 * lexer will be presented with a simpler source that only contains data
 * division entries.
 * <p>
 * This allows users to submit complete COBOL programs or fragments of COBOL
 * programs with non data description statements to the parser without the need
 * to add grammar rules for all these cases.
 */
public class CopybookParserCleaner {

	private static final Pattern IDENTIFICATION_DIVISION = Pattern.compile("IDENTIFICATION\\sDIVISION(\\s)*\\.",
			Pattern.CASE_INSENSITIVE);

	private static final Pattern PROCEDURE_DIVISION = Pattern.compile("PROCEDURE\\sDIVISION(\\s)*\\.",
			Pattern.CASE_INSENSITIVE);

	private static final Pattern WORKING_STORAGE_SECTION = Pattern.compile("WORKING-STORAGE\\sSECTION(\\s)*\\.",
			Pattern.CASE_INSENSITIVE);

	private static final Pattern LOCAL_STORAGE_SECTION = Pattern.compile("LOCAL-STORAGE\\sSECTION(\\s)*\\.",
			Pattern.CASE_INSENSITIVE);

	private static final Pattern LINKAGE_SECTION = Pattern.compile("LINKAGE\\sSECTION(\\s)*\\.",
			Pattern.CASE_INSENSITIVE);

	private static final Pattern COPY_DIRECTIVE = Pattern.compile("\\sCOPY\s",
			Pattern.CASE_INSENSITIVE);

	/**
	 * List of compiler directives (they can be period delimited but are guaranteed
	 * to be alone on a line).
	 */
	public static final List<String> COMPILER_DIRECTIVES = List.of("EJECT", "SKIP", "SKIP1", "SKIP2", "SKIP3");

	private final CopybookParserConfig config;

	public CopybookParserCleaner() {
		this(new CopybookParserConfig());
	}

	public CopybookParserCleaner(CopybookParserConfig config) {
		this.config = config;
	}

	public Result clean(Reader reader) {
		Result res = new Result();
		try (LineNumberReader r = new LineNumberReader(reader)) {
			boolean dataLine = true;
			String l;
			while ((l = r.readLine()) != null) {
				if (isCompilerDirective(l)) {
					continue;
				}
				String cl = removeExtraneousCharacters(l);
				if (cl.isBlank() || isComment(cl) || isDebug(cl) || isCopy(cl)) {
					continue;
				}
				if (IDENTIFICATION_DIVISION.matcher(cl).find()) {
					dataLine = false;
					res.clear();
					continue;
				}
				if (PROCEDURE_DIVISION.matcher(cl).find()) {
					break;
				}
				if (WORKING_STORAGE_SECTION.matcher(cl).find() || LOCAL_STORAGE_SECTION.matcher(cl).find()
						|| LINKAGE_SECTION.matcher(cl).find()) {
					dataLine = true;
					continue;
				}
				if (dataLine) {
					if (isAlphanumContinuationLiteral(cl)) {
						aggregateAlphanumContinuationLiteral(cl, res);
					} else {
						res.add(replaceLongSeparators(cl), r.getLineNumber());
					}
				}
			}
			return res;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Returns true if this line contains a compiler directive.
	 */
	private boolean isCompilerDirective(String l) {
		String[] tokens = l.trim().split("[\\s\\.]+");
		return tokens.length == 1 && COMPILER_DIRECTIVES.contains(tokens[0].toUpperCase());
	}

	/**
	 * Regular comments have * or / in the indicator area (column 7 for fixed
	 * format, Column 1 for free format).
	 * <p>
	 * There is also support for floating comments which start with *>
	 */
	private boolean isComment(String l) {
		if (l.trim().startsWith("*>")) {
			return true;
		}
		char c = getIndicator(l);
		return c == '*' || c == '/' || c == '$';
	}

	/**
	 * Debug lines should be ignored.
	 */
	private boolean isDebug(String l) {
		char c = getIndicator(l);
		return c == 'd' || c == 'D';
	}

	/**
	 * COPY of an external source is not supported.
	 */
	private boolean isCopy(String cl) {
		return COPY_DIRECTIVE.matcher(cl).find();
	}

	/**
	 * Debug lines should be ignored.
	 */
	private boolean isAlphanumContinuationLiteral(String l) {
		char c = getIndicator(l);
		return c == '-';
	}

	/**
	 * Retrieve the character in the indicator area (if any).
	 */
	private char getIndicator(String l) {
		int pos = config.isFreeCodeFormat() ? 0 : config.getStartColumn() - 1;
		return l.length() > pos ? l.charAt(pos) : ' ';
	}

	/**
	 * In fixed code format any character outside of the program-text area (AreaA
	 * plus AreaB) must be ignored.
	 */
	private String removeExtraneousCharacters(String l) {
		return config.isFreeCodeFormat() ? l : stripTrailing(blankSeqNumArea(l));
	}

	/**
	 * Here we blank out characters before the start column instead of deleting them
	 * in order to preserve column numbering.
	 */
	private String blankSeqNumArea(String l) {
		int len = Math.min(l.length(), config.getStartColumn() - 1);
		return " ".repeat(len) + l.substring(len);
	}

	/**
	 * All characters past the end column are deleted.
	 */
	private String stripTrailing(String l) {
		int len = Math.min(l.length(), config.getEndColumn());
		return l.substring(0, len).stripTrailing();
	}

	/**
	 * Cobol for zos allows commas and semi-colon separators. They can be replaced
	 * with space.
	 * <p>
	 * We keep an extra space to preserve column numbering.
	 */
	private String replaceLongSeparators(String l) {
		return l.replaceAll("[,;]\\s(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", "  ");
	}

	private void aggregateAlphanumContinuationLiteral(String l, Result res) {
		int last = res.cleanLines.size() - 1;
		if (last < 0) {
			return;
		}
		String continuedLine = res.cleanLines.get(last);
		String continuation = l.substring(config.getStartColumn()).stripLeading();
		if (continuation.startsWith("\"") || continuation.startsWith("'")) {
			res.cleanLines.set(last, continuedLine + continuation.substring(1));
		}
		
	}

    /**
     * Holds the result of the cleaning process along with utility methods.
     */
    public static class Result {

		final List<String> cleanLines = new ArrayList<>();

		/**
		 * For each clean line, keep track of the original line number (before
		 * cleaning)
		 */
		final List<Integer> originalLineNumbers = new ArrayList<>();

		void add(String line, int lineNumber) {
			cleanLines.add(line);
			originalLineNumbers.add(lineNumber);
		}

		public void clear() {
			cleanLines.clear();
			originalLineNumbers.clear();
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < cleanLines.size(); i++) {
				sb.append(String.format("%03d", originalLineNumbers.get(i)));
				sb.append(" ");
				sb.append(cleanLines.get(i));
				sb.append("\n");
			}
			return sb.toString();
		}

		public CharSequence toCobolSource() {
			return String.join("\n", cleanLines);
		}
		
		public int originalLineNumber(int i) {
			return originalLineNumbers.get(i - 1);
		}

	}

}
