package com.legsem.legstar.copybook;

import java.util.ArrayList;
import java.util.List;

/**
 * COBOL data description entry can be a record, a primitive type or a condition type.
 * <p>
 * See https://www.ibm.com/docs/en/cobol-zos/latest?topic=division-data-data-description-entry
 * <p>
 * Options that are not supported are:
 * <ul>
 * <li>Type declarations</li>
 * <li>DYNAMIC LENGTH clause</li>
 * <li>TYPE clause</li>
 * <li>TYPEDEF clause</li>
 * <li>VOLATILE clause</li>
 * </ul>
 */
public record CobolDataEntry(int levelNumber, // Level in the hierarchy this element was parsed from.
		String cobolName, // Cobol element name
		String redefines, // Cobol element being redefined
		boolean blankWhenZero, // Blank when zero clause
		boolean external, // External clause
		boolean global, // Global clause
		boolean groupUsageNational, // Group usage national clause
		boolean justifiedRight, // Data aligned at rightmost character position
		String picture, // General characteristics and editing requirements
		boolean signed, // True if COBOL data item has a SIGN clause
		boolean signLeading, // True if sign in leading byte
		boolean signSeparate, // Sign is in separate byte (not overpunched)
		boolean sync, // Synchronized on natural boundary in storage
		int minOccurs, // Minimum number of occurrences
		int maxOccurs, // Maximum number of occurrences
		String dependingOn, // Cobol element giving array actual size
		List<String> indexes, // Indexed by clause
		List<String> ascendingKeys, // Array items ordering ascending keys
		List<String> descendingKeys, // Array items ordering descending keys
		CobolUsage usage, // Cobol usage
		String value, // Cobol value clause
		String dateFormat, // Cobol date format clause
		List<CobolDataEntry> children, // List of direct children
		String renamesSubject, // Single rename subject
		Range renamesSubjectRange, // Range of rename subjects
		List<String> conditionLiterals, // One or more literal values
		List<Range> conditionRanges, // One or more ranges of literal values
		int srceLine // Line number in the original source file
) {

	public static class Builder {
		private int levelNumber;
		private String cobolName = "FILLER";
		private String redefines;
		private boolean blankWhenZero;
		private boolean external;
		private boolean global;
		private boolean groupUsageNational;
		private boolean justifiedRight;
		private String picture;
		private boolean signed;
		private boolean signLeading;
		private boolean signSeparate;
		private boolean sync;
		private int minOccurs = -1; // default is unset
		private int maxOccurs = -1; // default is unlimited
		private String dependingOn;
		private List<String> indexes = new ArrayList<>();
		private List<String> ascendingKeys = new ArrayList<>();
		private List<String> descendingKeys = new ArrayList<>();
		private CobolUsage usage;
		private String value;
		private String dateFormat;
		private int srceLine;
		private List<CobolDataEntry> children = new ArrayList<>();
		private String renamesSubject;
		private Range renamesSubjectRange;
		private List<String> conditionLiterals = new ArrayList<>();
		private List<Range> conditionRanges = new ArrayList<>();

		public Builder levelNumber(int levelNumber) {
			this.levelNumber = levelNumber;
			return this;
		}

		public Builder cobolName(String cobolName) {
			this.cobolName = cobolName;
			return this;
		}

		public Builder redefines(String redefines) {
			this.redefines = redefines;
			return this;
		}

		public Builder blankWhenZero(boolean blankWhenZero) {
			this.blankWhenZero = blankWhenZero;
			return this;
		}

		public Builder external(boolean external) {
			this.external = external;
			return this;
		}

		public Builder global(boolean global) {
			this.global = global;
			return this;
		}

		public Builder groupUsageNational(boolean groupUsageNational) {
			this.groupUsageNational = groupUsageNational;
			return this;
		}

		public Builder justifiedRight(boolean justifiedRight) {
			this.justifiedRight = justifiedRight;
			return this;
		}

		public Builder picture(String picture) {
			this.picture = picture;
			return this;
		}

		public Builder signed(boolean signed) {
			this.signed = signed;
			return this;
		}

		public Builder signLeading(boolean signLeading) {
			this.signLeading = signLeading;
			return this;
		}

		public Builder signSeparate(boolean signSeparate) {
			this.signSeparate = signSeparate;
			return this;
		}

		public Builder sync(boolean sync) {
			this.sync = sync;
			return this;
		}

		public Builder minOccurs(int minOccurs) {
			this.minOccurs = minOccurs;
			return this;
		}

		public Builder maxOccurs(int maxOccurs) {
			this.maxOccurs = maxOccurs;
			return this;
		}

		public Builder dependingOn(String dependingOn) {
			this.dependingOn = dependingOn;
			return this;
		}

		public Builder indexes(List<String> indexes) {
			this.indexes = indexes;
			return this;
		}

		public Builder ascendingKeys(List<String> ascendingKeys) {
			this.ascendingKeys = ascendingKeys;
			return this;
		}

		public Builder descendingKeys(List<String> descendingKeys) {
			this.descendingKeys = descendingKeys;
			return this;
		}

		public Builder usage(CobolUsage usage) {
			this.usage = usage;
			return this;
		}

		public Builder value(String value) {
			this.value = value;
			return this;
		}

		public Builder dateFormat(String dateFormat) {
			this.dateFormat = dateFormat;
			return this;
		}

		public Builder addChild(CobolDataEntry child) {
			children.add(child);
			return this;
		}

		public Builder renamesSubject(String renamesSubject) {
			this.renamesSubject = renamesSubject;
			return this;
		}

		public Builder renamesSubjectRange(Range renamesSubjectRange) {
			this.renamesSubjectRange = renamesSubjectRange;
			return this;
		}

		public Builder addConditionLiteral(String conditionLiteral) {
			this.conditionLiterals.add(conditionLiteral);
			return this;
		}

		public Builder addConditionRange(Range conditionRange) {
			this.conditionRanges.add(conditionRange);
			return this;
		}

		public Builder srceLine(int srceLine) {
			this.srceLine = srceLine;
			return this;
		}

		public CobolDataEntry build() {
			return new CobolDataEntry(levelNumber, cobolName, redefines, blankWhenZero, external, global,
					groupUsageNational, justifiedRight, picture, signed, signLeading, signSeparate, sync, minOccurs,
					maxOccurs, dependingOn, indexes, ascendingKeys, descendingKeys, usage, value, dateFormat,
					children, renamesSubject, renamesSubjectRange, conditionLiterals, conditionRanges, srceLine);
		}

		public boolean isHigherThan(int levelNumber2) {
			return this.levelNumber < levelNumber2;
		}
		
	}

	/**
	 * Represents all data items between from and to
	 */
	public static record Range(String from, String to) {}

	/**
	 * If maxOccurs is set, this is an array.
	 * <p>
	 * We also treat an array cobol data entries that are optional. Optionality in
	 * cobol is specified using a minOccurs or zero and maxOccurs of 1.
	 */
	public boolean isArray() {
		return (maxOccurs > 1 || (maxOccurs == 1 && minOccurs == 0));
	}

	/**
	 * @return true if this is a variable size array. Such arrays have their actual
	 *         dimension given by a preceding data entry.
	 */
	public boolean isVariableSizeArray() {
		return isArray() //
				&& minOccurs != maxOccurs //
				&& (dependingOn != null && !dependingOn.isBlank());
	}

    @Override
	public String toString() {
		return toString(0);
	}

	public String toString(int indent) {
		StringBuilder sb = new StringBuilder();
		sb.append(" ".repeat(indent));
		sb.append("CobolDataEntry [levelNumber=");
		sb.append(levelNumber);
		sb.append(", ");
		if (cobolName != null) {
			sb.append("cobolName=");
			sb.append(cobolName);
			sb.append(", ");
		}
		if (redefines != null) {
			sb.append("redefines=");
			sb.append(redefines);
			sb.append(", ");
		}
		if (blankWhenZero) {
			sb.append("blankWhenZero=");
			sb.append(blankWhenZero);
			sb.append(", ");
		}
		if (external) {
			sb.append("external=");
			sb.append(external);
			sb.append(", ");
		}
		if (global) {
			sb.append("global=");
			sb.append(global);
			sb.append(", ");
		}
		if (groupUsageNational) {
			sb.append("groupUsageNational=");
			sb.append(groupUsageNational);
			sb.append(", ");
		}
		if (justifiedRight) {
			sb.append("justifiedRight=");
			sb.append(justifiedRight);
			sb.append(", ");
		}
		if (picture != null) {
			sb.append("picture=");
			sb.append(picture);
			sb.append(", ");
		}
		if (signed) {
			sb.append("signed=");
			sb.append(signed);
			sb.append(", ");
		}
		if (signLeading) {
			sb.append("signLeading=");
			sb.append(signLeading);
			sb.append(", ");
		}
		if (signSeparate) {
			sb.append("signSeparate=");
			sb.append(signSeparate);
			sb.append(", ");
		}
		if (sync) {
			sb.append("sync=");
			sb.append(sync);
			sb.append(", ");
		}
		if (minOccurs != -1) {
			sb.append("minOccurs=");
			sb.append(minOccurs);
			sb.append(", ");
		}
		if (maxOccurs != -1) {
			sb.append("maxOccurs=");
			sb.append(maxOccurs);
			sb.append(", ");
		}
		if (dependingOn != null) {
			sb.append("dependingOn=");
			sb.append(dependingOn);
			sb.append(", ");
		}
		if (indexes != null && !indexes.isEmpty()) {
			sb.append("indexes=");
			sb.append(indexes);
			sb.append(", ");
		}
		if (ascendingKeys != null && !ascendingKeys.isEmpty()) {
			sb.append("ascendingKeys=");
			sb.append(ascendingKeys);
			sb.append(", ");
		}
		if (descendingKeys != null && !descendingKeys.isEmpty()) {
			sb.append("descendingKeys=");
			sb.append(descendingKeys);
			sb.append(", ");
		}
		if (usage != null) {
			sb.append("usage=");
			sb.append(usage);
			sb.append(", ");
		}
		if (value != null) {
			sb.append("value=");
			sb.append(value);
			sb.append(", ");
		}
		if (dateFormat != null) {
			sb.append("dateFormat=");
			sb.append(dateFormat);
			sb.append(", ");
		}
		if (children != null && !children.isEmpty()) {
			sb.append("children=[\n");
			children.forEach(c -> {
				sb.append(c.toString(indent + 2));
				sb.append("\n");
			});
			sb.append(" ".repeat(indent));
			sb.append("], ");
		}
		if (renamesSubject != null) {
			sb.append("renamesSubject=");
			sb.append(renamesSubject);
			sb.append(", ");
		}
		if (renamesSubjectRange != null) {
			sb.append("renamesSubjectRange=");
			sb.append(renamesSubjectRange);
			sb.append(", ");
		}
		if (conditionLiterals != null && !conditionLiterals.isEmpty()) {
			sb.append("conditionLiterals=");
			sb.append(conditionLiterals);
			sb.append(", ");
		}
		if (conditionRanges != null && !conditionRanges.isEmpty()) {
			sb.append("conditionRanges=");
			sb.append(conditionRanges);
			sb.append(", ");
		}
		sb.append("srceLine=");
		sb.append(srceLine);
		sb.append("]");
		return sb.toString();
	}
	
}
