package org.legstar.cobol.converter;

import java.math.BigDecimal;

import org.legstar.cobol.annotation.CobolBinaryNumber;
import org.legstar.cobol.annotation.CobolDouble;
import org.legstar.cobol.annotation.CobolFloat;
import org.legstar.cobol.annotation.CobolPackedDecimal;
import org.legstar.cobol.annotation.CobolString;
import org.legstar.cobol.annotation.CobolZonedDecimal;

/**
 * Convert primitive types.
 */
public class CobolPrimitiveConverter {

	/**
	 * Converts cobol PIC X to String
	 */
	private final CobolStringConverter stringConverter;

	/**
	 * Converts cobol COMP to short/integer/long
	 */
	private final CobolBinaryNumberConverter binaryNumberConverter;

	/**
	 * Converts cobol PIC 99V99 to BigDecimal
	 */
	private final CobolZonedDecimalConverter zonedDecimalConverter;

	/**
	 * Converts cobol COMP-3 to BigDecimal
	 */
	private final CobolPackedDecimalConverter packedDecimalConverter;

	/**
	 * Converts cobol COMP-1 to float
	 */
	private final CobolFloatConverter floatConverter;

	/**
	 * Converts cobol COMP-2 to double
	 */
	private final CobolDoubleConverter doubleConverter;

	public CobolPrimitiveConverter() {
		this(CobolBeanConverterConfig.ebcdic());
	}

	public CobolPrimitiveConverter(CobolConverterConfig config) {
		stringConverter = new CobolStringConverter(config.hostCharsetName(), //
				config.truncateHostStringsTrailingSpaces(), //
				config.rightPadCobolAlphanumWithSpaces(), //
				config.hostSpaceCharCode());
		binaryNumberConverter = new CobolBinaryNumberConverter();
		zonedDecimalConverter = new CobolZonedDecimalConverter(config.hostMinusSign(), //
				config.hostPlusSign(), //
				config.hostSpaceCharCode(), //
				config.positiveSignNibbleValue(), //
				config.negativeSignNibbleValue(), //
				config.unspecifiedSignNibbleValue());
		packedDecimalConverter = new CobolPackedDecimalConverter(config.hostSpaceCharCode(), //
				config.positiveSignNibbleValue(), //
				config.negativeSignNibbleValue(), //
				config.unspecifiedSignNibbleValue());
		floatConverter = new CobolFloatConverter();
		doubleConverter = new CobolDoubleConverter();
	}

	public byte[] toAlphanum(CobolString cobolString, Object value) {
		return stringConverter.toCobol(value.toString(), cobolString.charNum());
	}

	public byte[] toBinaryNumber(CobolBinaryNumber cobolBinaryNumber, Object value) {
		if (value instanceof Number) {
			return binaryNumberConverter.toCobol((Number) value, cobolBinaryNumber.totalDigits());
		} else {
			throw new CobolPrimitiveConverterException("Unsupported input type " + value.getClass().getName());
		}
	}

	public byte[] toZonedDecimal(CobolZonedDecimal cobolZonedDecimal, Object value) {
		if (value instanceof BigDecimal) {
			return zonedDecimalConverter.toCobol((BigDecimal) value, cobolZonedDecimal.signed(),
					cobolZonedDecimal.totalDigits(), cobolZonedDecimal.fractionDigits(),
					cobolZonedDecimal.signLeading(), cobolZonedDecimal.signSeparate(),
					cobolZonedDecimal.blankWhenZero());
		} else {
			return toZonedDecimal(cobolZonedDecimal, new BigDecimal(value.toString()));
		}
	}

	public byte[] toPackedDecimal(CobolPackedDecimal cobolPackedDecimal, Object value) {
		if (value instanceof BigDecimal) {
			return packedDecimalConverter.toCobol((BigDecimal) value, cobolPackedDecimal.signed(),
					cobolPackedDecimal.totalDigits(), cobolPackedDecimal.fractionDigits());
		} else {
			return toPackedDecimal(cobolPackedDecimal, new BigDecimal(value.toString()));
		}
	}

	public byte[] toComp_1(CobolFloat cobolFloat, Object value) {
		if (value instanceof Number) {
			return floatConverter.toCobol(((Number) value).floatValue());
		} else {
			throw new CobolPrimitiveConverterException("Unsupported input type " + value.getClass().getName());
		}
	}

	public byte[] toComp_2(CobolDouble cobolDouble, Object value) {
		if (value instanceof Number) {
			return doubleConverter.toCobol(((Number) value).doubleValue());
		} else {
			throw new CobolPrimitiveConverterException("Unsupported input type " + value.getClass().getName());
		}
	}
}
