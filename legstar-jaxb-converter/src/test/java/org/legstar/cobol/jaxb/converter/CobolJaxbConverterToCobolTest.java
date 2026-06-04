package org.legstar.cobol.jaxb.converter;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HexFormat;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.base.test.CobolTestBase;
import org.legstar.cobol.io.CobolOutputStream;

import legstar.samples.jaxb.ardo01.Ardo01Record;
import legstar.samples.jaxb.ardo04.Ardo04Record;
import legstar.samples.jaxb.flat01.Flat01Record;
import legstar.samples.jaxb.flat02.Flat02Record;
import legstar.samples.jaxb.alltypes.Alltypes;
import legstar.samples.jaxb.custdat.CustomerData;
import legstar.samples.jaxb.optl01.Optl01Record;
import legstar.samples.jaxb.rdef01.Rdef01Record;
import legstar.samples.jaxb.rdef06.Rdef06Record;
import legstar.samples.jaxb.rdef07.Rdef07Record;

public class CobolJaxbConverterToCobolTest extends CobolTestBase {

	private static final Path DATA = Paths.get("src/test/data");

	@Test
	public void testArdo01() {
		check(toCobol(Ardo01Record.class));
	}

	@Test
	public void testArdo04() {
		check(toCobol(Ardo04Record.class));
	}

	@Test
	public void testFlat01() {
		check(toCobol(Flat01Record.class));
	}

	@Test
	public void testFlat02() {
		check(toCobol(Flat02Record.class));
	}

	@Test
	public void testAlltypes() {
		check(toCobol(Alltypes.class));
	}

	@Test
	public void testCustomerData() {
		check(toCobol(CustomerData.class));
	}

	@Test
	public void testOptl01() {
		check(toCobol(Optl01Record.class));
	}

	@Test
	public void testRdef01() {
		check(toCobol(Rdef01Record.class));
	}

	@Test
	public void testRdef06() {
		check(toCobol(Rdef06Record.class));
	}

	@Test
	public void testRdef07() {
		check(toCobol(Rdef07Record.class));
	}

	private <T> String toCobol(Class<T> beanClass) {
		Path xmlPath = DATA.resolve(getTestName() + ".xml");
		Source source = new StreamSource(xmlPath.toFile());
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				CobolOutputStream cos = new CobolOutputStream(baos)) {
			CobolJaxbConverterConfig config = CobolJaxbConverterConfig.ebcdic();
			CobolJaxbConverter<T> converter = new CobolJaxbConverter<>(config, beanClass);
			converter.toCobol(cos, source);
			return HexFormat.of().formatHex(baos.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
