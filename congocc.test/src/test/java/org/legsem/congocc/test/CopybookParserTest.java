package org.legsem.congocc.test;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.legsem.cobol.copybook.model.CobolDataEntry;

public class CopybookParserTest extends TestBase {
	
	private static final Path COPYBOOKS = Paths.get("src/test/copybook");
	
	@Test
	public void parseALLTYPES() {
		check(parse("ALLTYPES"));
	}
	
	@Test
	public void parseSTRU01() {
		check(parse("STRU01"));
	}
	
	@Test
	public void parseARDO01() {
		check(parse("ARDO01"));
	}
	
	@Test
	public void parseARDO02() {
		check(parse("ARDO02"));
	}
	
	@Test
	public void parseARDO03() {
		check(parse("ARDO03"));
	}
	
	@Test
	public void parseARRAYGRP() {
		check(parse("ARRAYGRP"));
	}
	
	@Test
	public void parseARRAYSCX() {
		check(parse("ARRAYSCX"));
	}
	
	@Test
	public void parseARRAYSDO() {
		check(parse("ARRAYSDO"));
	}
	
	@Test
	public void parseARRAYSSM() {
		check(parse("ARRAYSSM"));
	}
	
	@Test
	public void parseBINARCHT() {
		check(parse("BINARCHT"));
	}
	
	@Test
	public void parseBINNATSI() {
		check(parse("BINNATSI"));
	}
	
	@Test
	public void parseBINNATUS() {
		check(parse("BINNATUS"));
	}
	
	@Test
	public void parseBINPKDUS() {
		check(parse("BINPKDUS"));
	}
	
	@Test
	public void parseCHARSETS() {
		check(parse("CHARSETS"));
	}
	
	@Test
	public void parseCUSTDAT() {
		check(parse("CUSTDAT"));
	}
	
	private String parse(String source) {
		CopybookParser parser = new CopybookParser();
		List<CobolDataEntry> entries = parser.parse(getTestName(), getReader(source));
		StringBuilder sb = new StringBuilder();
		sb.append("[\n");
		sb.append(entries.stream() //
				.map(en -> en.toString(2)) //
				.collect(Collectors.joining ("\n")));
		sb.append("\n]");
		return sb.toString();
	}

	private Reader getReader(String source) {
		try {
			Path sourcePath = COPYBOOKS.resolve(source);
			return Files.newBufferedReader(sourcePath, StandardCharsets.ISO_8859_1);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
