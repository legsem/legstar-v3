package org.legstar.cobol.converter;

import java.io.ByteArrayInputStream;

import org.legstar.cobol.base.test.CobolTestBase;
import org.legstar.cobol.io.CobolInputStream;

public abstract class CobolConverterTestBase extends CobolTestBase {

	public CobolInputStream inputStreamFrom(String hex) {
		return new CobolInputStream(new ByteArrayInputStream(hexToBytes(hex)));
	}
 
}
