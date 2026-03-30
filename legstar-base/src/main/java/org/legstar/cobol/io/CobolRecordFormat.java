package org.legstar.cobol.io;

/**
 * RECFM formats that we support {@link #FB} {@link #V} {@link #VB}
 */
public enum CobolRecordFormat {
	/** Fixed blocked */
	FB,
	/** Variable */
	V,
	/** Variable blocked */
	VB
}