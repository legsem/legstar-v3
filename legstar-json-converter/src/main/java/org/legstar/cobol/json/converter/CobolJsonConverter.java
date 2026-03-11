package org.legstar.cobol.json.converter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.legstar.cobol.converter.CobolBeanConverter;
import org.legstar.cobol.converter.CobolChoiceStrategy;
import org.legstar.cobol.converter.CobolInputStream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
public class CobolJsonConverter<T> {

	/**
	 * Converts cobol data to a java bean
	 */
	private final CobolBeanConverter<T> beanConverter;

	/**
	 * Converts a java bean to JSON
	 */
	private final ObjectMapper jsonMapper;

	/**
	 * Build a converter with default configuration parameters.
	 * 
	 * @param beanClass target bean class
	 */
	public CobolJsonConverter(Class<T> beanClass) {
		this(CobolJsonConverterConfig.ebcdic(), beanClass);
	}

	/**
	 * Build a converter.
	 * 
	 * @param config    the converter's parameters
	 * @param beanClass target bean class
	 */
	public CobolJsonConverter(CobolJsonConverterConfig config, Class<T> beanClass) {
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
	public CobolJsonConverter(CobolJsonConverterConfig config, Class<T> beanClass,
			CobolChoiceStrategy<T> choiceStrategy) {
		jsonMapper = new ObjectMapper();
		if (config.isFormattedOutput()) {
			jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
		}
		if (config.isOmitNullValues()) {
			jsonMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
		}
		if (config.isOmitEmptyArrays()) {
			jsonMapper.disable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
		}
		beanConverter = new CobolBeanConverter<>(config, beanClass, choiceStrategy);
	}

	/**
	 * Convert a single cobol data record to JSON
	 * 
	 * @param cis    the cobol input data
	 * @param writer the output JSON
	 */
	public void convert(CobolInputStream cis, Writer writer) {
		try {
			convert(cis, jsonMapper.createGenerator(writer));
		} catch (IOException e) {
			throw new CobolJsonConverterException(e);
		}
	}

	/**
	 * Convert all cobol data records to JSON
	 * 
	 * @param cis    the cobol input data
	 * @param writer the output JSON
	 */
	public void convertAll(CobolInputStream cis, Writer writer) {
		try {
			convertAll(cis, jsonMapper.createGenerator(writer));
		} catch (IOException e) {
			throw new CobolJsonConverterException(e);
		}
	}

	/**
	 * Convert a single cobol data record to JSON
	 * 
	 * @param cis the cobol input data
	 * @param os  the output JSON
	 */
	public void convert(CobolInputStream cis, OutputStream os) {
		try {
			convert(cis, jsonMapper.createGenerator(os));
		} catch (IOException e) {
			throw new CobolJsonConverterException(e);
		}
	}

	/**
	 * Convert all cobol data records to JSON
	 * 
	 * @param cis the cobol input data
	 * @param os  the output JSON
	 */
	public void convertAll(CobolInputStream cis, OutputStream os) {
		try {
			convertAll(cis, jsonMapper.createGenerator(os));
		} catch (IOException e) {
			throw new CobolJsonConverterException(e);
		}
	}

	/**
	 * Convert a single cobol data record to JSON
	 * 
	 * @param cis    the cobol input data
	 * @param generator the JSON generator
	 */
	public void convert(CobolInputStream cis, JsonGenerator generator) {
		T bean = beanConverter.convert(cis);
		toJson(bean, generator);
	}

	/**
	 * Convert all cobol data records to JSON
	 * 
	 * @param cis    the cobol input data
	 * @param generator the JSON generator
	 */
	public void convertAll(CobolInputStream cis, JsonGenerator generator) {
		beanConverter.convertAll(cis).forEach(b -> toJson(b, generator));
	}

	/**
	 * Given a bean with JSON annotations, produce the JSON.
	 * 
	 * @param bean   bean with JSON annotations
	 * @param generator the JSON generator
	 */
	public void toJson(T bean, JsonGenerator generator) {
		try {
			generator.writePOJO(bean);
		} catch (IOException e) {
			throw new CobolJsonConverterException(e);
		}
	}

}
