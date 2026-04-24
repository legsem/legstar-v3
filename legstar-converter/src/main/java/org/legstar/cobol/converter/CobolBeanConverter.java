package org.legstar.cobol.converter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
import org.legstar.cobol.io.CobolInputStream;

/**
 * Converts cobol data to a java T instance.
 * <p>
 * The target java bean class must hold cobol annotations as produced by
 * legstar-generator.
 * <p>
 * Thread safe.
 * 
 * @param <T> target bean class type
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

	/**
	 * Fast access to bean methods.
	 * <p>
	 * Here we cache Annotations, Constructors, declared fields and setValue methods
	 * so that we don't have to rebuild them every time we visit the same field or
	 * class.
	 * <p>
	 * This should not pose a risk of memory leak as we are limited to the fields
	 * and classes in a specific immutable beanClass.
	 */
	private final Map<Field, Annotation> annotationCache = new ConcurrentHashMap<>();

	private final Map<Class<?>, Constructor<?>> constructorCache = new ConcurrentHashMap<>();

	private final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentHashMap<>();

	private final Map<Field, Method> setValueCache = new ConcurrentHashMap<>();

	/**
	 * Build a converter with default configuration parameters.
	 * 
	 * @param beanClass target bean class
	 */
	public CobolBeanConverter(Class<T> beanClass) {
		this(CobolBeanConverterConfig.ebcdic(), beanClass);
	}

	/**
	 * Build a converter.
	 * 
	 * @param config    the converter's parameters
	 * @param beanClass target bean class
	 */
	public CobolBeanConverter(CobolConverterConfig config, Class<T> beanClass) {
		this(config, beanClass, null);
	}

	/**
	 * Build a converter.
	 * 
	 * @param config         the converter's parameters
	 * @param beanClass      target bean class
	 * @param choiceStrategy strategy to select alternatives in choices. If null,
	 *                       the default strategy is applied
	 */
	public CobolBeanConverter(CobolConverterConfig config, Class<T> beanClass, CobolChoiceStrategy<T> choiceStrategy) {
		this.beanClass = beanClass;
		this.choiceStrategy = choiceStrategy == null ? new CobolConverterDefaultChoiceStrategy() : choiceStrategy;
		stringConverter = new CobolStringConverter(config.hostCharsetName(), //
				config.truncateHostStringsTrailingSpaces(), //
				config.rightPadCobolAlphanumWithSpaces(), //
				config.hostSpaceCharCode());
		binaryNumberConverter = new CobolBinaryNumberConverter();
		zonedDecimalConverter = new CobolZonedDecimalConverter(config.hostMinusSign(),  //
				config.hostPlusSign(),  //
				config.positiveSignNibbleValue(), //
				config.negativeSignNibbleValue(), //
				config.unspecifiedSignNibbleValue());
		packedDecimalConverter = new CobolPackedDecimalConverter(config.hostSpaceCharCode(), //
				config.positiveSignNibbleValue(),  //
				config.negativeSignNibbleValue(), //
				config.unspecifiedSignNibbleValue());
		floatConverter = new CobolFloatConverter();
		doubleConverter = new CobolDoubleConverter();
	}

	/**
	 * Produce a java bean consuming cobol data from the input stream.
	 * <p>
	 * This can be invoked repeatedly over the same input stream.
	 * 
	 * @param cis the cobol input data
	 * @return a java bean instance
	 */
	public T convert(CobolInputStream cis) {
		Context context = new Context(cis, new HashMap<>(), new Stack<>());
		Annotation annotation = getCobolItemType(beanClass);
		return convert(context, annotation, beanClass);
	}

	/**
	 * The input stream may contain data for more than one bean. This method
	 * provides a stream that will produce as many beans as possible from the input
	 * stream.
	 * 
	 * @param cis the cobol input data
	 * @return a java.util.Stream of beans
	 */
	public Stream<T> convertAll(CobolInputStream cis) {
		Iterator<T> iter = new Iterator<>() {
			T nextBean = null;

			@Override
			public boolean hasNext() {
				if (nextBean != null) {
					return true;
				} else {
					try {
						nextBean = convert(cis);
					} catch (CobolBeanConverterEOFException e) {
						nextBean = null;
					}
					return (nextBean != null);
				}
			}

			@Override
			public T next() {
				if (nextBean != null || hasNext()) {
					T bean = nextBean;
					nextBean = null;
					return bean;
				} else {
					throw new NoSuchElementException();
				}
			}
		};
		return StreamSupport
				.stream(Spliterators.spliteratorUnknownSize(iter, Spliterator.ORDERED | Spliterator.NONNULL), false);
	}

	/**
	 * Dispatch to specialized conversion methods depending on the cobol annotation.
	 * 
	 * @param <Z>         the target java type
	 * @param context     conversion mutable context
	 * @param annotation  the current cobol item's annotations
	 * @param objectClass the target java class
	 * @return the converted java value
	 */
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
	/**
	 * Convert a cobol group.
	 * 
	 * @param <Z>        the target java type
	 * @param context    conversion mutable context
	 * @param cobolGroup the cobol group annotation
	 * @param groupClass the target java class
	 * @return the converted java value
	 */
	private <Z> Z convertGroup(Context context, CobolGroup cobolGroup, Class<Z> groupClass) {
		Z group = newInstance(groupClass);
		context.pushGroup(group);
		Field[] fields = getDeclaredFields(groupClass);
		for (Field field : fields) {
			Object value = convertField(context, field);
			setFieldValue(field, group, value);
		}
		context.popGroup();
		return group;
	}

	/**
	 * Convert a group child item.
	 * 
	 * @param context conversion mutable context
	 * @param field   the child item
	 * @return the converted java value
	 */
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

	/**
	 * Convert an array of items.
	 * 
	 * @param <Z>           the target java item type
	 * @param context       conversion mutable context
	 * @param cobolArray    the cobol array annotation
	 * @param cobolItemType the cobol item type annotation
	 * @param itemClass     the target java item class
	 * @return the converted java array value
	 */
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

	/**
	 * Convert a cobol choice.
	 * 
	 * @param <Z>         the target java type
	 * @param context     conversion mutable context
	 * @param cobolChoice the cobol choice annotation
	 * @param choiceClass the target java class
	 * @return the converted java value
	 */
	private <Z> Z convertChoice(Context context, CobolChoice cobolChoice, Class<Z> choiceClass) {
		try {
			Z choice = newInstance(choiceClass);
			Field[] fields = getDeclaredFields(choiceClass);
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
	/**
	 * Convert a COBOL alphanumeric.
	 * 
	 * @param <Z>         the target java type
	 * @param context     conversion mutable context
	 * @param cobolString cobol string annotation
	 * @param objectClass the target java class
	 * @return the converted java value
	 */
	private <Z> Z convertString(Context context, CobolString cobolString, Class<Z> objectClass) {
		try {
			return (Z) stringConverter.convert(context.cobolInputStream, cobolString.charNum(), objectClass);
		} catch (CobolBeanConverterException e) {
			throw qualifiedException(context, e, cobolString.cobolName());
		}
	}

	/**
	 * Convert a COBOL binary number (COMP, COMP-5).
	 * 
	 * @param <Z>         the target java type
	 * @param context     conversion mutable context
	 * @param cobolBinary cobol binary number annotation
	 * @param objectClass the target java class
	 * @return the converted java value
	 */
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

	/**
	 * Convert a COBOL zoned decimal.
	 * 
	 * @param <Z>               the target java type
	 * @param context           conversion mutable context
	 * @param cobolZonedDecimal cobol zoned decimal annotation
	 * @param objectClass       the target java class
	 * @return the converted java value
	 */
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

	/**
	 * Convert a COBOL packed decimal (COMP-3).
	 * 
	 * @param <Z>                the target java type
	 * @param context            conversion mutable context
	 * @param cobolPackedDecimal cobol packed decimal annotation
	 * @param objectClass        the target java class
	 * @return the converted java value
	 */
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

	/**
	 * Convert a COBOL float (COMP-1).
	 * 
	 * @param <Z>         the target java type
	 * @param context     conversion mutable context
	 * @param cobolFloat  cobol float annotation
	 * @param objectClass the target java class
	 * @return the converted java value
	 */
	private <Z> Z convertFloat(Context context, CobolFloat cobolFloat, Class<Z> objectClass) {
		try {
			return (Z) floatConverter.convert(context.cobolInputStream, objectClass);
		} catch (CobolBeanConverterException e) {
			throw qualifiedException(context, e, cobolFloat.cobolName());
		}
	}

	/**
	 * Convert a COBOL double (COMP-2).
	 * 
	 * @param <Z>         the target java type
	 * @param context     conversion mutable context
	 * @param cobolDouble cobol double annotation
	 * @param objectClass the target java class
	 * @return the converted java value
	 */
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
	/**
	 * Given a numeric value set the corresponding variable size cobol array size.
	 * 
	 * @param <Z>       the value type
	 * @param context   conversion mutable context
	 * @param cobolName cobol name of the variable giving the array size (Occurs
	 *                  depending on object).
	 * @param value     the value
	 */
	private <Z> void setOdoObjectValue(Context context, String cobolName, Z value) {
		if (value instanceof Number) {
			context.putOdoObjectValue(cobolName, ((Number) value).intValue());
		} else {
			throw qualifiedException(context, "Value " + value + " is not a number and cannot be used as an array size",
					cobolName);
		}
	}

	/**
	 * Retrieve the actual size of a variable size cobol array.
	 * 
	 * @param context     conversion mutable context
	 * @param cobolName   cobol name of the variable size array
	 * @param dependingOn cobol name of the variable giving the array size (Occurs
	 *                    depending on object).
	 * @param maxOccurs   the maximum size of the array
	 * @return the current size of the array
	 */
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
	// Default Choice strategy (all alternatives are eligible)
	// -----------------------------------------------------------------------------
	/**
	 * The default strategy to chose an alternative for a choice.
	 */
	class CobolConverterDefaultChoiceStrategy implements CobolChoiceStrategy<T> {

		@Override
		public boolean choose(T root, Object choice, Field alternative) {
			return true;
		}

	}

	// -----------------------------------------------------------------------------
	// Annotations
	// -----------------------------------------------------------------------------
	/**
	 * Retrieve the cobol annotation attaches to a class.
	 * <p>
	 * In the case of an array, this returns the type of an array item.
	 * 
	 * @param clazz the annotated class
	 * @return the cobol annotation or null if not found
	 */
	private Annotation getCobolItemType(Class<?> clazz) {
		if (clazz.isArray()) {
			return getCobolItemType(clazz.getComponentType());
		} else {
			return getCobolItemType(clazz.getDeclaredAnnotations());
		}
	}

	/**
	 * Retrieve the cobol annotation if a group child.
	 * 
	 * @param field the group child
	 * @return the cobol annotation or null if not found
	 */
	private Annotation getCobolItemType(Field field) {
		return annotationCache.computeIfAbsent(field, f -> {
			Annotation annotation = getCobolItemType(f.getDeclaredAnnotations());
			return annotation == null ? getCobolItemType(f.getType()) : annotation;
		});
	}

	/**
	 * Retrieve the cobol array annotation fro a grou child.
	 * 
	 * @param field the group child
	 * @return the cobol array annotation or null if not found
	 */
	private CobolArray getCobolArray(Field field) {
		return field.getDeclaredAnnotation(CobolArray.class);
	}

	/**
	 * Given a set of annotations, extract the first cobol annotation.
	 * 
	 * @param annotations a set of annotations
	 * @return the first cobol (normaly only) cobol annotation or null if none is
	 *         found
	 */
	private Annotation getCobolItemType(Annotation[] annotations) {
		return Arrays.stream(annotations) //
				.filter(a -> a.annotationType().isAnnotationPresent(CobolItemType.class)) //
				.findFirst() //
				.orElse(null);
	}

	/**
	 * Given a cobol annotation, retrieve the cobol item's name.
	 * 
	 * @param annotation
	 * @return the cobol name or null if none is found
	 */
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
	 * Create a new instance of a class.
	 * <p>
	 * Class is assumed to have a no arg constructor.
	 * <p>
	 * Since this is expensive, we cache the constructor method.
	 * 
	 * @param <Z>   the target instance class type
	 * @param clazz the target instance class
	 * @return a new instance
	 */
	@SuppressWarnings("unchecked")
	private <Z> Z newInstance(Class<Z> clazz) {
		try {
			return (Z) constructorCache.computeIfAbsent(clazz, c -> {
				try {
					return clazz.getConstructor();
				} catch (Throwable e) {
					throw new CobolBeanConverterException(e);
				}
			}).newInstance();
		} catch (Throwable e) {
			throw new CobolBeanConverterException(e);
		}
	}

	/**
	 * Set a field value using a setter method on the parent group bean.
	 * <p>
	 * Since this is expensive, we cache the setter method.
	 * 
	 * @param field the group child
	 * @param group the group bean
	 * @param value the value to set
	 */
	private void setFieldValue(Field field, Object group, Object value) {
		try {
			setValueCache.computeIfAbsent(field, f -> {
				try {
					return group.getClass().getMethod(setterName(field), field.getType());
				} catch (Throwable e) {
					throw new CobolBeanConverterException(e);
				}
			}).invoke(group, value);
		} catch (Throwable e) {
			throw new CobolBeanConverterException(e);
		}
	}

	/**
	 * Retrieve a class fields from cache if available.
	 * 
	 * @param clazz the class
	 * @return a list of fields
	 */
	private Field[] getDeclaredFields(Class<?> clazz) {
		return declaredFieldsCache.computeIfAbsent(clazz, c -> {
			return c.getDeclaredFields();
		});
	}

	/**
	 * Form a bean setter method name.
	 * 
	 * @param field a group child
	 * @return a setter method name
	 */
	private String setterName(Field field) {
		return "set" + Capitalize(field.getName());
	}

	/**
	 * Capitalize the first character of a string.
	 * 
	 * @param s the string
	 * @return a capitalized string
	 */
	private String Capitalize(String s) {
		return s.length() <= 1 ? s.toUpperCase() : Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

	// -----------------------------------------------------------------------------
	// Error reporting
	// -----------------------------------------------------------------------------

	/**
	 * Improve error reporting by adding cobol item qualified name and bytes
	 * location in input stream.
	 * 
	 * @param context   conversion mutable context
	 * @param e         an exception
	 * @param cobolName cobol name of the current item being processed
	 * @return an improved exception
	 */
	private CobolBeanConverterException qualifiedException(Context context, CobolBeanConverterException e,
			String cobolName) {
		e.setCobolQualifiedName(context.cobolQualifiedName(cobolName));
		e.setBytesCounter(context.getBytesRead());
		return e;
	}

	/**
	 * Create an exception.
	 * 
	 * @param context   conversion mutable context
	 * @param msg       the exception reason
	 * @param cobolName cobol name of the current item being processed
	 * @return an exception
	 */
	private CobolBeanConverterException qualifiedException(Context context, String msg, String cobolName) {
		CobolBeanConverterException e = new CobolBeanConverterException(msg);
		e.setCobolQualifiedName(context.cobolQualifiedName(cobolName));
		e.setBytesCounter(context.getBytesRead());
		return e;
	}

	// -----------------------------------------------------------------------------
	// Conversion context
	// -----------------------------------------------------------------------------

	/**
	 * Anything that is mutable (apart from caches) is stored in this context in
	 * order to keep the converter thread safe.
	 * 
	 * @param cobolInputStream the cobol input data
	 * @param odoObjectValues  the variable size array dimensions collected
	 * @param groupStack       the group hierarchy that is traversed
	 */
	record Context(CobolInputStream cobolInputStream, Map<String, Integer> odoObjectValues, Stack<Object> groupStack) {

		/**
		 * Is the end of file reached yet.
		 * 
		 * @return true if end of file has been reached
		 */
		boolean isEof() {
			return cobolInputStream.isEof();
		}

		/**
		 * How many cobol bytes were read so far.
		 * 
		 * @return the current cobol input byte count
		 */
		long getBytesRead() {
			return cobolInputStream.getBytesRead();
		}

		/**
		 * Reset the cobol data input to the previously marked position.
		 * 
		 * @throws IOException if resetting fails
		 */
		void reset() throws IOException {
			cobolInputStream.reset();
		}

		/**
		 * Mark the position in the cobol input data so that we can eventually reset to
		 * that position.
		 * 
		 * @param maxBytesLen the maximum limit of bytes that can be read before the
		 *                    mark position becomes invalid.
		 */
		void mark(int maxBytesLen) {
			cobolInputStream.mark(maxBytesLen);
		}

		/**
		 * Skip a number of bytes in the cobol input data.
		 * 
		 * @param leftover the number of bytes to skip
		 * @return the number of bytes actually skipped
		 * @throws IOException if skipping fails
		 */
		long skip(long leftover) throws IOException {
			return cobolInputStream.skip(leftover);
		}

		/**
		 * Are we on the root item.
		 * 
		 * @return true if the current item is the root
		 */
		boolean groupStackIsEmpty() {
			return groupStack.isEmpty();
		}

		/**
		 * Get the root item.
		 * 
		 * @param <Z> the root item type
		 * @return the root item or null if no item was converted yet
		 */
		@SuppressWarnings("unchecked")
		<Z> Z getRoot() {
			return groupStack.isEmpty() ? null : (Z) groupStack.get(0);
		}

		/**
		 * Push a group on the stack.
		 * 
		 * @param group to push
		 */
		void pushGroup(Object group) {
			groupStack.push(group);
		}

		/**
		 * Pop a group from the stack.
		 */
		void popGroup() {
			groupStack.pop();
		}

		/**
		 * Given a value, set the corresponding variable size cobol array size.
		 * 
		 * @param cobolName cobol name of the variable giving the array size (Occurs
		 *                  depending on object).
		 * @param value     the value
		 */
		void putOdoObjectValue(String cobolName, int value) {
			odoObjectValues.put(cobolName, value);
		}

		/**
		 * Retrieve the actual size of a variable size cobol array.
		 * 
		 * @param cobolName cobol name of the variable giving the array size (Occurs
		 *                  depending on object).
		 * @return the current size of the array
		 */
		Integer getOdoObjectValue(String cobolName) {
			return odoObjectValues.get(cobolName);
		}

		/**
		 * Create a qualified cobol name for the most inner group starting at the root
		 * group.
		 * 
		 * @return current cobol group qualified name
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
		 * 
		 * @return qualified cobol name starting at the root group
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
