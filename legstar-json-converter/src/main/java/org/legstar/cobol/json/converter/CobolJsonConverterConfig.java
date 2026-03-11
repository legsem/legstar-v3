package org.legstar.cobol.json.converter;

import org.legstar.cobol.converter.CobolConverterConfigBase;

/**
 * Set of parameters for the cobol JSON converter.
 */
public class CobolJsonConverterConfig extends CobolConverterConfigBase<CobolJsonConverterConfig> {

	/**
	 * Whether to produce an indented formatted JSON.
	 */
	private boolean formattedOutput;

	/**
	 * Whether elements with null values should be suppressed from the output.
	 */
	private boolean omitNullValues;

	/**
	 * Whether empty arrays should be suppressed from the output.
	 */
	private boolean omitEmptyArrays;

	/**
	 * A set of parameters for the cobol JSON converter.
	 * 
	 * @param hostCharsetName the cobol character set
	 */
	public CobolJsonConverterConfig(String hostCharsetName) {
		super(hostCharsetName);
	}

	/**
	 * Whether to produce an indented formatted JSON
	 * 
	 * @return true to produce an indented formatted JSON
	 */
	public boolean isFormattedOutput() {
		return formattedOutput;
	}

	/**
	 * Set whether to produce an indented formatted JSON
	 * 
	 * @param formattedOutput true to produce an indented formatted JSON
	 * @return this
	 */
	public CobolJsonConverterConfig setFormattedOutput(boolean formattedOutput) {
		this.formattedOutput = formattedOutput;
		return self();
	}

	/**
	 * Whether elements with null values should be suppressed from the output
	 * 
	 * @return true if elements with null values should be suppressed from the
	 *         output
	 */
	public boolean isOmitNullValues() {
		return omitNullValues;
	}

	/**
	 * Set whether elements with null values should be suppressed from the output
	 * 
	 * @param omitNullValues true if elements with null values should be suppressed
	 *                       from the output
	 * @return this
	 */
	public CobolJsonConverterConfig setOmitNullValues(boolean omitNullValues) {
		this.omitNullValues = omitNullValues;
		return self();
	}

	/**
	 * Whether empty arrays should be suppressed from the output
	 * 
	 * @return true if empty arrays should be suppressed from the output
	 */
	public boolean isOmitEmptyArrays() {
		return omitEmptyArrays;
	}

	/**
	 * Set whether empty arrays should be suppressed from the output
	 * 
	 * @param omitEmptyArrays true if empty arrays should be suppressed from the
	 *                        output
	 * @return this
	 */
	public CobolJsonConverterConfig setOmitEmptyArrays(boolean omitEmptyArrays) {
		this.omitEmptyArrays = omitEmptyArrays;
		return self();
	}

	/**
	 * A default configuration assuming cobol is using ebcdic (usual)
	 * 
	 * @return an ebcdic configuration
	 */
	public static CobolJsonConverterConfig ebcdic() {
		return new CobolJsonConverterConfig(DEFAULT_EBCDIC_CHARSET);
	}

	/**
	 * A default configuration assuming cobol is using ascii (unusual)
	 * 
	 * @return an ascii configuration
	 */
	public static CobolJsonConverterConfig ascii() {
		return new CobolJsonConverterConfig(DEFAULT_ASCII_CHARSET);
	}

	@Override
	public CobolJsonConverterConfig self() {
		return this;
	}

}
