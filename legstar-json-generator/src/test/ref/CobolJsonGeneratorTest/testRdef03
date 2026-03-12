package legstar.samples.json.rdef03;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.annotation.*;
import com.fasterxml.jackson.annotation.*;

@CobolGroup(cobolName = "RDEF03-RECORD")
@JsonPropertyOrder ({"comSelect", "comDetail1Choice"})
public class Rdef03Record {

    @CobolBinaryNumber(cobolName = "COM-SELECT", totalDigits = 4)
    private Short comSelect;

    private ComDetail1Choice comDetail1Choice;

    public Short getComSelect() {
        return comSelect;
    }

    public void setComSelect(Short comSelect) {
        this.comSelect = comSelect;
    }

    public ComDetail1Choice getComDetail1Choice() {
        return comDetail1Choice;
    }

    public void setComDetail1Choice(ComDetail1Choice comDetail1Choice) {
        this.comDetail1Choice = comDetail1Choice;
    }

    @CobolChoice(cobolName = "COM-DETAIL1", maxBytesLen = 10)
    public static class ComDetail1Choice {
    
        private ComDetail1 comDetail1;
    
        private ComDetail2 comDetail2;
    
        private ComDetail3 comDetail3;
    
        public ComDetail1 getComDetail1() {
            return comDetail1;
        }
    
        public void setComDetail1(ComDetail1 comDetail1) {
            this.comDetail1 = comDetail1;
        }
    
        public ComDetail2 getComDetail2() {
            return comDetail2;
        }
    
        public void setComDetail2(ComDetail2 comDetail2) {
            this.comDetail2 = comDetail2;
        }
    
        public ComDetail3 getComDetail3() {
            return comDetail3;
        }
    
        public void setComDetail3(ComDetail3 comDetail3) {
            this.comDetail3 = comDetail3;
        }
    
        @CobolGroup(cobolName = "COM-DETAIL1")
        @JsonPropertyOrder ({"comName"})
        public static class ComDetail1 {
        
            @CobolString(cobolName = "COM-NAME", charNum = 10)
            private String comName;
        
            public String getComName() {
                return comName;
            }
        
            public void setComName(String comName) {
                this.comName = comName;
            }
        
            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("{")
                    .append("comName=").append(comName)
                    .append("}");
                return sb.toString();
            }
        
        }
    
        @CobolGroup(cobolName = "COM-DETAIL2")
        @JsonPropertyOrder ({"comAmount"})
        public static class ComDetail2 {
        
            @CobolPackedDecimal(cobolName = "COM-AMOUNT", totalDigits = 7, fractionDigits = 2)
            private BigDecimal comAmount;
        
            public BigDecimal getComAmount() {
                return comAmount;
            }
        
            public void setComAmount(BigDecimal comAmount) {
                this.comAmount = comAmount;
            }
        
            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("{")
                    .append("comAmount=").append(comAmount)
                    .append("}");
                return sb.toString();
            }
        
        }
    
        @CobolGroup(cobolName = "COM-DETAIL3")
        @JsonPropertyOrder ({"comNumber"})
        public static class ComDetail3 {
        
            @CobolZonedDecimal(cobolName = "COM-NUMBER", totalDigits = 5)
            private BigDecimal comNumber;
        
            public BigDecimal getComNumber() {
                return comNumber;
            }
        
            public void setComNumber(BigDecimal comNumber) {
                this.comNumber = comNumber;
            }
        
            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("{")
                    .append("comNumber=").append(comNumber)
                    .append("}");
                return sb.toString();
            }
        
        }
    
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{")
                .append("comDetail1=").append(comDetail1)
                .append(", comDetail2=").append(comDetail2)
                .append(", comDetail3=").append(comDetail3)
                .append("}");
            return sb.toString();
        }
    
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("comSelect=").append(comSelect)
            .append(", comDetail1Choice=").append(comDetail1Choice)
            .append("}");
        return sb.toString();
    }

}
