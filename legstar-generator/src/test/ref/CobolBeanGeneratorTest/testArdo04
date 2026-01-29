package legstar.samples.ardo04;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.type.annotations.*;

@CobolGroup(cobolName = "ARDO04-RECORD")
public class Ardo04Record {

    @CobolBinaryNumber(cobolName = "C-ITEMS-NUMBER", signed = true, totalDigits = 4, odoObject = true)
    private Short cItemsNumber;

    @CobolArray(minOccurs=1, maxOccurs=250, dependingOn = "C-ITEMS-NUMBER")
    private CArray[] cArray;

    public Short getCItemsNumber() {
        return cItemsNumber;
    }

    public void setCItemsNumber(Short cItemsNumber) {
        this.cItemsNumber = cItemsNumber;
    }

    public CArray[] getCArray() {
        return cArray;
    }

    public void setCArray(CArray[] cArray) {
        this.cArray = cArray;
    }

    @CobolGroup(cobolName = "C-ARRAY")
    public static class CArray {
    
        @CobolString(cobolName = "C-ITEM-1", charNum = 5)
        private String cItem1;
    
        @CobolBinaryNumber(cobolName = "C-ITEM-2", signed = true, totalDigits = 4)
        private Short cItem2;
    
        public String getCItem1() {
            return cItem1;
        }
    
        public void setCItem1(String cItem1) {
            this.cItem1 = cItem1;
        }
    
        public Short getCItem2() {
            return cItem2;
        }
    
        public void setCItem2(Short cItem2) {
            this.cItem2 = cItem2;
        }
    
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{")
                .append("cItem1=").append(cItem1)
                .append(", cItem2=").append(cItem2)
                .append("}");
            return sb.toString();
        }
    
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("cItemsNumber=").append(cItemsNumber)
            .append(", cArray=").append(Arrays.toString(cArray))
            .append("}");
        return sb.toString();
    }

}
