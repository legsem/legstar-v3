package org.legstar.cobol.converter;

import java.io.ByteArrayInputStream;

import com.legsem.legstar.base.test.CobolTestBase;

public abstract class CobolConverterTestBase extends CobolTestBase {

	public CobolInputStream inputStreamFrom(String hex) {
		return new CobolInputStream(new ByteArrayInputStream(hexToBytes(hex)));
	}
 
}
