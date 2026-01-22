package org.legstar.cobol.generator.model;

import java.util.ArrayList;
import java.util.List;

import org.legstar.cobol.data.entry.CobolDataEntry;
import org.legstar.cobol.generator.utils.PictureUtils;

/**
 * TODO Occurs Depending On not handled
 * TODO missing Float & Double
 * TODO National & DBCS (DISPLAY-1) not handled
 */
public class RenderingModelGenerator {

	public RenderingModel generate(String source, CobolDataEntry dataEntry, String targetPackagePrefix) {
		StringBuilder sb = new StringBuilder();
		if (targetPackagePrefix != null && !targetPackagePrefix.isBlank()) {
			sb.append(targetPackagePrefix);
			sb.append(".");
		}
		sb.append(source.toLowerCase());
		return new RenderingModel(sb.toString(), generate(dataEntry));
	}

	private RenderingItem generate(CobolDataEntry dataEntry) {
		if (dataEntry.isGroup()) {
			return generateGroup(dataEntry);
		}
		if (dataEntry.usage() != null) {
			switch (dataEntry.usage()) {
			case BINARY:
				return generateBinaryNumber(dataEntry);
			case PACKED_DECIMAL:
				return generatePackedDecimal(dataEntry);
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
				return generateBinaryNumber(dataEntry);
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
			return generateZonedDecimal(dataEntry);
		} else if (PictureUtils.isAlphaNumeric(dataEntry.picture())) {
			return generateString(dataEntry);
		} else {
			throw new IllegalArgumentException("Unable to recognize " + dataEntry);
		}
	}

	/**
	 * A group item.
	 */
	private RenderingGroup generateGroup(CobolDataEntry dataEntry) {
		List<RenderingItem> children = new ArrayList<>();
		dataEntry.children().stream().map(this::generate).forEach(children::add);
		return new RenderingGroup(dataEntry.cobolName(), children);
	}

	/**
	 * An alphanumeric item
	 */
	private RenderingItem generateString(CobolDataEntry dataEntry) {
		PictureUtils.AlphaNumeric n = PictureUtils.alphaNumeric(dataEntry.picture());
		return new RenderingString(dataEntry.cobolName(), n.charNum());
	}

	/**
	 * A COMP-1 item
	 */
	private RenderingBinaryNumber generateBinaryNumber(CobolDataEntry dataEntry) {
		PictureUtils.CompNumeric n = PictureUtils.compNumeric(dataEntry.picture());
		// TODO missing mechanism to detect ODO objects
		return new RenderingBinaryNumber(dataEntry.cobolName(), n.signed(), n.totalDigits(), false);
	}

	/**
	 * A DISPLAY zoned decimal item
	 */
	private RenderingZonedDecimal generateZonedDecimal(CobolDataEntry dataEntry) {
		PictureUtils.CompNumeric n = PictureUtils.compNumeric(dataEntry.picture());
		// TODO missing mechanism to detect ODO objects
		return new RenderingZonedDecimal(dataEntry.cobolName(), n.totalDigits(), n.fractionDigits(),
				dataEntry.signLeading(), dataEntry.signSeparate(), false);
	}

	/**
	 * A COMP-3 item
	 */
	private RenderingPackedDecimal generatePackedDecimal(CobolDataEntry dataEntry) {
		PictureUtils.CompNumeric n = PictureUtils.compNumeric(dataEntry.picture());
		// TODO missing mechanism to detect ODO objects
		return new RenderingPackedDecimal(dataEntry.cobolName(), n.signed(), n.totalDigits(), n.fractionDigits(),
				false);
	}

}
