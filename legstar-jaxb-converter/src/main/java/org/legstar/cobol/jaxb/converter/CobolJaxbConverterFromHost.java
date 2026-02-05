package org.legstar.cobol.jaxb.converter;

import java.io.Writer;

import org.glassfish.jaxb.runtime.v2.runtime.JAXBContextImpl;
import org.legstar.cobol.converter.CobolConverterFromHost;
import org.legstar.cobol.converter.CobolConverterFromHostChoiceStrategy;
import org.legstar.cobol.converter.CobolConverterInputStream;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class CobolJaxbConverterFromHost<T> {
	
	private final CobolJaxbConverterConfig config;
	
	private final CobolConverterFromHost<T> beanConverter;
	
	public CobolJaxbConverterFromHost(CobolJaxbConverterConfig config) {
		this(config, null);
	}

	public CobolJaxbConverterFromHost(CobolJaxbConverterConfig config, CobolConverterFromHostChoiceStrategy<T> choiceStrategy) {
		this.config = config;
		beanConverter = new CobolConverterFromHost<>(config, choiceStrategy);
	}

	public void convert(CobolConverterInputStream cis, Class<T> beanClass, Writer writer) {
		T bean = beanConverter.convert(cis, beanClass);
		try {
			JAXBContextImpl jaxbContext = new JAXBContextImpl.JAXBContextBuilder() //
					.setClasses(new Class[] {beanClass}) //
					.build();
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(bean, writer);
		} catch (JAXBException e) {
			throw new CobolJaxbConverterException(e);
		}
	}
}
