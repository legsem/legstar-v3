package org.legsem.congocc.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.legstar.copybook.parser.CopybookParser;
import org.legstar.copybook.parser.Node;
import org.legstar.copybook.parser.ParseException;
import org.legstar.copybook.parser.Token;
import org.legstar.copybook.parser.ast.Copybook;
import org.legstar.copybook.parser.ast.DataEntry;
import org.legstar.copybook.parser.ast.DataLevel;

public class CongoCCParseTest {

	@Test
	public void oneEmpty() {
		assertEquals(null, parse(""));
	}

	@Test
	public void noDataName() {
		Copybook copybook = parse("01.");
		DataEntry dataEntry = (DataEntry) copybook.get(0);
		Node levelNumber = (Node) dataEntry.getFirstChild();
		assertEquals("01", levelNumber.toString());
	}

	@Test
	public void oneRecord() {
		parse("01 MYVAR.");
	}

	@Test
	public void twoRecords() {
		parse("01 RECORD1.\n01 RECORD2.");
	}

	@Test
	public void oneElementary() {
		parse("01 MYVAR PIC X.");
	}

	@Test
	public void oneExternalFloatingPoint() {
		parse("01 MYVAR PIC -9v9(9)E-99.");
	}

	@Test
	public void numericTtems() {
		parse("01 MYVAR PIC 9999.");
		parse("01 MYVAR PIC S99.");
		parse("01 MYVAR PIC S999V9.");
		parse("01 MYVAR PIC PPP999.");
		parse("01 MYVAR PIC S999PPP.");
	}

	@Test
	public void numericEdited() {
		parse("01 MYVAR PIC ****.**.");
		parse("01 MYVAR PIC *.*.");
		parse("01 MYVAR PIC ZZZZ.99.");
		parse("01 MYVAR PIC Z,ZZZ.ZZ+.");
		parse("01 MYVAR PIC $Z,ZZZ,ZZZ.ZZCR.");
		parse("01 MYVAR PIC $B*,***,***.**BBDB.");
	}

	@Test
	public void multipleElementary() {
		parse("01 MYVAR1 PIC S9(5)V99.\n 01 MYVAR2 PIC X(15).");
	}

	@Test
	public void testParserException1() {
		try {
			parse("       01 A PIC.");
			fail();
		} catch (ParseException e) {
			assertEquals("Encountered an error at input:1:16\n"
					+ "Found string \".\" of type INVALID\n"
					+ "Was expecting: PICTURE_IS", e.getMessage());
		}
	}

	@Test
	public void testParserException2() {
		try {
			parse("       01 A PIC X");
			fail();
		} catch (ParseException e) {
			assertEquals("Encountered an error at input:2:1\n"
					+ "Unexpected end of input.\n"
					+ " Found token of type EOF\n"
					+ "Was expecting: PERIOD", e.getMessage());
		}
	}

	@Test
	public void testRedefines() {
		parse("01 MYVAR REDEFINES OTHERVAR.");
	}
	
	private Copybook parse(String source) {
		CopybookParser parser = new CopybookParser(source);
		parser.Copybook();
		Node node = parser.rootNode();
		return node instanceof Copybook ? (Copybook) node : null; 
	}

}
