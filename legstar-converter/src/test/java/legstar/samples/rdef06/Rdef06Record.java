package legstar.samples.rdef06;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.annotation.*;

@CobolGroup(cobolName = "RDEF06-RECORD")
public class Rdef06Record {

    @CobolZonedDecimal(cobolName = "OPTL-STRUCT-IND", totalDigits = 3, odoObject = true)
    private BigDecimal optlStructInd;

    private OptlItemChoice optlItemChoice;

    public BigDecimal getOptlStructInd() {
        return optlStructInd;
    }

    public void setOptlStructInd(BigDecimal optlStructInd) {
        this.optlStructInd = optlStructInd;
    }

    public OptlItemChoice getOptlItemChoice() {
        return optlItemChoice;
    }

    public void setOptlItemChoice(OptlItemChoice optlItemChoice) {
        this.optlItemChoice = optlItemChoice;
    }

    @CobolChoice(cobolName = "OPTL-ITEM", maxBytesLen = 23)
    public static class OptlItemChoice {
    
        @CobolString(cobolName = "OPTL-ITEM", charNum = 23)
        private String optlItem;
    
        @CobolArray(minOccurs=0, maxOccurs=1, dependingOn = "OPTL-STRUCT-IND")
        private OptlStruct[] optlStruct;
    
        public String getOptlItem() {
            return optlItem;
        }
    
        public void setOptlItem(String optlItem) {
            this.optlItem = optlItem;
        }
    
        public OptlStruct[] getOptlStruct() {
            return optlStruct;
        }
    
        public void setOptlStruct(OptlStruct[] optlStruct) {
            this.optlStruct = optlStruct;
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
                .append("optlItem=").append(optlItem)
                .append(", optlStruct=").append(Arrays.toString(optlStruct))
                .append("}");
            return sb.toString();
        }
    
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("optlStructInd=").append(optlStructInd)
            .append(", optlItemChoice=").append(optlItemChoice)
            .append("}");
        return sb.toString();
    }

}
