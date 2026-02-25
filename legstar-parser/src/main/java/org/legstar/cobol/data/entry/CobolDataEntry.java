package org.legstar.cobol.data.entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a COBOL for z/OS data description entry.
 * <p>
 * Can be a record, a primitive type or a condition type.
 * <p>
 * COBOL for z/OS options that are not supported are:
 * <ul>
 * <li>Type declarations</li>
 * <li>DYNAMIC LENGTH clause</li>
 * <li>TYPE clause</li>
 * <li>TYPEDEF clause</li>
 * <li>VOLATILE clause</li>
 * </ul>
 * 
 * @see <a href=
 *      "https://www.ibm.com/docs/en/cobol-zos/latest?topic=division-data-data-description-entry">Cobol
 *      for z/OS data entry</a>
 * @param levelNumber         Level in the hierarchy this element was parsed
 *                            from
 * @param cobolName           Cobol element name
 * @param redefines           Cobol element being redefined
 * @param blankWhenZero       Blank when zero clause
 * @param external            External clause
 * @param global              Global clause
 * @param groupUsageNational  Group usage national clause
 * @param justifiedRight      Data aligned at rightmost character position
 * @param picture             General characteristics and editing requirements
 * @param signLeading         True if sign in leading byte
 * @param signSeparate        Sign is in separate byte (not overpunched)
 * @param sync                Synchronized on natural boundary in storage
 * @param minOccurs           Minimum number of occurrences
 * @param maxOccurs           Maximum number of occurrences
 * @param dependingOn         Cobol element giving array actual size
 * @param indexes             Indexed by clause
 * @param ascendingKeys       Array items ordering ascending keys
 * @param descendingKeys      Array items ordering descending keys
 * @param usage               Cobol usage
 * @param value               Cobol value clause
 * @param dateFormat          Cobol date format clause
 * @param children            List of direct children
 * @param renamesSubject      Single rename subject
 * @param renamesSubjectRange Range of rename subjects
 * @param conditionLiterals   One or more literal values
 * @param conditionRanges     One or more ranges of literal values
 * @param srceLine            Line number in the original source file
 */
public record CobolDataEntry(int levelNumber, //
		String cobolName, //
		String redefines, //
		boolean blankWhenZero, //
		boolean external, //
		boolean global, //
		boolean groupUsageNational, //
		boolean justifiedRight, //
		String picture, //
		boolean signLeading, //
		boolean signSeparate, //
		boolean sync, //
		int minOccurs, //
		int maxOccurs, //
		String dependingOn, //
		List<String> indexes, //
		List<String> ascendingKeys, //
		List<String> descendingKeys, //
		CobolDataEntryUsage usage, //
		String value, //
		String dateFormat, //
		List<CobolDataEntry> children, //
		String renamesSubject, //
		Range renamesSubjectRange, //
		List<String> conditionLiterals, //
		List<Range> conditionRanges, //
		int srceLine //
) {

	/**
	 * Builder for a cobol data entry.
	 */
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
		private boolean signLeading;
		private boolean signSeparate;
		private boolean sync;
		private int minOccurs = -1; // default is unset
		private int maxOccurs = -1; // default is unlimited
		private String dependingOn;
		private List<String> indexes = new ArrayList<>();
		private List<String> ascendingKeys = new ArrayList<>();
		private List<String> descendingKeys = new ArrayList<>();
		private CobolDataEntryUsage usage;
		private String value;
		private String dateFormat;
		private int srceLine;
		private List<CobolDataEntry> children = new ArrayList<>();
		private String renamesSubject;
		private Range renamesSubjectRange;
		private List<String> conditionLiterals = new ArrayList<>();
		private List<Range> conditionRanges = new ArrayList<>();

		/**
		 * Create a builder
		 */
		public Builder() {
		}

		/**
		 * Set the level number
		 * 
		 * @param levelNumber level number
		 * @return this builder
		 */
		public Builder levelNumber(int levelNumber) {
			this.levelNumber = levelNumber;
			return this;
		}

		/**
		 * Set the cobol element name
		 * 
		 * @param cobolName cobol element name
		 * @return this builder
		 */
		public Builder cobolName(String cobolName) {
			this.cobolName = cobolName;
			return this;
		}

		/**
		 * Set the cobol element being redefined
		 * 
		 * @param redefines cobol element being redefined
		 * @return this builder
		 */
		public Builder redefines(String redefines) {
			this.redefines = redefines;
			return this;
		}

		/**
		 * Set the blank when zero clause
		 * 
		 * @param blankWhenZero blank when zero clause
		 * @return this builder
		 */
		public Builder blankWhenZero(boolean blankWhenZero) {
			this.blankWhenZero = blankWhenZero;
			return this;
		}

		/**
		 * Set the external clause
		 * 
		 * @param external external clause
		 * @return this builder
		 */
		public Builder external(boolean external) {
			this.external = external;
			return this;
		}

		/**
		 * Set the global clause
		 * 
		 * @param global global clause
		 * @return this builder
		 */
		public Builder global(boolean global) {
			this.global = global;
			return this;
		}

		/**
		 * Set the group usage national clause
		 * 
		 * @param groupUsageNational group usage national clause
		 * @return this builder
		 */
		public Builder groupUsageNational(boolean groupUsageNational) {
			this.groupUsageNational = groupUsageNational;
			return this;
		}

		/**
		 * Set the data aligned at rightmost character position
		 * 
		 * @param justifiedRight data aligned at rightmost character position
		 * @return this builder
		 */
		public Builder justifiedRight(boolean justifiedRight) {
			this.justifiedRight = justifiedRight;
			return this;
		}

		/**
		 * Set the general characteristics and editing requirements
		 * 
		 * @param picture general characteristics and editing requirements
		 * @return this builder
		 */
		public Builder picture(String picture) {
			this.picture = picture;
			return this;
		}

		/**
		 * Set whether sign is in leading byte
		 * 
		 * @param signLeading true if sign in leading byte
		 * @return this builder
		 */
		public Builder signLeading(boolean signLeading) {
			this.signLeading = signLeading;
			return this;
		}

		/**
		 * Set whether sign is in separate byte (not overpunched)
		 * 
		 * @param signSeparate true if sign is in separate byte (not overpunched)
		 * @return this builder
		 */
		public Builder signSeparate(boolean signSeparate) {
			this.signSeparate = signSeparate;
			return this;
		}

		/**
		 * Set whether synchronized on natural boundary in storage
		 * 
		 * @param sync true if synchronized on natural boundary in storage
		 * @return this builder
		 */
		public Builder sync(boolean sync) {
			this.sync = sync;
			return this;
		}

		/**
		 * Set the minimum number of occurrences
		 * 
		 * @param minOccurs minimum number of occurrences
		 * @return this builder
		 */
		public Builder minOccurs(int minOccurs) {
			this.minOccurs = minOccurs;
			return this;
		}

		/**
		 * Set the maximum number of occurrences
		 * 
		 * @param maxOccurs maximum number of occurrences
		 * @return this builder
		 */
		public Builder maxOccurs(int maxOccurs) {
			this.maxOccurs = maxOccurs;
			return this;
		}

		/**
		 * Set the cobol element giving array actual size
		 * 
		 * @param dependingOn cobol element giving array actual size
		 * @return this builder
		 */
		public Builder dependingOn(String dependingOn) {
			this.dependingOn = dependingOn;
			return this;
		}

		/**
		 * Set the indexed by clause
		 * 
		 * @param indexes indexed by clause
		 * @return this builder
		 */
		public Builder indexes(List<String> indexes) {
			this.indexes = indexes;
			return this;
		}

		/**
		 * Set array items ordering ascending keys
		 * 
		 * @param ascendingKeys array items ordering ascending keys
		 * @return this builder
		 */
		public Builder ascendingKeys(List<String> ascendingKeys) {
			this.ascendingKeys = ascendingKeys;
			return this;
		}

		/**
		 * Set array items ordering descending keys
		 * 
		 * @param descendingKeys array items ordering descending keys
		 * @return this builder
		 */
		public Builder descendingKeys(List<String> descendingKeys) {
			this.descendingKeys = descendingKeys;
			return this;
		}

		/**
		 * Set the cobol usage
		 * 
		 * @param usage cobol usage
		 * @return this builder
		 */
		public Builder usage(CobolDataEntryUsage usage) {
			this.usage = usage;
			return this;
		}

		/**
		 * Set the cobol value clause
		 * 
		 * @param value cobol value clause
		 * @return this builder
		 */
		public Builder value(String value) {
			this.value = value;
			return this;
		}

		/**
		 * Set the cobol date format clause
		 * 
		 * @param dateFormat cobol date format clause
		 * @return this builder
		 */
		public Builder dateFormat(String dateFormat) {
			this.dateFormat = dateFormat;
			return this;
		}

		/**
		 * Add a direct child
		 * 
		 * @param child a direct childn
		 * @return this builder
		 */
		public Builder addChild(CobolDataEntry child) {
			children.add(child);
			return this;
		}

		/**
		 * Set the single rename subject
		 * 
		 * @param renamesSubject single rename subject
		 * @return this builder
		 */
		public Builder renamesSubject(String renamesSubject) {
			this.renamesSubject = renamesSubject;
			return this;
		}

		/**
		 * Add a range of rename subjects
		 * 
		 * @param renamesSubjectRange range of rename subjects
		 * @return this builder
		 */
		public Builder renamesSubjectRange(Range renamesSubjectRange) {
			this.renamesSubjectRange = renamesSubjectRange;
			return this;
		}

		/**
		 * Add a single condition literal value
		 * 
		 * @param conditionLiteral a single condition literal value
		 * @return this builder
		 */
		public Builder addConditionLiteral(String conditionLiteral) {
			this.conditionLiterals.add(conditionLiteral);
			return this;
		}

		/**
		 * Add a range of condition literal values
		 * 
		 * @param conditionRange a range of condition literal values
		 * @return this builder
		 */
		public Builder addConditionRange(Range conditionRange) {
			this.conditionRanges.add(conditionRange);
			return this;
		}

		/**
		 * Set the line number in the original source file
		 * 
		 * @param srceLine line number in the original source file
		 * @return this builder
		 */
		public Builder srceLine(int srceLine) {
			this.srceLine = srceLine;
			return this;
		}

		/**
		 * Build a CobolDataEntry
		 * 
		 * @return a new CobolDataEntry
		 */
		public CobolDataEntry build() {
			return new CobolDataEntry(levelNumber, cobolName, redefines, blankWhenZero, external, global,
					groupUsageNational, justifiedRight, picture, signLeading, signSeparate, sync, minOccurs, maxOccurs,
					dependingOn, indexes, ascendingKeys, descendingKeys, usage, value, dateFormat, children,
					renamesSubject, renamesSubjectRange, conditionLiterals, conditionRanges, srceLine);
		}

		/**
		 * Is the level being built an ancestor of levelNumber2
		 * 
		 * @param levelNumber2 the other level to compare to
		 * @return true if the current level is an ancestor of levelNumber2
		 */
		public boolean isHigherThan(int levelNumber2) {
			return this.levelNumber < levelNumber2;
		}

	}

	/**
	 * Represents all data items between from and to
	 * 
	 * @param from range start
	 * @param to   range end
	 */
	public static record Range(String from, String to) {
	}

	/**
	 * If maxOccurs is set, this is an array.
	 * <p>
	 * We also treat an array cobol data entries that are optional. Optionality in
	 * cobol is specified using a minOccurs or zero and maxOccurs of 1.
	 * 
	 * @return true if this is an array
	 */
	public boolean isArray() {
		return (maxOccurs > 1 || (maxOccurs == 1 && minOccurs == 0));
	}

	/**
	 * Is this a variable size array. Such arrays have their actual dimension given
	 * by a preceding data entry.
	 * 
	 * @return true if this a variable size array
	 */
	public boolean isVariableSizeArray() {
		return isArray() //
				&& minOccurs != maxOccurs //
				&& (dependingOn != null && !dependingOn.isBlank());
	}

	/**
	 * Is this a group item. Group items are not conditions, renames or primitive
	 * types.
	 * <p>
	 * Can't just check for children because in cobol groups are allowed not to have
	 * children.
	 * 
	 * @return true if this is a group item
	 */
	public boolean isGroup() {
		if (isConditionName() || isRenames()) {
			return false;
		}
		return usage == null && picture == null;
	}

	/**
	 * Does this item redefines a preceding one.
	 * 
	 * @return true if this item redefines another
	 */
	public boolean isRedefinition() {
		return redefines() != null && !redefines().isBlank();
	}

	/**
	 * Is this a condition name.
	 * 
	 * @return true if this item redefines another
	 */
	public boolean isConditionName() {
		return levelNumber() == 88;
	}

	/**
	 * is this an alias for a preceding item.
	 * 
	 * @return true if this item renames other items
	 */
	public boolean isRenames() {
		return levelNumber() == 66;
	}

	@Override
	public String toString() {
		return toString(0);
	}

	/**
	 * Returns a string representation of the cobol data entry.
	 * 
	 * @param indent number of indentation spaces
	 * @return a string representation of the cobol data entry
	 */
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
