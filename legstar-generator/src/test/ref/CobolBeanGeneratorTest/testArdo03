package legstar.samples.ardo03;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.type.annotations.*;

@CobolGroup(cobolName = "ARDO03-RECORD")
public class Ardo03Record {

    @CobolZonedDecimal(cobolName = "ODO-COUNTER", totalDigits = 5, odoObject = true)
    private BigDecimal odoCounter;

    @CobolArray(minOccurs=0, maxOccurs=5, dependingOn = "ODO-COUNTER")
    private OdoArray[] odoArray;

    public BigDecimal getOdoCounter() {
        return odoCounter;
    }

    public void setOdoCounter(BigDecimal odoCounter) {
        this.odoCounter = odoCounter;
    }

    public OdoArray[] getOdoArray() {
        return odoArray;
    }

    public void setOdoArray(OdoArray[] odoArray) {
        this.odoArray = odoArray;
    }

    @CobolGroup(cobolName = "ODO-ARRAY")
    public static class OdoArray {
    
        @CobolZonedDecimal(cobolName = "ODO-SUB-COUNTER", totalDigits = 3, odoObject = true)
        private BigDecimal odoSubCounter;
    
        @CobolArray(minOccurs=0, maxOccurs=5, dependingOn = "ODO-SUB-COUNTER")
        private OdoSubArray[] odoSubArray;
    
        public BigDecimal getOdoSubCounter() {
            return odoSubCounter;
        }
    
        public void setOdoSubCounter(BigDecimal odoSubCounter) {
            this.odoSubCounter = odoSubCounter;
        }
    
        public OdoSubArray[] getOdoSubArray() {
            return odoSubArray;
        }
    
        public void setOdoSubArray(OdoSubArray[] odoSubArray) {
            this.odoSubArray = odoSubArray;
        }
    
        @CobolGroup(cobolName = "ODO-SUB-ARRAY")
        public static class OdoSubArray {
        
            @CobolString(cobolName = "FILLER", charNum = 4)
            private String filler;
        
            public String getFiller() {
                return filler;
            }
        
            public void setFiller(String filler) {
                this.filler = filler;
            }
        
            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("{")
                    .append("filler=").append(filler)
                    .append("}");
                return sb.toString();
            }
        }
    
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{")
                .append("odoSubCounter=").append(odoSubCounter)
                .append(", odoSubArray=").append(Arrays.toString(odoSubArray))
                .append("}");
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("odoCounter=").append(odoCounter)
            .append(", odoArray=").append(Arrays.toString(odoArray))
            .append("}");
        return sb.toString();
    }
}
