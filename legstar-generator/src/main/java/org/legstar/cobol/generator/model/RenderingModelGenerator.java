package org.legstar.cobol.generator.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.legstar.cobol.data.entry.CobolDataEntry;
import org.legstar.cobol.type.utils.PictureUtils;

/**
 * TODO missing Float & Double
 * <p>
 * TODO National & DBCS (DISPLAY-1) not handled
 * <p>
 * TODO For occurs depending on relationships, the cobol name could be qualified
 * - this is not handled
 * <p>
 * TODO For redefines relationships, the cobol name could be qualified - this is
 * not handled
 */
public class RenderingModelGenerator {

	/**
	 * Cobol name of items whose value gives the size of some variable size array.
	 */
	private Set<String> odoObjects;

	/**
	 * Cobol name of items which are redefined by at least one sibling.
	 */
	private Set<String> redefObjects;

	public RenderingModel generate(String source, CobolDataEntry dataEntry, String targetPackagePrefix) {
		if (dataEntry.isConditionName() || dataEntry.isRenames()) {
			throw new IllegalArgumentException("This type of entry is not supported: " + dataEntry);
		}
		odoObjects = odoObjects(dataEntry);
		redefObjects = redefObjects(dataEntry);
		StringBuilder sb = new StringBuilder();
		if (targetPackagePrefix != null && !targetPackagePrefix.isBlank()) {
			sb.append(targetPackagePrefix);
			sb.append(".");
		}
		sb.append(source.toLowerCase());
		return new RenderingModel(sb.toString(), generate(dataEntry, fieldName(dataEntry.cobolName())));
	}

	/**
	 * Generate a rendering model specific to a particular Cobol data entry.
	 * <p>
	 * Might require analyzing the picture clause and usage clause.
	 */
	private RenderingItem generate(CobolDataEntry dataEntry, String fieldName) {
		if (dataEntry.isGroup()) {
			return generateGroup(dataEntry, fieldName);
		}
		if (dataEntry.usage() != null) {
			switch (dataEntry.usage()) {
			case BINARY:
				return generateBinaryNumber(dataEntry, fieldName);
			case PACKED_DECIMAL:
				return generatePackedDecimal(dataEntry, fieldName);
			case DISPLAY:
				break;
			case DISPLAY1:
				break;
			case DOUBLE_FLOAT:
				break;
			case FLOAT:
				break;
			case NATIONAL:
				break;
			case NATIVE_BINARY:
				return generateBinaryNumber(dataEntry, fieldName);
			case FUNCTION_POINTER:
			case INDEX:
			case POINTER:
			case PROCEDURE_POINTER:
				throw new IllegalArgumentException("Usage " + dataEntry.usage() + " is not supported");
			default:
				break;
			}
		}
		if (PictureUtils.isCompNumeric(dataEntry.picture())) {
			return generateZonedDecimal(dataEntry, fieldName);
		} else if (PictureUtils.isAlphaNumeric(dataEntry.picture())) {
			return generateString(dataEntry, fieldName);
		} else {
			throw new IllegalArgumentException("Unable to recognize " + dataEntry);
		}
	}

	/**
	 * A group item.
	 * <p>
	 * Level 88 and 66 are ignored.
	 * <p>
	 * Group children which are redefined along with the definition object are
	 * grouped in a RenderingChoice item.
	 */
	private RenderingGroup generateGroup(CobolDataEntry dataEntry, String fieldName) {
		List<RenderingItem> children = new ArrayList<>();
		ChoiceBuilder choiceBuilder = null;
		for (CobolDataEntry child : dataEntry.children()) {
			String childFieldName = uniqueFieldName(child.cobolName(), children);
			if (child.isConditionName() || child.isRenames()) {
				continue;
			} else if (isRedefObject(child)) {
				choiceBuilder = new ChoiceBuilder( //
						child.cobolName(), //
						new ArrayList<>(), //
						generateArray(child), //
						childFieldName + "Choice" //
				);
				choiceBuilder.add(generate(child, childFieldName));
			} else if (child.isRedefinition()) {
				choiceBuilder.add(generate(child, childFieldName));
			} else {
				if (choiceBuilder != null) {
					children.add(choiceBuilder.build());
					choiceBuilder = null;
				}
				children.add(generate(child, childFieldName));
			}
		}
		if (choiceBuilder != null) {
			children.add(choiceBuilder.build());
		}
		return new RenderingGroup(dataEntry.cobolName(), children, generateArray(dataEntry), fieldName);
	}

	/**
	 * Accumulates choice alternatives as they are encountered.
	 */
	static record ChoiceBuilder(String choiceCobolName, List<RenderingItem> choiceAlternatives,
			RenderingArray choiceArray, String choiceFieldName) {

		void add(RenderingItem alternative) {
			choiceAlternatives().add(alternative);
		}

		RenderingChoice build() {
			return new RenderingChoice(choiceCobolName(), choiceAlternatives(), choiceArray(), choiceFieldName());
		}

	}

	/**
	 * An alphanumeric item
	 */
	private RenderingItem generateString(CobolDataEntry dataEntry, String fieldName) {
		PictureUtils.AlphaNumeric n = PictureUtils.alphaNumeric(dataEntry.picture());
		return new RenderingString(dataEntry.cobolName(), n.charNum(), generateArray(dataEntry), fieldName);
	}

	/**
	 * A COMP item
	 */
	private RenderingBinaryNumber generateBinaryNumber(CobolDataEntry dataEntry, String fieldName) {
		PictureUtils.CompNumeric n = PictureUtils.compNumeric(dataEntry.picture());
		return new RenderingBinaryNumber(dataEntry.cobolName(), n.signed(), n.totalDigits(), isOdoObject(dataEntry),
				generateArray(dataEntry), fieldName);
	}

	/**
	 * A DISPLAY zoned decimal item
	 */
	private RenderingZonedDecimal generateZonedDecimal(CobolDataEntry dataEntry, String fieldName) {
		PictureUtils.CompNumeric n = PictureUtils.compNumeric(dataEntry.picture());
		return new RenderingZonedDecimal(dataEntry.cobolName(), n.totalDigits(), n.fractionDigits(),
				dataEntry.signLeading(), dataEntry.signSeparate(), isOdoObject(dataEntry), generateArray(dataEntry),
				fieldName);
	}

	/**
	 * A COMP-3 item
	 */
	private RenderingPackedDecimal generatePackedDecimal(CobolDataEntry dataEntry, String fieldName) {
		PictureUtils.CompNumeric n = PictureUtils.compNumeric(dataEntry.picture());
		return new RenderingPackedDecimal(dataEntry.cobolName(), n.signed(), n.totalDigits(), n.fractionDigits(),
				isOdoObject(dataEntry), generateArray(dataEntry), fieldName);
	}

	/**
	 * A fixed or Variable size array
	 */
	private RenderingArray generateArray(CobolDataEntry dataEntry) {
		if (dataEntry.isArray()) {
			return new RenderingArray(dataEntry.minOccurs(), dataEntry.maxOccurs(), dataEntry.dependingOn());
		} else {
			return null;
		}
	}

	/**
	 * Identify all cobol names corresponding to items whose value gives the size of
	 * some variable size array.
	 */
	private Set<String> odoObjects(CobolDataEntry dataEntry) {
		Set<String> odoObjects = new HashSet<>();
		addOdoObjects(odoObjects, dataEntry);
		return odoObjects;
	}

	/**
	 * Variable size arrays has a dependingOn property which is the cobol name of an
	 * item whose value gives the actual size of this array
	 */
	private void addOdoObjects(Set<String> odoObjects, CobolDataEntry dataEntry) {
		if (dataEntry.isVariableSizeArray()) {
			odoObjects.add(dataEntry.dependingOn());
		}
		if (dataEntry.isGroup()) {
			dataEntry.children().forEach(child -> addOdoObjects(odoObjects, child));
		}
	}

	/**
	 * Does this data entry give the size of some variable size array?
	 */
	private boolean isOdoObject(CobolDataEntry dataEntry) {
		return odoObjects.contains(dataEntry.cobolName());
	}

	/**
	 * Identify all cobol names corresponding to items that are redefined by at
	 * least one sibling.
	 */
	private Set<String> redefObjects(CobolDataEntry dataEntry) {
		Set<String> redefined = new HashSet<>();
		addRedefined(redefined, dataEntry);
		return redefined;
	}

	/**
	 * Items redefining a sibling form a Choice model.
	 */
	private void addRedefined(Set<String> redefined, CobolDataEntry dataEntry) {
		if (dataEntry.isRedefinition()) {
			redefined.add(dataEntry.redefines());
		}
		if (dataEntry.isGroup()) {
			dataEntry.children().forEach(child -> addRedefined(redefined, child));
		}
	}

	/**
	 * Is this a redefined data entry
	 */
	private boolean isRedefObject(CobolDataEntry dataEntry) {
		return redefObjects.contains(dataEntry.cobolName());
	}

	/**
	 * Form a java unique field name given a set of prior siblings.
	 */
	private String uniqueFieldName(String cobolName, List<RenderingItem> children) {
		String fieldName = fieldName(cobolName);
		if (children.stream().anyMatch(c -> c.fieldName().equals(fieldName))) {
			return fieldName + "_" + children.size();
		} else {
			return fieldName;
		}
	}

	/**
	 * Form a java field name from a cobol name
	 */
	private String fieldName(String cobolName) {
		String[] parts = cobolName.split("-");
		StringBuilder sb = new StringBuilder();
		Arrays.stream(parts).forEach(p -> {
			if (sb.isEmpty()) {
				sb.append(Character.toLowerCase(p.charAt(0)));
			} else {
				sb.append(Character.toUpperCase(p.charAt(0)));
			}
			sb.append(p.substring(1).toLowerCase());
		});
		return sb.toString();
	}

}
