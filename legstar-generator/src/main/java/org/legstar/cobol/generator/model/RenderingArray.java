package org.legstar.cobol.generator.model;

/**
 * Describes a cobol array.
 * 
 * @param minOccurs   minimum number of occurrences
 * @param maxOccurs   maximum number of occurrences
 * @param dependingOn item giving the actual size of the array for variable size
 *                    arrays (null otherwise).
 */
public record RenderingArray(int minOccurs, int maxOccurs, String dependingOn) {

	/**
	 * Is this a variable size array.
	 * 
	 * @return true if the array size is given by some preceding numeric item
	 */
	public boolean isVariableSize() {
		return dependingOn() != null && !dependingOn().isBlank();
	}

}
