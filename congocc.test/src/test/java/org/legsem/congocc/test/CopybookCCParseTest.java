package org.legsem.congocc.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.legstar.copybook.parser.CopybookCCParser;
import org.legstar.copybook.parser.Node;
import org.legstar.copybook.parser.ParseException;

public class CopybookCCParseTest {

	private String testName;

	@BeforeEach
	void setUp(TestInfo testInfo) {
		String s = testInfo.getDisplayName();
		testName = s.substring(0, s.indexOf("("));
	}

	@Test
	public void parseOneEmpty() {
		check(parse(""));
	}

	@Test
	public void parseNoDataName() {
		check(parse("01."));
	}

	@Test
	public void parseOneRecord() {
		check(parse("01 MYVAR."));
	}

	@Test
	public void parseTwoRecords() {
		check(parse("01 RECORD1.\n01 RECORD2."));
	}

	@Test
	public void parseOneElementary() {
		check(parse("01 MYVAR PIC X."));
	}

	@Test
	public void parseOne77Elementary() {
		check(parse("77 MYVAR PIC X."));
	}

	@Test
	public void parseOneExternalFloatingPoint() {
		check(parse("01 MYVAR PIC -9v9(9)E-99."));
	}

	@Test
	public void parsePictureClause() {
		check(parse("""
				01 MYVAR PIC X.
				01 MYVAR PIC 9.9.
				01 MYVAR PIC 99.
				01 MYVAR PIC 9999.
				01 MYVAR PIC S99.
				01 MYVAR PIC S999V9.
				01 MYVAR PIC PPP999.
				01 MYVAR PIC S999PPP.
				01 MYVAR PIC $99999.99CR.
				01 MYVAR PIC $9(5).9(2)CR.
				01 MYVAR PIC ****.**.
				01 MYVAR PIC *.*.
				01 MYVAR PIC ZZZZ.99.
				01 MYVAR PIC Z,ZZZ.ZZ+.
				01 MYVAR PIC $Z,ZZZ,ZZZ.ZZCR.
				01 MYVAR PIC $B*,***,***.**BBDB.
				01 MYVAR PIC X(10)/XX .
				01 MYVAR PIC X(5)BX(7).
				01 MYVAR PIC GGBBGG .
				01 MYVAR PIC +999.99E+99.
				01 MYVAR PIC -$$,$$$,$$$.99CR.
								"""));
	}

	@Test
	public void parseMultipleElementary() {
		check(parse("01 MYVAR1 PIC S9(5)V99.\n 01 MYVAR2 PIC X(15)."));
	}

	@Test
	public void parseBlankWhenZero() {
		check(parse("""
				01 MYVAR BLANK WHEN ZERO.
				01 MYVAR BLANK WHEN ZEROS.
				01 MYVAR BLANK WHEN ZEROES.
				01 MYVAR BLANK ZEROES.
								"""));
	}

	@Test
	public void parseExternal() {
		check(parse("01 MYVAR1 EXTERNAL."));
	}

	@Test
	public void parseGlobal() {
		check(parse("01 MYVAR1 GLOBAL."));
	}

	@Test
	public void parseGroupUsage() {
		check(parse("""
				01 MYVAR GROUP-USAGE IS NATIONAL.
				01 MYVAR GROUP-USAGE UTF-8.
								"""));
	}

	@Test
	public void parseJustified() {
		check(parse("""
				01 MYVAR JUSTIFIED.
				01 MYVAR JUST RIGHT.
								"""));
	}

	@Test
	public void parseFixedOccurs() {
		check(parse("""
				01 MYVAR OCCURS 3.
				01 MYVAR OCCURS 3 TIMES.
								"""));
	}

	@Test
	public void parseFixedOccursWithKeys() {
		check(parse("""
				01 MYVAR OCCURS 3 ASCENDING MYKEY.
				01 MYVAR OCCURS 3 ASCENDING KEY MYKEY.
				01 MYVAR OCCURS 3 ASCENDING KEY IS MYKEY.
				01 MYVAR OCCURS 3 DESCENDING MYKEY1 MYKEY2.
								"""));
	}

	@Test
	public void parseFixedOccursWithIndexes() {
		check(parse("""
				01 MYVAR OCCURS 3 INDEXED MYINDEX.
				01 MYVAR OCCURS 3 INDEXED BY MYINDEX.
				01 MYVAR OCCURS 3 INDEXED MYINDEX1 MYINDEX2.
								"""));
	}

	@Test
	public void parseVariableOccurs() {
		check(parse("""
				01 MYVAR OCCURS 3 DEPENDING MYDEP.
				01 MYVAR OCCURS 3 DEPENDING ON MYDEP.
				01 MYVAR OCCURS 3 TIMES DEPENDING ON MYDEP.
				01 MYVAR OCCURS UNBOUNDED DEPENDING ON MYDEP.
								"""));
	}

	@Test
	public void parseVariableOccursWithLowerBound() {
		check(parse("""
				01 MYVAR OCCURS 0 TO 3 DEPENDING MYDEP.
				01 MYVAR OCCURS 1 TO 3 DEPENDING MYDEP.
								"""));
	}

	@Test
	public void parseVariableOccursWithKeys() {
		check(parse("""
				01 MYVAR OCCURS 1 TO 3 DEPENDING MYDEP ASCENDING MYKEY.
				01 MYVAR OCCURS 3 DEPENDING ON MYDEP ASCENDING KEY MYKEY.
				01 MYVAR OCCURS 3 DEPENDING ON MYDEP ASCENDING KEY IS MYKEY.
				01 MYVAR OCCURS 3 DEPENDING MYDEP DESCENDING MYKEY1 MYKEY2.
								"""));
	}

	@Test
	public void parseVariableOccursWithIndexes() {
		check(parse("""
				01 MYVAR OCCURS 1 TO 3 DEPENDING MYDEP INDEXED MYINDEX.
				01 MYVAR OCCURS 3 DEPENDING ON MYDEP INDEXED BY MYINDEX.
				01 MYVAR OCCURS 3 DEPENDING MYDEP INDEXED MYINDEX1 MYINDEX2.
								"""));
	}

	@Test
	public void parseSignClause() {
		check(parse("""
				01 MYVAR PIC 9 SIGN IS LEADING SEPARATE CHARACTER.
				01 MYVAR PIC 9 SIGN IS TRAILING SEPARATE.
				01 MYVAR PIC 9 SIGN IS LEADING.
				01 MYVAR PIC 9 SIGN TRAILING.
				01 MYVAR PIC 9 LEADING.
								"""));
	}

	@Test
	public void parseSynchronizedClause() {
		check(parse("""
				01 MYVAR PIC 9 SYNCHRONIZED.
				01 MYVAR PIC 9 SYNC RIGHT.
				01 MYVAR PIC 9 SYNCHRONIZED LEFT.
								"""));
	}

	@Test
	public void parseUsageClause() {
		check(parse("""
				01 MYVAR USAGE BINARY.
				01 MYVAR COMPUTATIONAL.
				01 MYVAR USAGE COMP.
				01 MYVAR USAGE IS BINARY.
				01 MYVAR USAGE IS BINARY NATIVE.
				01 MYVAR COMPUTATIONAL-1.
				01 MYVAR USAGE IS COMP-1.
				01 MYVAR COMPUTATIONAL-2.
				01 MYVAR COMP-2 NATIVE.
				01 MYVAR USAGE PACKED-DECIMAL.
				01 MYVAR USAGE COMPUTATIONAL-3.
				01 MYVAR COMP-3 NATIVE.
				01 MYVAR USAGE COMPUTATIONAL-5.
				01 MYVAR COMP-5.
				01 MYVAR USAGE IS DISPLAY.
				01 MYVAR USAGE IS NATIONAL.
				01 MYVAR UTF-8 NATIVE.
				01 MYVAR DISPLAY-1.
				01 MYVAR USAGE INDEX.
				01 MYVAR POINTER.
				01 MYVAR USAGE POINTER-32.
				01 MYVAR USAGE PROCEDURE-POINTER.
				01 MYVAR FUNCTION-POINTER.
								"""));

	}

	@Test
	public void parseValueClause() {
		check(parse("""
				01 MYVAR VALUE 'ab'.
				01 MYVAR VALUE 'a''b'.
				01 MYVAR VALUE 'a"b'.
				01 MYVAR VALUE "ab".
				01 MYVAR VALUE "a""b".
				01 MYVAR VALUE "a'b".
				01 MYVAR VALUE 'ab'.
				01 MYVAR VALUE 99.
				01 MYVAR VALUE -99.
				01 MYVAR VALUE 99.9.
				01 MYVAR VALUE -99.9.
				01 MYVAR VALUE -9.9.
				01 MYVAR VALUE 0.
				01 MYVAR VALUE  -0.9.
				01 MYVAR VALUE 0.78E23.
				01 MYVAR VALUE -0.78E+23.
				01 MYVAR VALUE X"8182".
				01 MYVAR VALUE X'8182'.
				01 MYVAR VALUE N"ab".
				01 MYVAR VALUE N'ab'.
				01 MYVAR VALUE NX"8182".
				01 MYVAR VALUE NX'8182'.
				01 MYVAR VALUE U"ab".
				01 MYVAR VALUE U'ab'.
				01 MYVAR VALUE UX"8182".
				01 MYVAR VALUE UX'8182'.
				01 MYVAR VALUE G"ab".
				01 MYVAR VALUE G'ab'.
				01 MYVAR VALUE Z"ab".
				01 MYVAR VALUE Z'ab'.
				01 MYVAR VALUE ZEROS.
				01 MYVAR VALUE SPACE.
				01 MYVAR VALUE HIGH-VALUES.
				01 MYVAR VALUE LOW-VALUE.
				01 MYVAR VALUE QUOTES.
				01 MYVAR VALUE ALL '*'.
				01 MYVAR VALUE NULLS.
				01 MYVAR VALUE 'REDEFINES'.
								"""));
	}

	@Test
	public void parseRename() {
		check(parse("""
				66 NEWNAME RENAMES OLDNAME.
				66 NEWNAME RENAMES OLDSTART THROUGH OLDEND.
				01 RECORD-I.
				   05 DN-1.
				   05 DN-2.
				   05 DN-3.
				66 DN-6 RENAMES DN-1 THRU DN-3.
				01 RECORD-II.
								"""));
	}

	@Test
	public void parseCondition() {
		check(parse("""
				88 CONDITION VALUE 99.
				88 A VALUES ARE "1" "2".
				88 Q VALUE 13 THRU 19.
				88 A VALUE ALL SPACES.
				88 A VALUE ALL 'A' THROUGH ALL 'C'.
				88 A VALUE 1, 2, 3.
				88 A VALUE 1 THRU 5; 7; 8.
				88 A VALUE 1 WHEN SET TO FALSE IS 2.
				88 A VALUE 1 SET TO FALSE IS 2.
				88 A VALUE 1 TO FALSE IS 2.
				88 A VALUE 1 FALSE IS 2.
				88 A VALUE 1 FALSE 2.
								"""));
	}

	@Test
	public void parseException1() {
		try {
			parse("       01 A PIC.");
			fail();
		} catch (ParseException e) {
			assertEquals("Encountered an error at input:1:16\n" + "Found string \".\" of type INVALID\n"
					+ "Was expecting: PICTURE_IS", e.getMessage());
		}
	}

	@Test
	public void parseException2() {
		try {
			parse("       01 A PIC X");
			fail();
		} catch (ParseException e) {
			assertEquals("Encountered an error at input:2:1\n" + "Unexpected end of input.\n"
					+ " Found token of type EOF\n" + "Was expecting: PERIOD", e.getMessage());
		}
	}

	@Test
	public void parseRedefines() {
		check(parse("01 MYVAR REDEFINES OTHERVAR."));
	}

	private String parse(String source) {
		CopybookCCParser parser = new CopybookCCParser(source);
		parser.Copybook();
		Node node = parser.rootNode();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
		node.dump("", ps);
		String res = new String(baos.toByteArray(), StandardCharsets.UTF_8);
		res = res.replace("\r", "");
		return res;
	}

	private void check(String res) {
		try {
			Path refPath = Paths.get("src/test/ref", testName);
			if (refPath.toFile().exists()) {
				String ref = Files.readString(refPath);
				assertEquals(ref, res);
			} else {
				Files.writeString(refPath, res);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
