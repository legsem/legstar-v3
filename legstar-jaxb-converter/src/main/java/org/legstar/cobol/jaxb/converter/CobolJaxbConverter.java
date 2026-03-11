package org.legstar.cobol.jaxb.converter;

import java.io.OutputStream;
import java.io.Writer;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.legstar.cobol.converter.CobolBeanConverter;
import org.legstar.cobol.converter.CobolChoiceStrategy;
import org.legstar.cobol.converter.CobolInputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

/**
 * Converts cobol data to XML using JAXB.
 * <p>
 * The target java bean class must hold cobol and JAXB annotations as produced
 * by legstar-jaxb-generator.
 * <p>
 * Thread safe.
 * 
 * @param <T> target bean class type
 */
public class CobolJaxbConverter<T> {

	/**
	 * Configuration parameters
	 */
	private final CobolJaxbConverterConfig config;

	/**
	 * Converts cobol data to a java bean
	 */
	private final CobolBeanConverter<T> beanConverter;

	/**
	 * Converts a java bean to XML
	 */
	private final JAXBContext jaxbContext;

	/**
	 * Build a converter with default configuration parameters.
	 * 
	 * @param beanClass target bean class
	 */
	public CobolJaxbConverter(Class<T> beanClass) {
		this(CobolJaxbConverterConfig.ebcdic(), beanClass);
	}

	/**
	 * Build a converter.
	 * 
	 * @param config    the converter's parameters
	 * @param beanClass target bean class
	 */
	public CobolJaxbConverter(CobolJaxbConverterConfig config, Class<T> beanClass) {
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
	public CobolJaxbConverter(CobolJaxbConverterConfig config, Class<T> beanClass,
			CobolChoiceStrategy<T> choiceStrategy) {
		this.config = config;
		jaxbContext = newJAXBContext(beanClass);
		beanConverter = new CobolBeanConverter<T>(config, beanClass, choiceStrategy);
	}

	/**
	 * Convert a single cobol data record to XML
	 * 
	 * @param cis    the cobol input data
	 * @param writer the output XML
	 */
	public void convert(CobolInputStream cis, Writer writer) {
		convert(cis, new StreamResult(writer));
	}

	/**
	 * Convert all cobol data records to XML
	 * 
	 * @param cis    the cobol input data
	 * @param writer the output XML
	 */
	public void convertAll(CobolInputStream cis, Writer writer) {
		convertAll(cis, new StreamResult(writer));
	}

	/**
	 * Convert a single cobol data record to XML
	 * 
	 * @param cis the cobol input data
	 * @param os  the output XML
	 */
	public void convert(CobolInputStream cis, OutputStream os) {
		convert(cis, new StreamResult(os));
	}

	/**
	 * Convert all cobol data records to XML
	 * 
	 * @param cis the cobol input data
	 * @param os  the output XML
	 */
	public void convertAll(CobolInputStream cis, OutputStream os) {
		convertAll(cis, new StreamResult(os));
	}

	/**
	 * Convert a single cobol data record to XML
	 * 
	 * @param cis    the cobol input data
	 * @param result the output XML
	 */
	public void convert(CobolInputStream cis, Result result) {
		T bean = beanConverter.convert(cis);
		toXml(bean, result);
	}

	/**
	 * Convert all cobol data records to XML
	 * 
	 * @param cis    the cobol input data
	 * @param result the output XML
	 */
	public void convertAll(CobolInputStream cis, Result result) {
		beanConverter.convertAll(cis).forEach(b -> toXml(b, result));
	}

	/**
	 * Given a bean with JAXB annotations, produce the XML.
	 * 
	 * @param bean   bean with JAXB annotations
	 * @param result the output XML
	 */
	public void toXml(T bean, Result result) {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			if (config.isFormattedOutput()) {
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			}
			marshaller.marshal(bean, result);
		} catch (JAXBException e) {
			throw new CobolJaxbConverterException(e);
		}
	}

	/**
	 * Create a JAXBContext.
	 * <p>
	 * This is expensive.
	 * 
	 * @param <T>       target bean class type
	 * @param beanClass target bean class
	 * @return a new JAXBContext
	 */
	private static <T> JAXBContext newJAXBContext(Class<T> beanClass) {
		try {
			return JAXBContext.newInstance(beanClass);
		} catch (JAXBException e) {
			throw new CobolJaxbConverterException(e);
		}
	}
}
