package org.legstar.cobol.type.converter;

public interface CobolConverterConfig {

    /**
     * @return code for unspecified sign nibble value
     */
    public int unspecifiedSignNibbleValue();

    /**
     * @return code for positive sign nibble value
     */
    public int positiveSignNibbleValue();

    /**
     * @return code for négative sign nibble value
     */
    public int negativeSignNibbleValue();

    /**
     * @return code for a separate plus sign
     */
    public int hostPlusSign();

    /**
     * @return code for a separate minus sign
     */
    public int hostMinusSign();

    /**
     * @return the host space character code point
     */
    public int hostSpaceCharCode();

    /**
     * @return the character set used by the host to encode strings (Must be a
     *         valid java charset)
     */
    public String hostCharsetName();

    /**
     * @return should trailing spaces be trimmed from host strings when
     *         converted to java
     */
    public boolean truncateHostStringsTrailingSpaces();
    
    /**
     * @return the maximum number of bytes a level 01 item can span
     */
    public int maxCobolTypeBytesLen();

}
