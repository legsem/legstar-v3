package org.legstar.cobol.copybook.parser;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.data.entry.CobolDataEntry;

public class CopybookParserTest extends CopybookParserTestBase {
	
	private static final Path COPYBOOKS = Paths.get("src/test/copybook");
	
	@Test
	public void parseALLTYPES() {
		check(parse("ALLTYPES"));
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
	
	@Test
	public void parseDIGITNAME() {
		check(parse("DIGITNAME"));
	}
	
	@Test
	public void parseDOUBLMIX() {
		check(parse("DOUBLMIX"));
	}
	
	@Test
	public void parseDPLARCHT() {
		check(parse("DPLARCHT"));
	}
	
	@Test
	public void parseFIXARCOM() {
		check(parse("FIXARCOM"));
	}
	
	@Test
	public void parseFIXARNUM() {
		check(parse("FIXARNUM"));
	}
	
	@Test
	public void parseFIXARSIM() {
		check(parse("FIXARSIM"));
	}
	
	@Test
	public void parseFLAT01() {
		check(parse("FLAT01"));
	}
	
	@Test
	public void parseFLAT02() {
		check(parse("FLAT02"));
	}
	
	@Test
	public void parseFLOATMIX() {
		check(parse("FLOATMIX"));
	}
	
	@Test
	public void parseFREEFORM() {
		check(parse("FREEFORM", new CopybookParserConfig().setFreeCodeFormat(true)));
	}
	
	@Test
	public void parseLSFILEAC() {
		check(parse("LSFILEAC"));
	}
	
	@Test
	public void parseLSFILEAD() {
		check(parse("LSFILEAD"));
	}
	
	@Test
	public void parseLSFILEAE() {
		check(parse("LSFILEAE"));
	}
	
	@Test
	public void parseLSFILEAL() {
		check(parse("LSFILEAL"));
	}
	
	@Test
	public void parseLSFILEAQ() {
		check(parse("LSFILEAQ"));
	}
	
	@Test
	public void parseNUMZONED() {
		check(parse("NUMZONED"));
	}
	
	@Test
	public void parseOPTL01() {
		check(parse("OPTL01"));
	}
	
	@Test
	public void parseOSARRAYS() {
		check(parse("OSARRAYS"));
	}
	
	@Test
	public void parseRDEF01() {
		check(parse("RDEF01"));
	}
	
	@Test
	public void parseRDEF02() {
		check(parse("RDEF02"));
	}
	
	@Test
	public void parseRDEF03() {
		check(parse("RDEF03"));
	}
	
	@Test
	public void parseRDEF04() {
		check(parse("RDEF04"));
	}
	
	@Test
	public void parseREDBOTHA() {
		check(parse("REDBOTHA"));
	}
	
	@Test
	public void parseREDINOUT() {
		check(parse("REDINOUT"));
	}
	
	@Test
	public void parseREDMULTI() {
		check(parse("REDMULTI"));
	}
	
	@Test
	public void parseREDOPERA() {
		check(parse("REDOPERA"));
	}
	
	@Test
	public void parseREDSIMPT() {
		check(parse("REDSIMPT"));
	}
	
	@Test
	public void parseREDTRANS() {
		check(parse("REDTRANS"));
	}
	
	@Test
	public void parseSTRU01() {
		check(parse("STRU01"));
	}
	
	@Test
	public void parseSTRU03() {
		check(parse("STRU03"));
	}
	
	@Test
	public void parseSTRU04() {
		check(parse("STRU04"));
	}
	
	@Test
	public void parseSTRU05() {
		check(parse("STRU05"));
	}
	
	@Test
	public void parseT1CONTXT() {
		check(parse("T1CONTXT"));
	}
	
	@Test
	public void parseT1DATE01() {
		check(parse("T1DATE01"));
	}
	
	@Test
	public void parseTCOBWVB() {
		check(parse("TCOBWVB"));
	}
	
	@Test
	public void parseTYPESMIX() {
		check(parse("TYPESMIX"));
	}
	
	@Test
	public void parseVALUEMIX() {
		check(parse("VALUEMIX"));
	}
	
	@Test
	public void parseVARAR021() {
		check(parse("VARAR021"));
	}
	
	@Test
	public void parseVARARCOM() {
		check(parse("VARARCOM"));
	}
	
	private String parse(String source) {
		return parse(source, new CopybookParserConfig());
	}

	private String parse(String source, CopybookParserConfig config) {
		CopybookParser parser = new CopybookParser(config);
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
