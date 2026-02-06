package org.legstar.cobol.jaxb.converter;

import java.io.Writer;

import org.legstar.cobol.converter.CobolBeanConverter;
import org.legstar.cobol.converter.CobolChoiceStrategy;
import org.legstar.cobol.converter.CobolInputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

/**
 * Converts cobol data to XML using JAXB.
 * <p>
 * The target java bean class must hold cobol and JAXB annotations as produced by
 * legstar-jaxb-generator.
 * <p>
 * Thread safe and immutable.
 */
public class CobolXmlConverter<T> {

	private final CobolXmlConverterConfig config;

	private final JAXBContext jaxbContext;

	private final CobolBeanConverter<T> beanConverter;

	public CobolXmlConverter(CobolXmlConverterConfig config, Class<T> beanClass) {
		this(config, beanClass, null);
	}

	public CobolXmlConverter(CobolXmlConverterConfig config, Class<T> beanClass,
			CobolChoiceStrategy<T> choiceStrategy) {
		this.config = config;
		jaxbContext = newJAXBContext(beanClass);
		beanConverter = new CobolBeanConverter<>(config, beanClass, choiceStrategy);
	}

	public void convert(CobolInputStream cis, Writer writer) {
		T bean = beanConverter.convert(cis);
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(bean, writer);
		} catch (JAXBException e) {
			throw new CobolXmlConverterException(e);
		}
	}

	private static <T> JAXBContext newJAXBContext(Class<T> beanClass) {
		try {
			return JAXBContext.newInstance(beanClass);
		} catch (JAXBException e) {
			throw new CobolXmlConverterException(e);
		}
	}
}
