package org.legstar.cobol.data.entry;

/**
 * Cobol data entry usages that are supported.
 */
public enum CobolDataEntryUsage {
	/** COMP. */
	BINARY,
	/** COMP-1. */
	FLOAT,
	/** COMP-2. */
	DOUBLE_FLOAT,
	/** COMP-3. */
	PACKED_DECIMAL,
	/** COMP-5. */
	NATIVE_BINARY,
	/** DISPLAY. */
	DISPLAY,
	/** DBCS. */
	DISPLAY1,
	/** INDEX. */
	INDEX,
	/** UTF-16. */
	NATIONAL,
	/** Pointer. */
	POINTER,
	/** Procedure pointer. */
	PROCEDURE_POINTER,
	/** Function pointer. */
	FUNCTION_POINTER;

	/** An character string. */
	public static final String COBOL_DISPLAY = "DISPLAY";
	/** An double byte character string. */
	public static final String COBOL_DISPLAY_1 = "DISPLAY-1";
	/** A UTF16-BE character string. */
	public static final String COBOL_NATIONAL = "NATIONAL";
	/** A binary numeric. */
	public static final String COBOL_BINARY = "BINARY";
	/** A native binary numeric. */
	public static final String COBOL_COMP_5 = "COMP-5";
	/** A packed numeric. */
	public static final String COBOL_PACKED_DECIMAL = "COMP-3";
	/** A single float. */
	public static final String COBOL_COMP_1 = "COMP-1";
	/** A double float. */
	public static final String COBOL_COMP_2 = "COMP-2";
	/** An index. */
	public static final String COBOL_INDEX = "INDEX";
	/** A pointer. */
	public static final String COBOL_POINTER = "POINTER";
	/** A pointer to a procedure. */
	public static final String COBOL_PROCEDURE_POINTER = "PROCEDURE-POINTER";
	/** A pointer to a function. */
	public static final String COBOL_FUNCTION_POINTER = "FUNCTION-POINTER";

	/**
	 * Cobol usage clause (as a COBOL string)
	 * 
	 * @return the Cobol usage clause (as a COBOL string)
	 */
	public String toCobolString() {
		switch (this) {
		case BINARY:
			return COBOL_BINARY;
		case FLOAT:
			return COBOL_COMP_1;
		case DOUBLE_FLOAT:
			return COBOL_COMP_2;
		case PACKED_DECIMAL:
			return COBOL_PACKED_DECIMAL;
		case NATIVE_BINARY:
			return COBOL_COMP_5;
		case DISPLAY:
			return COBOL_DISPLAY;
		case DISPLAY1:
			return COBOL_DISPLAY_1;
		case INDEX:
			return COBOL_INDEX;
		case NATIONAL:
			return COBOL_NATIONAL;
		case POINTER:
			return COBOL_POINTER;
		case PROCEDURE_POINTER:
			return COBOL_PROCEDURE_POINTER;
		case FUNCTION_POINTER:
			return COBOL_FUNCTION_POINTER;
		default:
			throw new IllegalArgumentException("Unknown Enum usage: " + name());
		}
	}

	/**
	 * Get the java enumeration corresponding to a COBOL usage string.
	 * 
	 * @param cobolUsage the COBOL usage string
	 * @return the java enumeration
	 */
	public static CobolDataEntryUsage fromCobolString(String cobolUsage) {
		if (cobolUsage == null) {
			return null;
		}
		if (cobolUsage.equals(COBOL_BINARY)) {
			return BINARY;
		} else if (cobolUsage.equals("COMP")) {
			return BINARY;
		} else if (cobolUsage.equals("COMPUTATIONAL")) {
			return BINARY;
		} else if (cobolUsage.equals("COMP-4")) {
			return BINARY;
		} else if (cobolUsage.equals("COMPUTATIONAL-4")) {
			return BINARY;
		} else if (cobolUsage.equals(COBOL_COMP_1)) {
			return FLOAT;
		} else if (cobolUsage.equals(COBOL_COMP_2)) {
			return DOUBLE_FLOAT;
		} else if (cobolUsage.equals(COBOL_PACKED_DECIMAL)) {
			return PACKED_DECIMAL;
		} else if (cobolUsage.equals("PACKED-DECIMAL")) {
			return PACKED_DECIMAL;
		} else if (cobolUsage.equals("COMPUTATIONAL-3")) {
			return PACKED_DECIMAL;
		} else if (cobolUsage.equals(COBOL_COMP_5)) {
			return NATIVE_BINARY;
		} else if (cobolUsage.equals("COMPUTATIONAL-5")) {
			return NATIVE_BINARY;
		} else if (cobolUsage.equals(COBOL_DISPLAY)) {
			return DISPLAY;
		} else if (cobolUsage.equals(COBOL_DISPLAY_1)) {
			return DISPLAY1;
		} else if (cobolUsage.equals(COBOL_INDEX)) {
			return INDEX;
		} else if (cobolUsage.equals(COBOL_NATIONAL)) {
			return NATIONAL;
		} else if (cobolUsage.equals(COBOL_POINTER)) {
			return POINTER;
		} else if (cobolUsage.equals(COBOL_PROCEDURE_POINTER)) {
			return PROCEDURE_POINTER;
		} else if (cobolUsage.equals(COBOL_FUNCTION_POINTER)) {
			return FUNCTION_POINTER;
		} else {
			throw new IllegalArgumentException("Unknown COBOL usage: " + cobolUsage);
		}

	}

}
