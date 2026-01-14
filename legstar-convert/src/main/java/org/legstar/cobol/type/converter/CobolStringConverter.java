package org.legstar.cobol.type.converter;

import java.io.UnsupportedEncodingException;

public class CobolStringConverter {
	
	private final CobolConverterConfig config;
	
	public CobolStringConverter(CobolConverterConfig config) {
		this.config = config;
	}
	
	public String fromHost(byte[] hostData, int start, int bytesLen) {
        // Trim trailing low-values and, optionally, trailing spaces
        int spaceCode = config.hostSpaceCharCode();
        boolean checkSpace = config.truncateHostStringsTrailingSpaces();

        int end = start + bytesLen;
        while (end > start) {
            int code = hostData[end - 1] & 0xFF;
            if (code != 0 && ((checkSpace && code != spaceCode) || !checkSpace)) {
                break;
            }
            end--;
        }

        // Return early if this is an empty string
        if (start == end) {
            return "";
        }

        // Host strings are sometimes dirty
        boolean isDirty = false;
        for (int i = start; i < end; i++) {
            if (hostData[i] == 0) {
                isDirty = true;
                break;
            }
        }

        // Cleanup if needed then ask java to convert using the proper host
        // character set
        String result = null;
        try {
            if (isDirty) {
                byte[] work = new byte[end - start];
                for (int i = start; i < end; i++) {
                    work[i - start] = hostData[i] == 0 ? (byte) config
                            .hostSpaceCharCode() : hostData[i];
                }
                result = new String(work, config.hostCharsetName());
            } else {
                result = new String(hostData, start, end - start,
                		config.hostCharsetName());
            }
        } catch (UnsupportedEncodingException e) {
            throw new FromHostException(
                    "Failed to use host character set "
                            + config.hostCharsetName(), e);
        }

        return result;
	}

}
