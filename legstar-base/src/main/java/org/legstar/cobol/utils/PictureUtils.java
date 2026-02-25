package org.legstar.cobol.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for cobol PICTURE clause.
 * 
 * TODO CR/DB and N/G probably not handled correctly
 */
public class PictureUtils {

	private static final Pattern COMP_NUMERIC = Pattern.compile("(S?)([0-9\\(\\)]*?)(V([0-9\\(\\)]*?))?",
			Pattern.CASE_INSENSITIVE);

	private static final Pattern COMP_NUMERIC_PART = Pattern.compile("9(9+|\\((\\d+)\\))?", Pattern.CASE_INSENSITIVE);

	private static final Pattern ALPHA_NUMERIC = Pattern.compile("[AXB0-9CRDPS/GN$ZE+-.,V\\(\\)\\*]+",
			Pattern.CASE_INSENSITIVE);

	private static final Pattern ALPHA_PART = Pattern.compile("[AXZ9]([AXZ9]+|\\((\\d+)\\))?",
			Pattern.CASE_INSENSITIVE);

	/**
	 * Utility class.
	 */
	private PictureUtils() {
	}

	/**
	 * Computational numerics have picture clauses like 9(4) or S9(5)V99.
	 * 
	 * @param pic the cobol picture clause
	 * @return the computational numeric properties
	 */
	public static CompNumeric compNumeric(String pic) {
		Matcher m = COMP_NUMERIC.matcher(pic);
		if (m.matches()) {
			boolean signed = "S".equals(m.group(1));
			int fractionDigits = m.group(3) == null ? 0 : digitCount(m.group(4));
			int totalDigits = digitCount(m.group(2)) + fractionDigits;
			return new CompNumeric(signed, totalDigits, fractionDigits);
		} else {
			throw new IllegalArgumentException("Picture clause " + pic + " is not a computational picture");
		}
	}

	/**
	 * Aphanumerics have picture clauses like X(4) or +9(5).99.
	 * <p>
	 * All characters contribute to the length of the alphanumeric apart from the V
	 * character which stands for a virtual decimal delimiter.
	 * 
	 * @param pic the cobol picture clause
	 * @return the alphanumeric properties
	 */
	public static AlphaNumeric alphaNumeric(String pic) {
		if (!isAlphaNumeric(pic)) {
			throw new IllegalArgumentException("Picture clause " + pic + " is not an alphanumeric picture");
		}
		Matcher m = ALPHA_PART.matcher(pic);
		int charNum = 0;
		int pos = 0;
		while (m.find()) {
			int start = m.start();
			charNum += start - pos;
			int end = m.end();
			charNum += charCount(pic.substring(start, end));
			pos = end;
		}
		charNum += pic.length() - pos;
		if (pic.contains("V") || pic.contains("v")) {
			charNum--;
		}
		return new AlphaNumeric(charNum);
	};

	/**
	 * Is this picture that of a computational numeric.
	 * 
	 * @param pic the picture clause
	 * @return true if this picture corresponds to a computational numeric
	 */
	public static boolean isCompNumeric(String pic) {
		if (pic == null) {
			return false;
		}
		Matcher m = COMP_NUMERIC.matcher(pic);
		return m.matches();
	}

	/**
	 * Is this picture that of an alphanumeric.
	 * 
	 * @param pic the picture clause
	 * @return true if this picture corresponds to an alphanumeric
	 */
	public static boolean isAlphaNumeric(String pic) {
		if (pic == null) {
			return false;
		} else if (isCompNumeric(pic)) {
			return false;
		}
		Matcher m = ALPHA_NUMERIC.matcher(pic);
		return m.matches();
	}

	/**
	 * Calculate the number of digits given a numeric description like 9999 or 9(4)
	 * 
	 * @param numberPart a numeric picture part
	 * @return number of digits
	 */
	public static int digitCount(String numberPart) {
		if (numberPart == null || numberPart.isBlank()) {
			return 0;
		}
		Matcher m = COMP_NUMERIC_PART.matcher(numberPart);
		if (m.matches()) {
			if (m.group(1) == null) {
				return 1;
			} else if (m.group(2) == null) {
				return 1 + m.group(1).length();
			} else {
				return Integer.parseInt(m.group(2));
			}
		} else {
			throw new IllegalArgumentException("Picture part " + numberPart + " is not a computational picture part");
		}
	}

	/**
	 * Calculate the number of characters given an alphanumeric like XXXX or X(4)
	 * 
	 * @param alphaPart the picture part
	 * @return the number of characters
	 */
	public static int charCount(String alphaPart) {
		if (alphaPart == null) {
			return 0;
		}
		Matcher m = ALPHA_PART.matcher(alphaPart);
		if (m.matches()) {
			if (m.group(1) == null) {
				return 1;
			} else if (m.group(2) == null) {
				return 1 + m.group(1).length();
			} else {
				return Integer.parseInt(m.group(2));
			}
		} else {
			throw new IllegalArgumentException("Picture part " + alphaPart + " is not an alphanumeric picture part");
		}
	}

	/**
	 * A computational numeric properties
	 * 
	 * @param signed         is this a signed numeric
	 * @param totalDigits    total number of digits (including fraction digits)
	 * @param fractionDigits number of fraction digits
	 */
	public static record CompNumeric(boolean signed, int totalDigits, int fractionDigits) {
	};

	/**
	 * An alphanumeric properties
	 * 
	 * @param charNum number of characters
	 */
	public static record AlphaNumeric(int charNum) {
	}

}
