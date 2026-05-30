package org.legstar.cobol.converter;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.legstar.cobol.io.CobolInputStream;
import org.legstar.cobol.io.CobolOutputStream;

/**
 * Converts Cobol data to and from a Java T instance.
 * <p>
 * The Java bean class must hold Cobol annotations as produced by
 * legstar-generator.
 * <p>
 * Thread safe.
 * 
 * @param <T> Java bean class type
 */
public class CobolBeanConverter<T> {

	/**
	 * The Java bean class (as produced by legstar-generator).
	 */
	private final Class<T> beanClass;

	/**
	 * Converter for primitive types.
	 */
	private final CobolPrimitiveConverter primitiveConverter;

	/**
	 * Class/Field information cache
	 */
	private final CobolClassInfo classInfo;

	/**
	 * A custom choice resolution strategy. Null if default strategy applies
	 */
	private final CobolChoiceStrategy<T> choiceStrategy;

	/**
	 * Build a converter with default configuration parameters.
	 * 
	 * @param beanClass Java bean class
	 */
	public CobolBeanConverter(Class<T> beanClass) {
		this(CobolBeanConverterConfig.ebcdic(), beanClass, null);
	}

	/**
	 * Build a converter with default configuration parameters and a choice
	 * resolution strategy.
	 * 
	 * @param beanClass      target bean class
	 * @param choiceStrategy choice resolution strategy
	 */
	public CobolBeanConverter(Class<T> beanClass, CobolChoiceStrategy<T> choiceStrategy) {
		this(CobolBeanConverterConfig.ebcdic(), beanClass, choiceStrategy);
	}

	/**
	 * Build a converter with a specific configuration and choice resolution
	 * strategy.
	 * 
	 * @param config         the converter's parameters
	 * @param beanClass      target bean class
	 * @param choiceStrategy strategy to select alternatives in choices
	 */
	public CobolBeanConverter(CobolConverterConfig config, Class<T> beanClass, CobolChoiceStrategy<T> choiceStrategy) {
		this.beanClass = beanClass;
		this.primitiveConverter = new CobolPrimitiveConverter(config);
		this.classInfo = new CobolClassInfoReflect();
		this.choiceStrategy = choiceStrategy;
	}

	/**
	 * Produce a Java bean consuming Cobol data from the input stream.
	 * <p>
	 * This can be invoked repeatedly over the same input stream.
	 * 
	 * @param cis the Cobol input data stream
	 * @return a Java bean instance
	 */
	public T toJava(CobolInputStream cis) {
		CobolBeanDeserializer<T> deserializer = choiceStrategy == null
				? new CobolBeanDeserializer<>(primitiveConverter, classInfo)
				: new CobolBeanDeserializer<>(primitiveConverter, classInfo, choiceStrategy);
		return deserializer.deserialize(cis, beanClass);
	}

	/**
	 * The input stream may contain data for more than one bean. This method
	 * provides a stream that will produce as many beans as possible from the input
	 * stream.
	 * 
	 * @param cis the Cobol input data stream
	 * @return a java.util.Stream of beans
	 */
	public Stream<T> toJavaAll(CobolInputStream cis) {
		Iterator<T> iter = new Iterator<>() {
			T nextBean = null;

			@Override
			public boolean hasNext() {
				if (nextBean != null) {
					return true;
				} else {
					try {
						nextBean = toJava(cis);
					} catch (CobolPrimitiveConverterEOFException e) {
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
	 * Serialize a bean as Cobol data.
	 * <p>
	 * This can be invoked repeatedly over the same output stream.
	 * 
	 * @param cobolOutputStream the Cobol output data stream
	 * @param bean              the bean to be serialized as Cobol data
	 */
	public void toCobol(CobolOutputStream cobolOutputStream, T bean) {
		CobolBeanSerializer serializer = new CobolBeanSerializer(primitiveConverter, classInfo);
		serializer.serialize(cobolOutputStream, bean);
	}

	/**
	 * Serialize a stream of beans as Cobol data.
	 * 
	 * @param cobolOutputStream the Cobol output data stream
	 * @param stream            stream of beans
	 */
	public void toCobolAll(CobolOutputStream cobolOutputStream, Stream<T> stream) {
		stream.forEach(b -> toCobol(cobolOutputStream, b));
	}
}
