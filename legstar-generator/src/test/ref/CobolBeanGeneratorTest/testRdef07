package legstar.samples.rdef07;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.type.annotations.*;

@CobolGroup(cobolName = "RDEF07-RECORD")
public class Rdef07Record {

    @CobolZonedDecimal(cobolName = "COM-COUNTER", totalDigits = 3, odoObject = true)
    private BigDecimal comCounter;

    @CobolArray(minOccurs=0, maxOccurs=5, dependingOn = "COM-COUNTER")
    private ComStruct[] comStruct;

    public BigDecimal getComCounter() {
        return comCounter;
    }

    public void setComCounter(BigDecimal comCounter) {
        this.comCounter = comCounter;
    }

    public ComStruct[] getComStruct() {
        return comStruct;
    }

    public void setComStruct(ComStruct[] comStruct) {
        this.comStruct = comStruct;
    }

    @CobolGroup(cobolName = "COM-STRUCT")
    public static class ComStruct {
    
        private ComAlt1Choice comAlt1Choice;
    
        public ComAlt1Choice getComAlt1Choice() {
            return comAlt1Choice;
        }
    
        public void setComAlt1Choice(ComAlt1Choice comAlt1Choice) {
            this.comAlt1Choice = comAlt1Choice;
        }
    
        @CobolChoice(cobolName = "COM-ALT1", maxBytesLen = 2)
        public static class ComAlt1Choice {
        
            @CobolPackedDecimal(cobolName = "COM-ALT1", totalDigits = 3)
            private BigDecimal comAlt1;
        
            @CobolString(cobolName = "COM-ALT2", charNum = 2)
            private String comAlt2;
        
            public BigDecimal getComAlt1() {
                return comAlt1;
            }
        
            public void setComAlt1(BigDecimal comAlt1) {
                this.comAlt1 = comAlt1;
            }
        
            public String getComAlt2() {
                return comAlt2;
            }
        
            public void setComAlt2(String comAlt2) {
                this.comAlt2 = comAlt2;
            }
        
            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("{")
                    .append("comAlt1=").append(comAlt1)
                    .append(", comAlt2=").append(comAlt2)
                    .append("}");
                return sb.toString();
            }
        
        }
    
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{")
                .append("comAlt1Choice=").append(comAlt1Choice)
                .append("}");
            return sb.toString();
        }
    
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("comCounter=").append(comCounter)
            .append(", comStruct=").append(Arrays.toString(comStruct))
            .append("}");
        return sb.toString();
    }

}
