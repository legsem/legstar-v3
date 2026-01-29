package legstar.samples.rdef05;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.type.annotations.*;

@CobolGroup(cobolName = "RDEF05-RECORD")
public class Rdef05Record {

    private Choice1Alt1Choice choice1Alt1Choice;

    private Choice2Alt1Choice choice2Alt1Choice;

    public Choice1Alt1Choice getChoice1Alt1Choice() {
        return choice1Alt1Choice;
    }

    public void setChoice1Alt1Choice(Choice1Alt1Choice choice1Alt1Choice) {
        this.choice1Alt1Choice = choice1Alt1Choice;
    }

    public Choice2Alt1Choice getChoice2Alt1Choice() {
        return choice2Alt1Choice;
    }

    public void setChoice2Alt1Choice(Choice2Alt1Choice choice2Alt1Choice) {
        this.choice2Alt1Choice = choice2Alt1Choice;
    }

    @CobolChoice(cobolName = "CHOICE1-ALT1", maxBytesLen = 4)
    public static class Choice1Alt1Choice {
    
        @CobolString(cobolName = "CHOICE1-ALT1", charNum = 4)
        private String choice1Alt1;
    
        @CobolZonedDecimal(cobolName = "CHOICE1-ALT2", totalDigits = 4, fractionDigits = 3)
        private BigDecimal choice1Alt2;
    
        @CobolZonedDecimal(cobolName = "CHOICE1-ALT3", totalDigits = 4, fractionDigits = 2)
        private BigDecimal choice1Alt3;
    
        public String getChoice1Alt1() {
            return choice1Alt1;
        }
    
        public void setChoice1Alt1(String choice1Alt1) {
            this.choice1Alt1 = choice1Alt1;
        }
    
        public BigDecimal getChoice1Alt2() {
            return choice1Alt2;
        }
    
        public void setChoice1Alt2(BigDecimal choice1Alt2) {
            this.choice1Alt2 = choice1Alt2;
        }
    
        public BigDecimal getChoice1Alt3() {
            return choice1Alt3;
        }
    
        public void setChoice1Alt3(BigDecimal choice1Alt3) {
            this.choice1Alt3 = choice1Alt3;
        }
    
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{")
                .append("choice1Alt1=").append(choice1Alt1)
                .append(", choice1Alt2=").append(choice1Alt2)
                .append(", choice1Alt3=").append(choice1Alt3)
                .append("}");
            return sb.toString();
        }
    
    }

    @CobolChoice(cobolName = "CHOICE2-ALT1", maxBytesLen = 4)
    public static class Choice2Alt1Choice {
    
        @CobolString(cobolName = "CHOICE2-ALT1", charNum = 4)
        private String choice2Alt1;
    
        @CobolZonedDecimal(cobolName = "CHOICE2-ALT2", totalDigits = 4, fractionDigits = 3)
        private BigDecimal choice2Alt2;
    
        @CobolZonedDecimal(cobolName = "CHOICE2-ALT3", totalDigits = 4, fractionDigits = 2)
        private BigDecimal choice2Alt3;
    
        @CobolZonedDecimal(cobolName = "CHOICE2-ALT4", totalDigits = 4, fractionDigits = 1)
        private BigDecimal choice2Alt4;
    
        public String getChoice2Alt1() {
            return choice2Alt1;
        }
    
        public void setChoice2Alt1(String choice2Alt1) {
            this.choice2Alt1 = choice2Alt1;
        }
    
        public BigDecimal getChoice2Alt2() {
            return choice2Alt2;
        }
    
        public void setChoice2Alt2(BigDecimal choice2Alt2) {
            this.choice2Alt2 = choice2Alt2;
        }
    
        public BigDecimal getChoice2Alt3() {
            return choice2Alt3;
        }
    
        public void setChoice2Alt3(BigDecimal choice2Alt3) {
            this.choice2Alt3 = choice2Alt3;
        }
    
        public BigDecimal getChoice2Alt4() {
            return choice2Alt4;
        }
    
        public void setChoice2Alt4(BigDecimal choice2Alt4) {
            this.choice2Alt4 = choice2Alt4;
        }
    
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{")
                .append("choice2Alt1=").append(choice2Alt1)
                .append(", choice2Alt2=").append(choice2Alt2)
                .append(", choice2Alt3=").append(choice2Alt3)
                .append(", choice2Alt4=").append(choice2Alt4)
                .append("}");
            return sb.toString();
        }
    
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("choice1Alt1Choice=").append(choice1Alt1Choice)
            .append(", choice2Alt1Choice=").append(choice2Alt1Choice)
            .append("}");
        return sb.toString();
    }

}
