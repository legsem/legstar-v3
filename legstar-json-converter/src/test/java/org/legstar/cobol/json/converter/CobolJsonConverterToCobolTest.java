package org.legstar.cobol.json.converter;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HexFormat;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.base.test.CobolTestBase;
import org.legstar.cobol.io.CobolOutputStream;

import legstar.samples.json.alltypes.Alltypes;
import legstar.samples.json.ardo01.Ardo01Record;
import legstar.samples.json.ardo04.Ardo04Record;
import legstar.samples.json.custdat.CustomerData;
import legstar.samples.json.flat01.Flat01Record;
import legstar.samples.json.flat02.Flat02Record;
import legstar.samples.json.optl01.Optl01Record;
import legstar.samples.json.rdef01.Rdef01Record;
import legstar.samples.json.rdef06.Rdef06Record;
import legstar.samples.json.rdef07.Rdef07Record;

public class CobolJsonConverterToCobolTest extends CobolTestBase {

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
		Path xmlPath = DATA.resolve(getTestName() + ".json");
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				CobolOutputStream cos = new CobolOutputStream(baos)) {
			CobolJsonConverterConfig config = CobolJsonConverterConfig.ebcdic();
			CobolJsonConverter<T> converter = new CobolJsonConverter<>(config, beanClass);
			converter.toCobol(cos, xmlPath.toFile());
			return HexFormat.of().formatHex(baos.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
