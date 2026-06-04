package org.legstar.cobol.json.converter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import org.legstar.cobol.converter.CobolBeanConverter;
import org.legstar.cobol.converter.CobolChoiceStrategy;
import org.legstar.cobol.io.CobolInputStream;
import org.legstar.cobol.io.CobolOutputStream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
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
	 * The intermediary java bean class.
	 */
	private final Class<T> beanClass;

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
		this.beanClass = beanClass;
	}

	/**
	 * Convert a single cobol data record to JSON
	 * 
	 * @param cis    the cobol input data
	 * @param writer the output JSON
	 */
	public void toJson(CobolInputStream cis, Writer writer) {
		try {
			toJson(cis, jsonMapper.createGenerator(writer));
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
	public void toJsonAll(CobolInputStream cis, Writer writer) {
		try {
			toJsonAll(cis, jsonMapper.createGenerator(writer));
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
	public void toJson(CobolInputStream cis, OutputStream os) {
		try {
			toJson(cis, jsonMapper.createGenerator(os));
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
	public void toJsonAll(CobolInputStream cis, OutputStream os) {
		try {
			toJsonAll(cis, jsonMapper.createGenerator(os));
		} catch (IOException e) {
			throw new CobolJsonConverterException(e);
		}
	}

	/**
	 * Read a JSON file and convert to Cobol.
	 * 
	 * @param cobolOutputStream the Cobol output data stream
	 * @param file            JSON file
	 */
	public void toCobol(CobolOutputStream cobolOutputStream, File file) {
		try {
			toCobol(cobolOutputStream, jsonMapper.createParser(file));
		} catch (IOException e) {
			throw new CobolJsonConverterException(e);
		}
	}

	/**
	 * Read a JSON and convert to Cobol.
	 * 
	 * @param cobolOutputStream the Cobol output data stream
	 * @param reader            reads a JSON
	 */
	public void toCobol(CobolOutputStream cobolOutputStream, Reader reader) {
		try {
			toCobol(cobolOutputStream, jsonMapper.createParser(reader));
		} catch (IOException e) {
			throw new CobolJsonConverterException(e);
		}
	}

	/**
	 * Read a JSON stream and convert to Cobol.
	 * 
	 * @param cobolOutputStream the Cobol output data stream
	 * @param is                a JSON input stream
	 */
	public void toCobol(CobolOutputStream cobolOutputStream, InputStream is) {
		try {
			toCobol(cobolOutputStream, jsonMapper.createParser(is));
		} catch (IOException e) {
			throw new CobolJsonConverterException(e);
		}
	}

	/**
	 * Convert a single cobol data record to JSON
	 * 
	 * @param cis       the cobol input data
	 * @param generator the JSON generator
	 */
	private void toJson(CobolInputStream cis, JsonGenerator generator) {
		T bean = beanConverter.toJava(cis);
		toJson(bean, generator);
	}

	/**
	 * Convert all cobol data records to JSON
	 * 
	 * @param cis       the cobol input data
	 * @param generator the JSON generator
	 */
	private void toJsonAll(CobolInputStream cis, JsonGenerator generator) {
		beanConverter.toJavaAll(cis).forEach(b -> toJson(b, generator));
	}

	/**
	 * Given a bean with JSON annotations, produce the JSON.
	 * 
	 * @param bean      bean with JSON annotations
	 * @param generator the JSON generator
	 */
	private void toJson(T bean, JsonGenerator generator) {
		try {
			generator.writePOJO(bean);
		} catch (IOException e) {
			throw new CobolJsonConverterException(e);
		}
	}

	/**
	 * Given a JSON parser, convert JSON to Cobol.
	 * 
	 * @param cobolOutputStream the Cobol output data stream
	 * @param parser            the JSON parser
	 */
	private void toCobol(CobolOutputStream cobolOutputStream, JsonParser parser) {
		beanConverter.toCobol(cobolOutputStream, toJava(parser));
	}

	/**
	 * Given a JSON parser, produce a java bean.
	 * 
	 * @param parser the JSON parser
	 * @return a java bean instance
	 */
	private T toJava(JsonParser parser) {
		try {
			return parser.readValueAs(beanClass);
		} catch (IOException e) {
			throw new CobolJsonConverterException(e);
		}
	}

}
