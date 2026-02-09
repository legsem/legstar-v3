package org.legstar.cobol.converter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.legstar.cobol.annotation.CobolArray;
import org.legstar.cobol.annotation.CobolBinaryNumber;
import org.legstar.cobol.annotation.CobolChoice;
import org.legstar.cobol.annotation.CobolDouble;
import org.legstar.cobol.annotation.CobolFloat;
import org.legstar.cobol.annotation.CobolGroup;
import org.legstar.cobol.annotation.CobolItemType;
import org.legstar.cobol.annotation.CobolPackedDecimal;
import org.legstar.cobol.annotation.CobolString;
import org.legstar.cobol.annotation.CobolZonedDecimal;

/**
 * Converts cobol data to a java T instance.
 * <p>
 * The target java bean class must hold cobol annotations as produced by
 * legstar-generator.
 * <p>
 * Thread safe and immutable.
 */
public class CobolBeanConverter<T> {

	/**
	 * The target java bean class (as produced by legstar-generator).
	 */
	private final Class<T> beanClass;

	/**
	 * A custom choice resolution strategy. Null if default strategy applies
	 */
	private final CobolChoiceStrategy<T> choiceStrategy;

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

	public CobolBeanConverter(Class<T> beanClass) {
		this(CobolBeanConverterConfig.ebcdic(), beanClass, null);
	}

	public CobolBeanConverter(CobolBeanConverterConfig config, Class<T> beanClass) {
		this(config, beanClass, null);
	}

	public CobolBeanConverter(CobolBeanConverterConfig config, Class<T> beanClass,
			CobolChoiceStrategy<T> choiceStrategy) {
		this.beanClass = beanClass;
		this.choiceStrategy = choiceStrategy == null ? new CobolConverterDefaultChoiceStrategy() : choiceStrategy;
		stringConverter = new CobolStringConverter(config.hostCharsetName(),
				config.truncateHostStringsTrailingSpaces());
		binaryNumberConverter = new CobolBinaryNumberConverter();
		zonedDecimalConverter = new CobolZonedDecimalConverter(config.hostMinusSign(), config.positiveSignNibbleValue(),
				config.negativeSignNibbleValue());
		packedDecimalConverter = new CobolPackedDecimalConverter(config.hostSpaceCharCode(),
				config.positiveSignNibbleValue(), config.negativeSignNibbleValue(),
				config.unspecifiedSignNibbleValue());
		floatConverter = new CobolFloatConverter();
		doubleConverter = new CobolDoubleConverter();
	}

	/**
	 * Produce a java bean consuming cobol data from the input stream.
	 * <p>
	 * This can be invoked repeatedly over the same input stream.
	 * <p>
	 * 
	 * @param cis the cobol data
	 * @return a java bean instance
	 */
	public T convert(CobolInputStream cis) {
		Context context = new Context(cis, new HashMap<>(), new Stack<>());
		Annotation annotation = getCobolItemType(beanClass);
		return convert(context, annotation, beanClass);
	}

	private <Z> Z convert(Context context, Annotation annotation, Class<Z> objectClass) {
		if (annotation instanceof CobolGroup) {
			return convertGroup(context, (CobolGroup) annotation, objectClass);
		} else if (annotation instanceof CobolChoice) {
			return convertChoice(context, (CobolChoice) annotation, objectClass);
		} else if (annotation instanceof CobolString) {
			return convertString(context, (CobolString) annotation, objectClass);
		} else if (annotation instanceof CobolBinaryNumber) {
			return convertBinary(context, (CobolBinaryNumber) annotation, objectClass);
		} else if (annotation instanceof CobolZonedDecimal) {
			return convertZonedDecimal(context, (CobolZonedDecimal) annotation, objectClass);
		} else if (annotation instanceof CobolPackedDecimal) {
			return convertPackedDecimal(context, (CobolPackedDecimal) annotation, objectClass);
		} else if (annotation instanceof CobolFloat) {
			return convertFloat(context, (CobolFloat) annotation, objectClass);
		} else if (annotation instanceof CobolDouble) {
			return convertDouble(context, (CobolDouble) annotation, objectClass);
		} else {
			throw new CobolBeanConverterException("Unsupported Cobol annotation " + annotation);
		}
	}

	// -----------------------------------------------------------------------------
	// Complex types
	// -----------------------------------------------------------------------------
	private <Z> Z convertGroup(Context context, CobolGroup cobolGroup, Class<Z> groupClass) {
		Z group = newInstance(groupClass);
		context.pushGroup(group);
		Field[] fields = groupClass.getDeclaredFields();
		for (Field field : fields) {
			Object value = convertField(context, field);
			setFieldValue(field, group, value);
		}
		context.popGroup();
		return group;
	}

	private Object convertField(Context context, Field field) {
		Annotation cobolItemType = getCobolItemType(field);
		if (cobolItemType == null) {
			throw new CobolBeanConverterException("Field " + field.getName() + " in "
					+ context.currentGroupQualifiedName() + " does not have Cobol annotations");
		}
		CobolArray cobolArray = getCobolArray(field);
		return cobolArray == null //
				? convert(context, cobolItemType, field.getType()) //
				: convertArray(context, cobolArray, cobolItemType, field.getType().getComponentType());
	}

	private <Z> Z[] convertArray(Context context, CobolArray cobolArray, Annotation cobolItemType, Class<Z> itemClass) {
		int maxOccurs = cobolArray.dependingOn() == null || cobolArray.dependingOn().isBlank() //
				? cobolArray.maxOccurs() //
				: getOdoObjectValue(context, cobolName(cobolItemType), cobolArray.dependingOn(),
						cobolArray.maxOccurs());
		@SuppressWarnings("unchecked")
		Z[] array = (Z[]) Array.newInstance(itemClass, maxOccurs);
		for (int i = 0; i < maxOccurs; i++) {
			array[i] = convert(context, cobolItemType, itemClass);
		}
		return array;
	}

	private <Z> Z convertChoice(Context context, CobolChoice cobolChoice, Class<Z> choiceClass) {
		try {
			Z choice = newInstance(choiceClass);
			Field[] fields = choiceClass.getDeclaredFields();
			long start = context.getBytesRead();
			for (Field alternative : fields) {
				if (choiceStrategy.choose(context.getRoot(), choice, alternative)) {
					// Strategy says this alternative is eligible so we try it out
					context.mark(cobolChoice.maxBytesLen());
					try {
						Object value = convertField(context, alternative);
						// Alternative matches the data (no exception caught while converting)
						setFieldValue(alternative, choice, value);
						long leftover = cobolChoice.maxBytesLen() - (context.getBytesRead() - start);
						if (leftover > 0) {
							// Chosen alternative is shorter than the larger one
							context.skip(leftover);
						}
						break;
					} catch (Exception e) {
						// Alternative does not match the data, try another one
						context.reset();
					}
				}
			}
			if (start == context.getBytesRead()) {
				// No data was consumed means no alternative matched the data
				throw qualifiedException(context, "None of the " + fields.length + " alternatives matched the data",
						cobolChoice.cobolName());
			}
			return choice;
		} catch (IOException e) {
			throw new CobolBeanConverterException(e);
		}
	}

	// -----------------------------------------------------------------------------
	// Primitive types
	// -----------------------------------------------------------------------------
	private <Z> Z convertString(Context context, CobolString cobolString, Class<Z> objectClass) {
		try {
			return (Z) stringConverter.convert(context.cobolInputStream, cobolString.charNum(), objectClass);
		} catch (CobolBeanConverterException e) {
			throw qualifiedException(context, e, cobolString.cobolName());
		}
	}

	private <Z> Z convertBinary(Context context, CobolBinaryNumber cobolBinary, Class<Z> objectClass) {
		try {
			Z value = (Z) binaryNumberConverter.convert(context.cobolInputStream, cobolBinary.signed(),
					cobolBinary.totalDigits(), objectClass);
			if (cobolBinary.odoObject()) {
				setOdoObjectValue(context, cobolBinary.cobolName(), value);
			}
			return value;
		} catch (CobolBeanConverterException e) {
			throw qualifiedException(context, e, cobolBinary.cobolName());
		}
	}

	private <Z> Z convertZonedDecimal(Context context, CobolZonedDecimal cobolZonedDecimal, Class<Z> objectClass) {
		try {
			Z value = (Z) zonedDecimalConverter.convert(context.cobolInputStream, cobolZonedDecimal.totalDigits(),
					cobolZonedDecimal.fractionDigits(), cobolZonedDecimal.signLeading(),
					cobolZonedDecimal.signSeparate(), objectClass);
			if (cobolZonedDecimal.odoObject()) {
				setOdoObjectValue(context, cobolZonedDecimal.cobolName(), value);
			}
			return value;
		} catch (CobolBeanConverterException e) {
			throw qualifiedException(context, e, cobolZonedDecimal.cobolName());
		}
	}

	private <Z> Z convertPackedDecimal(Context context, CobolPackedDecimal cobolPackedDecimal, Class<Z> objectClass) {
		try {
			Z value = (Z) packedDecimalConverter.convert(context.cobolInputStream, cobolPackedDecimal.signed(),
					cobolPackedDecimal.totalDigits(), cobolPackedDecimal.fractionDigits(), objectClass);
			if (cobolPackedDecimal.odoObject()) {
				setOdoObjectValue(context, cobolPackedDecimal.cobolName(), value);
			}
			return value;
		} catch (CobolBeanConverterException e) {
			throw qualifiedException(context, e, cobolPackedDecimal.cobolName());
		}
	}

	private <Z> Z convertFloat(Context context, CobolFloat cobolFloat, Class<Z> objectClass) {
		try {
			return (Z) floatConverter.convert(context.cobolInputStream, objectClass);
		} catch (CobolBeanConverterException e) {
			throw qualifiedException(context, e, cobolFloat.cobolName());
		}
	}

	private <Z> Z convertDouble(Context context, CobolDouble cobolDouble, Class<Z> objectClass) {
		try {
			return (Z) doubleConverter.convert(context.cobolInputStream, objectClass);
		} catch (CobolBeanConverterException e) {
			throw qualifiedException(context, e, cobolDouble.cobolName());
		}
	}

	// -----------------------------------------------------------------------------
	// Occurs depending On
	// -----------------------------------------------------------------------------
	private <Z> void setOdoObjectValue(Context context, String cobolName, Z value) {
		if (value instanceof Number) {
			context.putOdoObjectValue(cobolName, ((Number) value).intValue());
		} else {
			throw qualifiedException(context, "Value " + value + " is not a number and cannot be used as an array size",
					cobolName);
		}
	}

	private int getOdoObjectValue(Context context, String cobolName, String dependingOn, int maxOccurs) {
		Integer value = context.getOdoObjectValue(dependingOn);
		if (value == null) {
			throw qualifiedException(context, "Array size depends on " + dependingOn + " but no value was set",
					cobolName);
		} else if (value < 0 || value > maxOccurs) {
			throw qualifiedException(context,
					"Array size depending on " + dependingOn + " is '" + value + "' which is invalid", cobolName);
		} else {
			return value;
		}
	}

	// -----------------------------------------------------------------------------
	// Default Choice strategy (all fields are eligible)
	// -----------------------------------------------------------------------------
	class CobolConverterDefaultChoiceStrategy implements CobolChoiceStrategy<T> {

		@Override
		public boolean choose(T root, Object choice, Field alternative) {
			return true;
		}

	}

	// -----------------------------------------------------------------------------
	// Annotations
	// -----------------------------------------------------------------------------
	private Annotation getCobolItemType(Class<?> clazz) {
		if (clazz.isArray()) {
			return getCobolItemType(clazz.getComponentType());
		} else {
			return getCobolItemType(clazz.getDeclaredAnnotations());
		}
	}

	private Annotation getCobolItemType(Field field) {
		Annotation annotation = getCobolItemType(field.getDeclaredAnnotations());
		return annotation == null ? getCobolItemType(field.getType()) : annotation;
	}

	private CobolArray getCobolArray(Field f) {
		return f.getDeclaredAnnotation(CobolArray.class);
	}

	private Annotation getCobolItemType(Annotation[] annotations) {
		return Arrays.stream(annotations) //
				.filter(a -> a.annotationType().isAnnotationPresent(CobolItemType.class)) //
				.findFirst() //
				.orElse(null);
	}

	private String cobolName(Annotation annotation) {
		if (annotation instanceof CobolGroup) {
			return ((CobolGroup) annotation).cobolName();
		} else if (annotation instanceof CobolChoice) {
			return ((CobolChoice) annotation).cobolName();
		} else if (annotation instanceof CobolBinaryNumber) {
			return ((CobolBinaryNumber) annotation).cobolName();
		} else if (annotation instanceof CobolZonedDecimal) {
			return ((CobolZonedDecimal) annotation).cobolName();
		} else if (annotation instanceof CobolPackedDecimal) {
			return ((CobolPackedDecimal) annotation).cobolName();
		} else if (annotation instanceof CobolFloat) {
			return ((CobolFloat) annotation).cobolName();
		} else if (annotation instanceof CobolDouble) {
			return ((CobolDouble) annotation).cobolName();
		} else {
			return null;
		}
	}

	// -----------------------------------------------------------------------------
	// Bean getters/setters
	// -----------------------------------------------------------------------------
	/**
	 * Create a new instance of a bean.
	 * <p>
	 * Bean is assumed to have a no arg constructor.
	 */
	private <Z> Z newInstance(Class<Z> clazz) {
		try {
			Constructor<Z> constructor = clazz.getConstructor();
			return constructor.newInstance();
		} catch (Exception e) {
			throw new CobolBeanConverterException(e);
		}
	}

	private Object setFieldValue(Field field, Object group, Object value) {
		try {
			Method setter = group.getClass().getMethod(setterName(field), field.getType());
			return setter.invoke(group, value);
		} catch (Exception e) {
			throw new CobolBeanConverterException(e);
		}
	}

	private String setterName(Field field) {
		return "set" + Capitalize(field.getName());
	}

	private String Capitalize(String s) {
		return s.length() <= 1 ? s.toUpperCase() : Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

	// -----------------------------------------------------------------------------
	// Error reporting
	// -----------------------------------------------------------------------------

	/**
	 * Improve error reporting by adding cobol item qualified name and bytes
	 * location in input stream.
	 */
	private CobolBeanConverterException qualifiedException(Context context, CobolBeanConverterException e,
			String cobolName) {
		e.setCobolQualifiedName(context.cobolQualifiedName(cobolName));
		e.setBytesCounter(context.getBytesRead());
		return e;
	}

	private CobolBeanConverterException qualifiedException(Context context, String msg, String cobolName) {
		CobolBeanConverterException e = new CobolBeanConverterException(msg);
		e.setCobolQualifiedName(context.cobolQualifiedName(cobolName));
		e.setBytesCounter(context.getBytesRead());
		return e;
	}

	// -----------------------------------------------------------------------------
	// Conversion context
	// -----------------------------------------------------------------------------

	record Context(CobolInputStream cobolInputStream, Map<String, Integer> odoObjectValues, Stack<Object> groupStack) {

		long getBytesRead() {
			return cobolInputStream.getBytesRead();
		}

		void reset() throws IOException {
			cobolInputStream.reset();
		}

		void mark(int maxBytesLen) {
			cobolInputStream.mark(maxBytesLen);
		}

		void skip(long leftover) throws IOException {
			cobolInputStream.skip(leftover);
		}

		boolean groupStackIsEmpty() {
			return groupStack.isEmpty();
		}

		@SuppressWarnings("unchecked")
		<Z> Z getRoot() {
			return groupStack.isEmpty() ? null : (Z) groupStack.get(0);
		}

		void pushGroup(Object group) {
			groupStack.push(group);
		}

		void popGroup() {
			groupStack.pop();
		}

		void putOdoObjectValue(String cobolName, int value) {
			odoObjectValues.put(cobolName, value);
		}

		Integer getOdoObjectValue(String cobolName) {
			return odoObjectValues.get(cobolName);
		}

		/**
		 * Create a qualified cobol name for the most inner group starting at the root
		 * group.
		 */
		String currentGroupQualifiedName() {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < groupStack.size(); i++) {
				if (i > 0) {
					sb.append(".");
				}
				CobolGroup cobolGroup = groupStack.get(i).getClass().getAnnotation(CobolGroup.class);
				sb.append(cobolGroup.cobolName());
			}
			return sb.toString();
		}

		/**
		 * Create a qualified cobol name starting at the root group.
		 */
		String cobolQualifiedName(String cobolName) {
			StringBuilder sb = new StringBuilder();
			sb.append(currentGroupQualifiedName());
			if (!groupStack.isEmpty()) {
				sb.append(".");
			}
			sb.append(cobolName);
			return sb.toString();
		}

	}
}
