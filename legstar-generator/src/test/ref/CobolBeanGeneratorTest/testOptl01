package legstar.samples.optl01;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.type.annotations.*;

@CobolGroup(cobolName = "OPTL01-RECORD")
public class Optl01Record {

    @CobolZonedDecimal(cobolName = "OPTL-STRUCT-IND", totalDigits = 3, odoObject = true)
    private BigDecimal optlStructInd;

    @CobolZonedDecimal(cobolName = "OPTL-ITEM-IND", totalDigits = 3, odoObject = true)
    private BigDecimal optlItemInd;

    @CobolArray(minOccurs=0, maxOccurs=1, dependingOn = "OPTL-STRUCT-IND")
    private OptlStruct[] optlStruct;

    @CobolArray(minOccurs=0, maxOccurs=1, dependingOn = "OPTL-ITEM-IND")
    @CobolString(cobolName = "OPTL-ITEM", charNum = 32)
    private String[] optlItem;

    public BigDecimal getOptlStructInd() {
        return optlStructInd;
    }

    public void setOptlStructInd(BigDecimal optlStructInd) {
        this.optlStructInd = optlStructInd;
    }

    public BigDecimal getOptlItemInd() {
        return optlItemInd;
    }

    public void setOptlItemInd(BigDecimal optlItemInd) {
        this.optlItemInd = optlItemInd;
    }

    public OptlStruct[] getOptlStruct() {
        return optlStruct;
    }

    public void setOptlStruct(OptlStruct[] optlStruct) {
        this.optlStruct = optlStruct;
    }

    public String[] getOptlItem() {
        return optlItem;
    }

    public void setOptlItem(String[] optlItem) {
        this.optlItem = optlItem;
    }

    @CobolGroup(cobolName = "OPTL-STRUCT")
    public static class OptlStruct {
    
        @CobolString(cobolName = "OPTL-STRUCT-FIELD1", charNum = 18)
        private String optlStructField1;
    
        @CobolString(cobolName = "OPTL-STRUCT-FIELD2", charNum = 5)
        private String optlStructField2;
    
        public String getOptlStructField1() {
            return optlStructField1;
        }
    
        public void setOptlStructField1(String optlStructField1) {
            this.optlStructField1 = optlStructField1;
        }
    
        public String getOptlStructField2() {
            return optlStructField2;
        }
    
        public void setOptlStructField2(String optlStructField2) {
            this.optlStructField2 = optlStructField2;
        }
    
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{")
                .append("optlStructField1=").append(optlStructField1)
                .append(", optlStructField2=").append(optlStructField2)
                .append("}");
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("optlStructInd=").append(optlStructInd)
            .append(", optlItemInd=").append(optlItemInd)
            .append(", optlStruct=").append(Arrays.toString(optlStruct))
            .append(", optlItem=").append(Arrays.toString(optlItem))
            .append("}");
        return sb.toString();
    }
}
