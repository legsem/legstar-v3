package org.legstar.cobol.converter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import legstar.samples.custdat.CustomerData;

public class CobolBeanConverterRecfmVTest extends CobolConverterTestBase {
	
	@Test
	public void testCustomerDataRecfmV() throws IOException {
		Path filePath = Paths.get("src/test/data/ZOS.FCUSTDAT.RDW.bin");
		FileInputStream fis = new FileInputStream(filePath.toFile());
		CobolInputStream cis = new CobolInputStream(fis, CobolRecordFormat.V);
		CobolBeanConverterConfig config = CobolBeanConverterConfig.ebcdic();
		CobolBeanConverter<CustomerData> fromHost = new CobolBeanConverter<>(config, CustomerData.class);
		CustomerData output = null;
		while (cis.available() > 0) {
			output = fromHost.convert(cis);
		}
		check(Objects.toString(output));
	}

}
