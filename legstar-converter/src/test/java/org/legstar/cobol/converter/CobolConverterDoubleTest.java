package org.legstar.cobol.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class CobolConverterDoubleTest extends CobolConverterTestBase {

	CobolConverterDouble converter = new CobolConverterDouble();

	@Test
	public void testNoHostData() {
		try {
			converter.toDouble(inputStreamFrom(""));
			fail();
		} catch (Exception e) {
			assertEquals("Not enough bytes for a double. Needed 8, got -1 instead", e.getMessage());
		}
	}

    @Test
    public void testFromHostDouble() {
        assertEquals(0.0d, fromHost("0000000000000000"));
        assertEquals(1.0d, fromHost("4110000000000000"));
        assertEquals(345006.56779999996d, fromHost("45543ae915b573e8"));
        assertEquals(7.982006699999995E-14d, fromHost("361677a4590fab68"));
        assertEquals(3.40282347E38d, fromHost("60ffffff048ff9e8"));
        assertEquals(-5.670078E-14d, fromHost("b5ff5b8c70649ed0"));
        assertEquals(0.5160421914381664d, fromHost("40841b574f9545cd"));
        assertEquals(0.3767408804881589d, fromHost("4060721720c34c90"));
        assertEquals(0.5748594107197613d, fromHost("409329fc80d6d646"));
        assertEquals(0.6001057816958458d, fromHost("4099a0885286303d"));
        assertEquals(0.7850369597199326d, fromHost("40c8f82ea425fc38"));
        assertEquals(0.2998016949252067d, fromHost("404cbfcdcafd37c0"));
        assertEquals(0.7788391667528523d, fromHost("40c76200ee0c21d0"));
        assertEquals(0.8158852356638823d, fromHost("40d0dddad4773358"));
        assertEquals(0.4364272923152456d, fromHost("406fb9b2f3936868"));
        assertEquals(0.6062248297052384d, fromHost("409b318ce99b6f60"));
    }

	private Double fromHost(String payload) {
		return converter.toDouble(inputStreamFrom(payload));
	}
}
