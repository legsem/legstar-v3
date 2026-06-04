package org.legstar.cobol.converter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.base.test.CobolTestBase;
import org.legstar.cobol.io.CobolInputStream;
import org.legstar.cobol.io.CobolRecordFormat;

import legstar.samples.custdat.CustomerData;

public class CobolBeanConverterRecfmVTest extends CobolTestBase {
	
	@Test
	public void testCustomerDataRecfmV() throws IOException {
		Path filePath = Paths.get("src/test/data/ZOS.FCUSTDAT.RDW.bin");
		FileInputStream fis = new FileInputStream(filePath.toFile());
		CobolInputStream cis = new CobolInputStream(fis, CobolRecordFormat.V);
		CobolBeanConverter<CustomerData> converter = new CobolBeanConverter<>(CustomerData.class);
		CustomerData output = null;
		boolean eof = false;
		while (!eof) {
			try {
				output = converter.toJava(cis);
			} catch (CobolPrimitiveConverterEOFException e) {
				eof = true;
			}
		}
		check(Objects.toString(output));
	}

}
