package org.legstar.cobol.type.converter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import legstar.samples.custdat.CustomerData;

public class CobolConverterFromHostRecfmVTest extends CobolConverterTestBase {
	
	@Test
	public void testCustomerDataRecfmV() throws IOException {
		Path filePath = Paths.get("src/test/data/ZOS.FCUSTDAT.RDW.bin");
		FileInputStream fis = new FileInputStream(filePath.toFile());
		CobolConverterInputStream cis = new CobolConverterInputStream(fis, HostDataRecordFormat.V);
		CobolConverterConfig config = CobolConverterConfig.ebcdic();
		CobolConverterFromHost<CustomerData> fromHost = new CobolConverterFromHost<>(config);
		CustomerData output = null;
		while (cis.available() > 0) {
			output = fromHost.convert(cis, CustomerData.class);
		}
		check(Objects.toString(output));
	}

}
