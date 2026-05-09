package org.legstar.cobol.converter;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.legstar.cobol.annotation.CobolBinaryNumber;
import org.legstar.cobol.annotation.CobolDouble;
import org.legstar.cobol.annotation.CobolFloat;
import org.legstar.cobol.annotation.CobolPackedDecimal;
import org.legstar.cobol.annotation.CobolString;
import org.legstar.cobol.annotation.CobolZonedDecimal;
import org.legstar.cobol.io.CobolOutputStream;

public class CobolPrimitiveSerializer extends CobolPrimitiveConverter {

	public CobolPrimitiveSerializer(CobolConverterConfig config) {
		super(config);
	}

	public void serialize(CobolOutputStream cos, Annotation annotation, Object value) {
		try {
			byte[] buffer = null;
			if (annotation instanceof CobolString) {
				buffer = convertString((CobolString) annotation, value);
			} else if (annotation instanceof CobolBinaryNumber) {
				buffer = convertBinary((CobolBinaryNumber) annotation, value);
			} else if (annotation instanceof CobolZonedDecimal) {
				buffer = convertZonedDecimal((CobolZonedDecimal) annotation, value);
			} else if (annotation instanceof CobolPackedDecimal) {
				buffer = convertPackedDecimal((CobolPackedDecimal) annotation, value);
			} else if (annotation instanceof CobolFloat) {
				buffer = convertFloat((CobolFloat) annotation, value);
			} else if (annotation instanceof CobolDouble) {
				buffer = convertDouble((CobolDouble) annotation, value);
			} else {
				throw new CobolBeanConverterException("Unsupported Cobol annotation " + annotation);
			}
			cos.write(buffer);
		} catch (IOException e) {
			// TODO generate qualified exception
			e.printStackTrace();
		}
	}

}
