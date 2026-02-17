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
 * The target java bean class must hold cobol and JAXB annotations as produced by
 * legstar-jaxb-generator.
 * <p>
 * Thread safe and immutable.
 */
public class CobolXmlConverter<T> {

	private final CobolXmlConverterConfig config;

	private final JAXBContext jaxbContext;

	private final CobolBeanConverter<T> beanConverter;

	public CobolXmlConverter(Class<T> beanClass) {
		this(CobolXmlConverterConfig.ebcdic(), beanClass);
	}

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
		convert(cis, new StreamResult(writer));
	}

	public void convertAll(CobolInputStream cis, Writer writer) {
		convertAll(cis, new StreamResult(writer));
	}

	public void convert(CobolInputStream cis, OutputStream os) {
		convert(cis, new StreamResult(os));
	}

	public void convertAll(CobolInputStream cis, OutputStream os) {
		convertAll(cis, new StreamResult(os));
	}

	public void convert(CobolInputStream cis, Result result) {
		T bean = beanConverter.convert(cis);
		toXml(bean, result);
	}
	
	public void convertAll(CobolInputStream cis, Result result) {
		beanConverter.convertAll(cis).forEach(b -> toXml(b, result));
	}
	
	public void toXml(T bean, Result result) {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			if (config.isFormattedOutput()) {
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			}
			marshaller.marshal(bean, result);
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
