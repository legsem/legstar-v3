package legstar.samples.jaxb.ardo04;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.annotation.*;
import jakarta.xml.bind.annotation.*;

@CobolGroup(cobolName = "ARDO04-RECORD")
@XmlRootElement(name = "ardo04Record")
@XmlType (propOrder={"c_ItemsNumber", "c_Array"})
public class Ardo04Record {

    @CobolBinaryNumber(cobolName = "C-ITEMS-NUMBER", signed = true, totalDigits = 4, odoObject = true)
    private Short c_ItemsNumber;

    @CobolArray(minOccurs=1, maxOccurs=250, dependingOn = "C-ITEMS-NUMBER")
    private C_Array[] c_Array;

    public Short getC_ItemsNumber() {
        return c_ItemsNumber;
    }

    public void setC_ItemsNumber(Short c_ItemsNumber) {
        this.c_ItemsNumber = c_ItemsNumber;
    }

    public C_Array[] getC_Array() {
        return c_Array;
    }

    public void setC_Array(C_Array[] c_Array) {
        this.c_Array = c_Array;
    }

    @CobolGroup(cobolName = "C-ARRAY")
    @XmlType (propOrder={"c_Item1", "c_Item2"})
    public static class C_Array {
    
        @CobolString(cobolName = "C-ITEM-1", charNum = 5)
        private String c_Item1;
    
        @CobolBinaryNumber(cobolName = "C-ITEM-2", signed = true, totalDigits = 4)
        private Short c_Item2;
    
        public String getC_Item1() {
            return c_Item1;
        }
    
        public void setC_Item1(String c_Item1) {
            this.c_Item1 = c_Item1;
        }
    
        public Short getC_Item2() {
            return c_Item2;
        }
    
        public void setC_Item2(Short c_Item2) {
            this.c_Item2 = c_Item2;
        }
    
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{")
                .append("c_Item1=").append(c_Item1)
                .append(", c_Item2=").append(c_Item2)
                .append("}");
            return sb.toString();
        }
    
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("c_ItemsNumber=").append(c_ItemsNumber)
            .append(", c_Array=").append(Arrays.toString(c_Array))
            .append("}");
        return sb.toString();
    }

}
