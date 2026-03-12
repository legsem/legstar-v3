package org.legstar.cobol.bean.generator;

import java.io.Writer;
import java.util.Stack;

import gg.jte.output.WriterOutput;

/**
 * Getting proper indentation using jte templates only is hard (see
 * https://github.com/casid/jte/issues/270).
 * <p>
 * This output writer indents Groups and Choices after the templating engine did
 * its part.
 */
public class BeanIndentWriterOutput extends WriterOutput {

	private static final String NL = System.getProperty("line.separator");

	private StringBuilder builder = new StringBuilder();

	/**
	 * Group and Choices have their depth pushed on this stack.
	 */
	private Stack<Integer> depth = new Stack<>();

	/**
	 * Wrap a regular writer to provide indent capabilities.
	 * 
	 * @param writer a writer to augment
	 */
	public BeanIndentWriterOutput(Writer writer) {
		super(writer);
	}

	@Override
	public void writeContent(String value) {
		int pos, prev = 0;
		while ((pos = value.indexOf(NL, prev)) > -1) {
			builder.append(value.substring(prev, pos + NL.length()));
			writeline();
			prev = pos + NL.length();
		}
		if (prev < value.length()) {
			builder.append(value.substring(prev));
		}
	}

	/**
	 * CobolGroup and CobolChoice occurrences increase the depth.
	 * <p>
	 * Closing curly braces without preceding spaces are assumed to close a
	 * CobolGroup or CobolChoice so they decrease the depth.
	 */
	private void writeline() {

		String line = builder.toString();
		builder = new StringBuilder();

		if (line.startsWith("@CobolGroup") || line.startsWith("@CobolChoice")) {
			depth.push(depth.size());
		}
		if (!depth.isEmpty()) {
			super.writeContent("    ".repeat(depth.peek()));
		}
		super.writeContent(line);

		if (line.startsWith("}")) {
			depth.pop();
		}
	}

}
