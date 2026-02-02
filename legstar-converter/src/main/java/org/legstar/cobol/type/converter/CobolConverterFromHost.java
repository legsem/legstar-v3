package org.legstar.cobol.type.converter;

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

import org.legstar.cobol.type.annotations.CobolArray;
import org.legstar.cobol.type.annotations.CobolBinaryNumber;
import org.legstar.cobol.type.annotations.CobolChoice;
import org.legstar.cobol.type.annotations.CobolDouble;
import org.legstar.cobol.type.annotations.CobolFloat;
import org.legstar.cobol.type.annotations.CobolGroup;
import org.legstar.cobol.type.annotations.CobolItemType;
import org.legstar.cobol.type.annotations.CobolPackedDecimal;
import org.legstar.cobol.type.annotations.CobolString;
import org.legstar.cobol.type.annotations.CobolZonedDecimal;

/**
 * Converts host bytes to a java T instance.
 * <p>
 * The target java class must hold cobol annotations as produced by legstar-generator.
 */
public class CobolConverterFromHost<T> {

	private final CobolConverterString stringConverter;

	private final CobolConverterBinaryNumber binaryNumberConverter;

	private final CobolConverterZonedDecimal zonedDecimalConverter;

	private final CobolConverterPackedDecimal packedDecimalConverter;

	private final CobolConverterFloat floatConverter;

	private final CobolConverterDouble doubleConverter;

	private final Map<String, Integer> odoObjectValues = new HashMap<>();

	private final CobolConverterFromHostChoiceStrategy<T> choiceStrategy;

	private final Stack<Object> groupStack = new Stack<>();
	
	public CobolConverterFromHost(CobolConverterConfig config) {
		this(config, null);
	}

	public CobolConverterFromHost(CobolConverterConfig config, CobolConverterFromHostChoiceStrategy<T> choiceStrategy) {
		this.stringConverter = new CobolConverterString(config);
		this.binaryNumberConverter = new CobolConverterBinaryNumber(config);
		this.zonedDecimalConverter = new CobolConverterZonedDecimal(config);
		this.packedDecimalConverter = new CobolConverterPackedDecimal(config);
		this.floatConverter = new CobolConverterFloat(config);
		this.doubleConverter = new CobolConverterDouble(config);
		this.choiceStrategy = choiceStrategy == null ? new CobolConverterDefaultChoiceStrategy() : choiceStrategy;
	}

	public T convert(CobolConverterInputStream is, Class<T> outputClass) {
		Annotation annotation = getCobolItemType(outputClass);
		return convert(annotation, is, outputClass);
	}

	private <Z> Z convert(Annotation annotation, CobolConverterInputStream is, Class<Z> objectClass) {
		if (annotation instanceof CobolGroup) {
			return convertGroup((CobolGroup) annotation, is, objectClass);
		} else if (annotation instanceof CobolChoice) {
			return convertChoice((CobolChoice) annotation, is, objectClass);
		} else if (annotation instanceof CobolString) {
			return convertString((CobolString) annotation, is, objectClass);
		} else if (annotation instanceof CobolBinaryNumber) {
			return convertBinary((CobolBinaryNumber) annotation, is, objectClass);
		} else if (annotation instanceof CobolZonedDecimal) {
			return convertZonedDecimal((CobolZonedDecimal) annotation, is, objectClass);
		} else if (annotation instanceof CobolPackedDecimal) {
			return convertPackedDecimal((CobolPackedDecimal) annotation, is, objectClass);
		} else if (annotation instanceof CobolFloat) {
			return convertFloat((CobolFloat) annotation, is, objectClass);
		} else if (annotation instanceof CobolDouble) {
			return convertDouble((CobolDouble) annotation, is, objectClass);
		} else {
			throw new CobolConverterException("Unsupported Cobol annotation " + annotation);
		}
	}

	// -----------------------------------------------------------------------------
	// Complex types
	// -----------------------------------------------------------------------------
	private <Z> Z convertGroup(CobolGroup cobolGroup, CobolConverterInputStream is, Class<Z> groupClass) {
		Z group = newInstance(groupClass);
		groupStack.push(group);
		Field[] fields = groupClass.getDeclaredFields();
		for (Field field : fields) {
			Object value = convertField(is, field);
			setFieldValue(field, group, value);
		}
		groupStack.pop();
		return group;
	}

	private Object convertField(CobolConverterInputStream is, Field field) {
		Annotation cobolItemType = getCobolItemType(field);
		if (cobolItemType == null) {
			throw new CobolConverterException("Field " + field.getName() + " in " + currentGroupQualifiedName()
					+ " does not have Cobol annotations");
		}
		CobolArray cobolArray = getCobolArray(field);
		return cobolArray == null //
				? convert(cobolItemType, is, field.getType()) //
				: convertArray(cobolArray, cobolItemType, is, field.getType().getComponentType());
	}

	private <Z> Z[] convertArray(CobolArray cobolArray, Annotation cobolItemType, CobolConverterInputStream is,
			Class<Z> itemClass) {
		int maxOccurs = cobolArray.dependingOn() == null || cobolArray.dependingOn().isBlank() //
				? cobolArray.maxOccurs() //
				: getOdoObjectValue(cobolArray.dependingOn());
		@SuppressWarnings("unchecked")
		Z[] array = (Z[]) Array.newInstance(itemClass, maxOccurs);
		for (int i = 0; i < maxOccurs; i++) {
			array[i] = convert(cobolItemType, is, itemClass);
		}
		return array;
	}

	private <Z> Z convertChoice(CobolChoice cobolChoice, CobolConverterInputStream is, Class<Z> choiceClass) {
		try {
			Z choice = newInstance(choiceClass);
			Field[] fields = choiceClass.getDeclaredFields();
			long start = is.getBytesRead();
			for (Field alternative : fields) {
				if (choiceStrategy.choose(getRoot(), choice, alternative)) {
					// Strategy says this alternative is eligible so we try it out
					is.mark(cobolChoice.maxBytesLen());
					try {
						Object value = convertField(is, alternative);
						// Alternative matches the data (no exception caught while converting)
						setFieldValue(alternative, choice, value);
						long leftover = cobolChoice.maxBytesLen() - (is.getBytesRead() - start);
						if (leftover > 0) {
							// Chosen alternative is shorter than the larger one
							is.skip(leftover);
						}
						break;
					} catch (Exception e) {
						// Alternative does not match the data, try another one
						is.reset();
					}
				}
			}
			if (start == is.getBytesRead()) {
				// No data was consumed means no alternative matched the data
				throw qualifiedException("None of the " + fields.length + " alternatives matched the data", is,
						cobolChoice.cobolName());
			}
			return choice;
		} catch (IOException e) {
			throw new CobolConverterException(e);
		}
	}

	// -----------------------------------------------------------------------------
	// Primitive types
	// -----------------------------------------------------------------------------
	private <Z> Z convertString(CobolString cobolString, CobolConverterInputStream is, Class<Z> objectClass) {
		try {
			return (Z) stringConverter.convert(is, cobolString.charNum(), objectClass);
		} catch (CobolConverterException e) {
			throw qualifiedException(e, is, cobolString.cobolName());
		}
	}

	private <Z> Z convertBinary(CobolBinaryNumber cobolBinary, CobolConverterInputStream is, Class<Z> objectClass) {
		try {
			Z value = (Z) binaryNumberConverter.convert(is, cobolBinary.signed(), cobolBinary.totalDigits(),
					objectClass);
			if (cobolBinary.odoObject()) {
				setOdoObjectValue(cobolBinary.cobolName(), value);
			}
			return value;
		} catch (CobolConverterException e) {
			throw qualifiedException(e, is, cobolBinary.cobolName());
		}
	}

	private <Z> Z convertZonedDecimal(CobolZonedDecimal cobolZonedDecimal, CobolConverterInputStream is,
			Class<Z> objectClass) {
		try {
			Z value = (Z) zonedDecimalConverter.convert(is, cobolZonedDecimal.totalDigits(),
					cobolZonedDecimal.fractionDigits(), cobolZonedDecimal.signLeading(),
					cobolZonedDecimal.signSeparate(), objectClass);
			if (cobolZonedDecimal.odoObject()) {
				setOdoObjectValue(cobolZonedDecimal.cobolName(), value);
			}
			return value;
		} catch (CobolConverterException e) {
			throw qualifiedException(e, is, cobolZonedDecimal.cobolName());
		}
	}

	private <Z> Z convertPackedDecimal(CobolPackedDecimal cobolPackedDecimal, CobolConverterInputStream is,
			Class<Z> objectClass) {
		try {
			Z value = (Z) packedDecimalConverter.convert(is, cobolPackedDecimal.signed(),
					cobolPackedDecimal.totalDigits(), cobolPackedDecimal.fractionDigits(), objectClass);
			if (cobolPackedDecimal.odoObject()) {
				setOdoObjectValue(cobolPackedDecimal.cobolName(), value);
			}
			return value;
		} catch (CobolConverterException e) {
			throw qualifiedException(e, is, cobolPackedDecimal.cobolName());
		}
	}

	private <Z> Z convertFloat(CobolFloat cobolFloat, CobolConverterInputStream is, Class<Z> objectClass) {
		try {
			return (Z) floatConverter.convert(is, objectClass);
		} catch (CobolConverterException e) {
			throw qualifiedException(e, is, cobolFloat.cobolName());
		}
	}

	private <Z> Z convertDouble(CobolDouble cobolDouble, CobolConverterInputStream is, Class<Z> objectClass) {
		try {
			return (Z) doubleConverter.convert(is, objectClass);
		} catch (CobolConverterException e) {
			throw qualifiedException(e, is, cobolDouble.cobolName());
		}
	}

	// -----------------------------------------------------------------------------
	// Occurs depending On
	// -----------------------------------------------------------------------------
	private <Z> void setOdoObjectValue(String cobolName, Z value) {
		if (value instanceof Number) {
			odoObjectValues.put(cobolName, ((Number) value).intValue());
		} else {
			throw new CobolConverterException(
					"Value " + value + " is not a number and cannot be used for an occurs depending on");
		}
	}

	private int getOdoObjectValue(String dependingOn) {
		if (odoObjectValues.containsKey(dependingOn)) {
			return odoObjectValues.get(dependingOn);
		} else {
			throw new CobolConverterException("Occurs depending on " + dependingOn + " was not set");
		}
	}

	// -----------------------------------------------------------------------------
	// Default Choice strategy (all fields are eligible)
	// -----------------------------------------------------------------------------
	class CobolConverterDefaultChoiceStrategy implements CobolConverterFromHostChoiceStrategy<T> {

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
			throw new CobolConverterException(e);
		}
	}

	private Object setFieldValue(Field field, Object group, Object value) {
		try {
			Method setter = group.getClass().getMethod(setterName(field), field.getType());
			return setter.invoke(group, value);
		} catch (Exception e) {
			throw new CobolConverterException(e);
		}
	}

	private String setterName(Field field) {
		return "set" + Capitalize(field.getName());
	}

	private String Capitalize(String s) {
		return s.length() <= 1 ? s.toUpperCase() : Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

	// -----------------------------------------------------------------------------
	// Group stack. The top group is the upper item in the stack
	// -----------------------------------------------------------------------------
	public Stack<Object> getGroupStack() {
		return groupStack;
	}

	@SuppressWarnings("unchecked")
	public T getRoot() {
		return groupStack.isEmpty() ? null : (T) groupStack.get(0);
	}

	private String currentGroupQualifiedName() {
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

	// -----------------------------------------------------------------------------
	// Error reporting
	// -----------------------------------------------------------------------------

	/**
	 * Improve error reporting by adding cobol item qualified name and bytes
	 * location in input stream.
	 */
	private CobolConverterException qualifiedException(CobolConverterException e, CobolConverterInputStream is,
			String cobolName) {
		e.setCobolQualifiedName(cobolQualifiedName(cobolName));
		e.setBytesCounter(is.getBytesRead());
		return e;
	}

	private CobolConverterException qualifiedException(String msg, CobolConverterInputStream is, String cobolName) {
		CobolConverterException e = new CobolConverterException(msg);
		e.setCobolQualifiedName(cobolQualifiedName(cobolName));
		e.setBytesCounter(is.getBytesRead());
		return e;
	}

	private String cobolQualifiedName(String cobolName) {
		StringBuilder sb = new StringBuilder();
		sb.append(currentGroupQualifiedName());
		if (!groupStack.isEmpty()) {
			sb.append(".");
		}
		sb.append(cobolName);
		return sb.toString();
	}
	
}
