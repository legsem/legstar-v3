package org.legstar.cobol.jaxb.converter;

import java.io.OutputStream;
import java.io.Writer;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import org.legstar.cobol.converter.CobolBeanConverter;
import org.legstar.cobol.converter.CobolChoiceStrategy;
import org.legstar.cobol.io.CobolInputStream;
import org.legstar.cobol.io.CobolOutputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

/**
 * Converts Cobol data to and from XML using JAXB.
 * <p>
 * Involves an intermediary java bean which holds both Cobol and JAXB
 * annotations as produced by legstar-jaxb-generator.
 * <p>
 * Thread safe.
 * 
 * @param <T> Intermediary java bean class type
 */
public class CobolJaxbConverter<T> {

	/**
	 * Configuration parameters
	 */
	private final CobolJaxbConverterConfig config;

	/**
	 * The intermediary java bean class.
	 */
	private final Class<T> beanClass;

	/**
	 * Converts Cobol data to and from an intermediary java bean
	 */
	private final CobolBeanConverter<T> beanConverter;

	/**
	 * Converts a java bean to and from XML
	 */
	private final JAXBContext jaxbContext;

	/**
	 * Build a converter with default configuration parameters.
	 * 
	 * @param beanClass intermediary java bean class
	 */
	public CobolJaxbConverter(Class<T> beanClass) {
		this(CobolJaxbConverterConfig.ebcdic(), beanClass);
	}

	/**
	 * Build a converter.
	 * 
	 * @param config    the converter's parameters
	 * @param beanClass intermediary java bean class
	 */
	public CobolJaxbConverter(CobolJaxbConverterConfig config, Class<T> beanClass) {
		this(config, beanClass, null);
	}

	/**
	 * Build a converter.
	 * 
	 * @param config         the converter's parameters
	 * @param beanClass      intermediary java bean class
	 * @param choiceStrategy strategy to select alternatives in choices. If null,
	 *                       the default strategy is applied
	 */
	public CobolJaxbConverter(CobolJaxbConverterConfig config, Class<T> beanClass,
			CobolChoiceStrategy<T> choiceStrategy) {
		this.config = config;
		this.beanClass = beanClass;
		jaxbContext = newJAXBContext(beanClass);
		beanConverter = new CobolBeanConverter<T>(config, beanClass, choiceStrategy);
	}

	/**
	 * Convert a single cobol data record to XML
	 * 
	 * @param cis    the cobol input data
	 * @param writer the output XML
	 */
	public void toXml(CobolInputStream cis, Writer writer) {
		toXml(cis, new StreamResult(writer));
	}

	/**
	 * Convert all cobol data records to XML
	 * 
	 * @param cis    the cobol input data
	 * @param writer the output XML
	 */
	public void toXmlAll(CobolInputStream cis, Writer writer) {
		toXmlAll(cis, new StreamResult(writer));
	}

	/**
	 * Convert a single cobol data record to XML
	 * 
	 * @param cis the cobol input data
	 * @param os  the output XML
	 */
	public void toXml(CobolInputStream cis, OutputStream os) {
		toXml(cis, new StreamResult(os));
	}

	/**
	 * Convert all cobol data records to XML
	 * 
	 * @param cis the cobol input data
	 * @param os  the output XML
	 */
	public void toXmlAll(CobolInputStream cis, OutputStream os) {
		toXmlAll(cis, new StreamResult(os));
	}

	/**
	 * Convert a single cobol data record to XML
	 * 
	 * @param cis    the cobol input data
	 * @param result the output XML
	 */
	public void toXml(CobolInputStream cis, Result result) {
		T bean = beanConverter.toJava(cis);
		toXml(bean, result);
	}

	/**
	 * Convert all cobol data records to XML
	 * 
	 * @param cis    the cobol input data
	 * @param result the output XML
	 */
	public void toXmlAll(CobolInputStream cis, Result result) {
		beanConverter.toJavaAll(cis).forEach(b -> toXml(b, result));
	}

	/**
	 * Convert an XML source to Cobol.
	 * 
	 * @param cobolOutputStream the Cobol output data stream
	 * @param source the XML source
	 */
	public void toCobol(CobolOutputStream cobolOutputStream, Source source) {
		beanConverter.toCobol(cobolOutputStream, toJava(source));
	}
	
	/**
	 * Given a bean with JAXB annotations, produce the XML.
	 * 
	 * @param bean   bean with JAXB annotations
	 * @param result the output XML
	 */
	private void toXml(T bean, Result result) {
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
	 * Given an XML source, produce a java bean.
	 * 
	 * @param source the XML source
	 * @return a java bean instance
	 */
	private T toJava(Source source) {
		try {
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return unmarshaller.unmarshal(source, beanClass).getValue();
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
