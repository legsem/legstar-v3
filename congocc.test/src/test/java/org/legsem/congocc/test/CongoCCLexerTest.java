package org.legsem.congocc.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.legstar.copybook.parser.CopybookLexer;
import org.legstar.copybook.parser.CopybookLexer.LexicalState;
import org.legstar.copybook.parser.Token;

public class CongoCCLexerTest {

	@Test
	public void t0() {
		assertEquals("[LEVEL_NUMBER:01]", //
				lex("01."));
	}

	@Test
	public void t1() {
		assertEquals("[LEVEL_NUMBER:01, DATA_NAME:MYVAR]", //
				lex("01 MYVAR."));
	}

	@Test
	public void t2() {
		assertEquals("[LEVEL_NUMBER:01, DATA_NAME:MYVAR, PICTURE_IS:****.**]", // 
				lex("01 MYVAR PIC ****.**."));
	}

	@Test
	public void t3() {
		assertEquals("[LEVEL_NUMBER:01, DATA_NAME:MYVAR, PICTURE_IS:Z,ZZZ.ZZ+]", // 
				lex("01 MYVAR PIC Z,ZZZ.ZZ+"));
	}

	@Test
	public void t4() {
		assertEquals("[LEVEL_NUMBER:01, DATA_NAME:MYVAR, DATA_NAME:OTHERVAR]", // 
				lex("01 MYVAR REDEFINES OTHERVAR"));
	}

	@Test
	public void t5() {
		assertEquals("[LEVEL_NUMBER:01, DATA_NAME:MYVAR, USAGE:USAGE, BINARY:BINARY]", // 
				lex("01 MYVAR USAGE BINARY"));
	}

	@Test
	public void t6() {
		assertEquals("[NUMERIC_LITERAL:0]", // 
				lex("0", LexicalState.VALUE_STATE));
	}

	@Test
	public void t7() {
		assertEquals("[ZERO_CONSTANT:ZEROS]", // 
				lex("ZEROS", LexicalState.VALUE_STATE));
	}

	@Test
	public void t8() {
		assertEquals("[LEVEL_66:66, DATA_NAME:NEWNAME, RENAMES:RENAMES, DATA_NAME:OLDNAME]", // 
				lex("66 NEWNAME RENAMES OLDNAME"));
	}

	@Test
	public void t9() {
		assertEquals("[LEVEL_88:88, DATA_NAME:CONDITION, VALUE:VALUE, INTEGER:99]", // 
				lex(" 88 CONDITION VALUE 99"));
	}

	private String lex(String s) {
		return lex(s, LexicalState.LEVEL_STATE);
	}

	private String lex(String s, LexicalState state) {
		List<String> res = new ArrayList<>();
		CopybookLexer lexer = new CopybookLexer(s);
		lexer.switchTo(state);
		Token t = null;
		while ((t = lexer.getNextToken(t)) != null) {
			if (t.getType().isEOF()) {
				break;
			}
			switch (t.getType()) {
			case WHITESPACE:
			case PERIOD:
			case PICTURE:
			case REDEFINES:
				break;
			default:
				res.add(t.getType() + ":" + t.toString());
			}
		}
		return res.toString();
	}

}
