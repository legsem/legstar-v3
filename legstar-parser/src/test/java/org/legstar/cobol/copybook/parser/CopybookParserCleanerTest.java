package org.legstar.cobol.copybook.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.legstar.cobol.copybook.parser.CopybookParserCleaner.Result;

public class CopybookParserCleanerTest {

	@Test
	public void noBlanksAndComments() {
		StringReader reader = new StringReader("""
				      * A Comment on col 7
				      / Another Comment on col 7
				                   *> A floating comment
				""");
		CopybookParserCleaner c = new CopybookParserCleaner();
		Result res = c.clean(reader);
		assertEquals("", res.toString());
	}

	@Test
	public void noIdentificationDivision() {
		StringReader reader = new StringReader("""
				       01 MYVAR.
				""");
		CopybookParserCleaner c = new CopybookParserCleaner();
		Result res = c.clean(reader);
		assertEquals("001        01 MYVAR.\n", res.toString());
	}

	@Test
	public void identificationDivision() {
		StringReader reader = new StringReader("""
				       IDENTIFICATION DIVISION.
				       01 MYVAR.
				""");
		CopybookParserCleaner c = new CopybookParserCleaner();
		Result res = c.clean(reader);
		assertEquals("", res.toString());
	}

	@Test
	public void workingStorageSection() {
		StringReader reader = new StringReader("""
				       IDENTIFICATION DIVISION.
				       DATA DIVISION.
				       WORKING-STORAGE SECTION.
				       01 MYVAR.
				""");
		CopybookParserCleaner c = new CopybookParserCleaner();
		Result res = c.clean(reader);
		assertEquals("004        01 MYVAR.\n", res.toString());
	}

	@Test
	public void localStorageSection() {
		StringReader reader = new StringReader("""
				       IDENTIFICATION DIVISION.
				       DATA DIVISION.
				       LOCAL-STORAGE SECTION.
				       01 MYVAR.
				""");
		CopybookParserCleaner c = new CopybookParserCleaner();
		Result res = c.clean(reader);
		assertEquals("004        01 MYVAR.\n", res.toString());
	}

	@Test
	public void linkageSection() {
		StringReader reader = new StringReader("""
				       IDENTIFICATION DIVISION.
				       DATA DIVISION.
				       LINKAGE SECTION.
				       01 MYVAR.
				""");
		CopybookParserCleaner c = new CopybookParserCleaner();
		Result res = c.clean(reader);
		assertEquals("004        01 MYVAR.\n", res.toString());
	}

	@Test
	public void procedureDivision() {
		StringReader reader = new StringReader("""
				       IDENTIFICATION DIVISION.
				       DATA DIVISION.
				       PROCEDURE DIVISION.
				       01 MYVAR.
				""");
		CopybookParserCleaner c = new CopybookParserCleaner();
		Result res = c.clean(reader);
		assertEquals("", res.toString());
	}

	@Test
	public void removeExtraneousCharacters() {
		StringReader reader = new StringReader("""
				123456701 MYVAR.                                                         1234567
				""");

		CopybookParserCleaner c = new CopybookParserCleaner();
		Result res = c.clean(reader);
		assertEquals("001       701 MYVAR.\n", res.toString());
	}

	@Test
	public void removeDebugLines() {
		StringReader reader = new StringReader("""
				123456D01 MYVAR.
				123456d01 MYVAR.
				""");

		CopybookParserCleaner c = new CopybookParserCleaner();
		Result res = c.clean(reader);
		assertEquals("", res.toString());
	}

	@Test
	public void removeCompilerDirectives() {
		StringReader reader = new StringReader("""
				EJECT
				SKIP.
				SKIP1
				SKIP2.
				SKIP3
				""");

		CopybookParserCleaner c = new CopybookParserCleaner();
		Result res = c.clean(reader);
		assertEquals("", res.toString());
	}

	@Test
	public void replaceLongSeparators() {
		StringReader reader = new StringReader("""
				123456 01 A, PIC; X(5), VALUE, 5.
				       01 A  PIC  X(5)  VALUE  'A''B", C'.
				""");

		CopybookParserCleaner c = new CopybookParserCleaner();
		Result res = c.clean(reader);
		assertEquals("001        01 A  PIC  X(5)  VALUE  5.\n002        01 A  PIC  X(5)  VALUE  'A''B\", C'.\n",
				res.toString());
	}

	@Test
	public void aggregateAlphanumContinuationLiteral() {
		StringReader reader = new StringReader("""
				       01 MYVAR PIC X(120) VALUE
				000001               "AAAAAAAAAABBBBBBBBBBCCCCCCCCCCDDDDDDDDDDEEEEEEEEEE
				      -              "GGGGGGGGGGHHHHHHHHHHIIIIIIIIIIJJJJJJJJJJKKKKKKKKKK
				      -    "LLLLLLLLLLMMMMMMMMMM"
				""");

		CopybookParserCleaner c = new CopybookParserCleaner();
		Result res = c.clean(reader);
		assertEquals("001        01 MYVAR PIC X(120) VALUE\n"
				+ "002                      \"AAAAAAAAAABBBBBBBBBBCCCCCCCCCCDDDDDDDDDDEEEEEEEEEEGGGGGGGGGGHHHHHHHHHHIIIIIIIIIIJJJJJJJJJJKKKKKKKKKKLLLLLLLLLLMMMMMMMMMM\"\n",
				res.toString());
	}
}
