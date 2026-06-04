package org.legstar.cobol.converter;

import java.io.InputStream;
import java.math.BigDecimal;

import org.legstar.cobol.annotation.CobolBinaryNumber;
import org.legstar.cobol.annotation.CobolDouble;
import org.legstar.cobol.annotation.CobolFloat;
import org.legstar.cobol.annotation.CobolPackedDecimal;
import org.legstar.cobol.annotation.CobolString;
import org.legstar.cobol.annotation.CobolZonedDecimal;

/**
 * Convert Cobol-annotated primitive types to and from Java.
 */
public class CobolPrimitiveConverter {

	/**
	 * Converts Cobol PIC X to String
	 */
	private final CobolStringConverter stringConverter;

	/**
	 * Converts Cobol COMP to short/integer/long
	 */
	private final CobolBinaryNumberConverter binaryNumberConverter;

	/**
	 * Converts Cobol PIC 99V99 to BigDecimal
	 */
	private final CobolZonedDecimalConverter zonedDecimalConverter;

	/**
	 * Converts Cobol COMP-3 to BigDecimal
	 */
	private final CobolPackedDecimalConverter packedDecimalConverter;

	/**
	 * Converts Cobol COMP-1 to float
	 */
	private final CobolFloatConverter floatConverter;

	/**
	 * Converts Cobol COMP-2 to double
	 */
	private final CobolDoubleConverter doubleConverter;

	/**
	 * Convert primitive types with default configuration.
	 */
	public CobolPrimitiveConverter() {
		this(CobolBeanConverterConfig.ebcdic());
	}

	/**
	 * Convert primitive types.
	 * 
	 * @param config converter configuration
	 */
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

	// -----------------------------------------------------------------------------
	// To Java
	// -----------------------------------------------------------------------------
	/**
	 * Convert a COBOL alphanumeric to java String.
	 * 
	 * @param <Z>         the target java type
	 * @param is          cobol data stream
	 * @param cobolString cobol string annotation
	 * @param objectClass the target java class
	 * @return the converted java value
	 */
	public <Z> Z toJava(InputStream is, CobolString cobolString, Class<Z> objectClass) {
		return (Z) stringConverter.toJava(is, cobolString.charNum(), objectClass);
	}

	/**
	 * Convert a COBOL binary number (COMP, COMP-5).
	 * 
	 * @param <Z>         the target java type
	 * @param is          cobol data stream
	 * @param cobolBinary cobol binary number annotation
	 * @param objectClass the target java class
	 * @return the converted java value
	 */
	public <Z> Z toJava(InputStream is, CobolBinaryNumber cobolBinary, Class<Z> objectClass) {
		return (Z) binaryNumberConverter.toJava(is, cobolBinary.signed(), cobolBinary.totalDigits(), objectClass);
	}

	/**
	 * Convert a COBOL zoned decimal.
	 * 
	 * @param <Z>               the target java type
	 * @param is                cobol data stream
	 * @param cobolZonedDecimal cobol zoned decimal annotation
	 * @param objectClass       the target java class
	 * @return the converted java value
	 */
	public <Z> Z toJava(InputStream is, CobolZonedDecimal cobolZonedDecimal, Class<Z> objectClass) {
		return (Z) zonedDecimalConverter.toJava(is, cobolZonedDecimal.totalDigits(), cobolZonedDecimal.fractionDigits(),
				cobolZonedDecimal.signLeading(), cobolZonedDecimal.signSeparate(), objectClass);
	}

	/**
	 * Convert a COBOL packed decimal (COMP-3).
	 * 
	 * @param <Z>                the target java type
	 * @param is                 cobol data stream
	 * @param cobolPackedDecimal cobol packed decimal annotation
	 * @param objectClass        the target java class
	 * @return the converted java value
	 */
	public <Z> Z toJava(InputStream is, CobolPackedDecimal cobolPackedDecimal, Class<Z> objectClass) {
		return (Z) packedDecimalConverter.toJava(is, cobolPackedDecimal.signed(), cobolPackedDecimal.totalDigits(),
				cobolPackedDecimal.fractionDigits(), objectClass);
	}

	/**
	 * Convert a COBOL float (COMP-1).
	 * 
	 * @param <Z>         the target java type
	 * @param is          cobol data stream
	 * @param cobolFloat  cobol float annotation
	 * @param objectClass the target java class
	 * @return the converted java value
	 */
	public <Z> Z toJava(InputStream is, CobolFloat cobolFloat, Class<Z> objectClass) {
		return (Z) floatConverter.toJava(is, objectClass);
	}

	/**
	 * Convert a COBOL double (COMP-2).
	 * 
	 * @param <Z>         the target java type
	 * @param is          cobol data stream
	 * @param cobolDouble cobol double annotation
	 * @param objectClass the target java class
	 * @return the converted java value
	 */
	public <Z> Z toJava(InputStream is, CobolDouble cobolDouble, Class<Z> objectClass) {
		return (Z) doubleConverter.toJava(is, objectClass);
	}

	// -----------------------------------------------------------------------------
	// To Cobol
	// -----------------------------------------------------------------------------
	/**
	 * Convert a java object to a Cobol alphanumeric.
	 * 
	 * @param cobolString the Cobol item description
	 * @param value       the java object
	 * @return a byte array with Cobol data
	 */
	public byte[] toAlphanum(CobolString cobolString, Object value) {
		return stringConverter.toCobol(value.toString(), cobolString.charNum());
	}

	/**
	 * Convert a java object to a Cobol binary number.
	 * 
	 * @param cobolBinaryNumber the Cobol item description
	 * @param value             the java object
	 * @return a byte array with Cobol data
	 * @throws CobolPrimitiveConverterException if value is not a Number
	 */
	public byte[] toBinaryNumber(CobolBinaryNumber cobolBinaryNumber, Object value) {
		if (value instanceof Number) {
			return binaryNumberConverter.toCobol((Number) value, cobolBinaryNumber.totalDigits());
		} else {
			throw new CobolPrimitiveConverterException("Unsupported input type " + value.getClass().getName());
		}
	}

	/**
	 * Convert a java object to a Cobol zoned decimal.
	 * 
	 * @param cobolZonedDecimal the Cobol item description
	 * @param value             the java object
	 * @return a byte array with Cobol data
	 * @throws NumberFormatException if string is passed which is not a valid
	 *                               decimal number
	 */
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

	/**
	 * Convert a java object to a Cobol packed decimal.
	 * 
	 * @param cobolPackedDecimal the Cobol item description
	 * @param value              the java object
	 * @return a byte array with Cobol data
	 * @throws NumberFormatException if string is passed which is not a valid
	 *                               decimal number
	 */
	public byte[] toPackedDecimal(CobolPackedDecimal cobolPackedDecimal, Object value) {
		if (value instanceof BigDecimal) {
			return packedDecimalConverter.toCobol((BigDecimal) value, cobolPackedDecimal.signed(),
					cobolPackedDecimal.totalDigits(), cobolPackedDecimal.fractionDigits());
		} else {
			return toPackedDecimal(cobolPackedDecimal, new BigDecimal(value.toString()));
		}
	}

	/**
	 * Convert a java object to a Cobol COMP-1 float.
	 * 
	 * @param cobolFloat the Cobol item description
	 * @param value      the java object
	 * @return a byte array with Cobol data
	 * @throws CobolPrimitiveConverterException if value is not a Number
	 */
	public byte[] toComp_1(CobolFloat cobolFloat, Object value) {
		if (value instanceof Number) {
			return floatConverter.toCobol(((Number) value).floatValue());
		} else {
			throw new CobolPrimitiveConverterException("Unsupported input type " + value.getClass().getName());
		}
	}

	/**
	 * Convert a java object to a Cobol COMP-2 double.
	 * 
	 * @param cobolDouble the Cobol item description
	 * @param value       the java object
	 * @return a byte array with Cobol data
	 * @throws CobolPrimitiveConverterException if value is not a Number
	 */
	public byte[] toComp_2(CobolDouble cobolDouble, Object value) {
		if (value instanceof Number) {
			return doubleConverter.toCobol(((Number) value).doubleValue());
		} else {
			throw new CobolPrimitiveConverterException("Unsupported input type " + value.getClass().getName());
		}
	}
}
