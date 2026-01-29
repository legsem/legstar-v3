package legstar.samples.rdef04;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.type.annotations.*;

@CobolGroup(cobolName = "RDEF04-RECORD")
public class Rdef04Record {

    private OuterRedefinesLongChoice outerRedefinesLongChoice;

    @CobolString(cobolName = "FOOTER", charNum = 1)
    private String footer;

    public OuterRedefinesLongChoice getOuterRedefinesLongChoice() {
        return outerRedefinesLongChoice;
    }

    public void setOuterRedefinesLongChoice(OuterRedefinesLongChoice outerRedefinesLongChoice) {
        this.outerRedefinesLongChoice = outerRedefinesLongChoice;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    @CobolChoice(cobolName = "OUTER-REDEFINES-LONG", maxBytesLen = 10)
    public static class OuterRedefinesLongChoice {
    
        @CobolString(cobolName = "OUTER-REDEFINES-LONG", charNum = 10)
        private String outerRedefinesLong;
    
        private OuterRedefinesShort outerRedefinesShort;
    
        public String getOuterRedefinesLong() {
            return outerRedefinesLong;
        }
    
        public void setOuterRedefinesLong(String outerRedefinesLong) {
            this.outerRedefinesLong = outerRedefinesLong;
        }
    
        public OuterRedefinesShort getOuterRedefinesShort() {
            return outerRedefinesShort;
        }
    
        public void setOuterRedefinesShort(OuterRedefinesShort outerRedefinesShort) {
            this.outerRedefinesShort = outerRedefinesShort;
        }
    
        @CobolGroup(cobolName = "OUTER-REDEFINES-SHORT")
        public static class OuterRedefinesShort {
        
            private InnerRedefinesLongChoice innerRedefinesLongChoice;
        
            public InnerRedefinesLongChoice getInnerRedefinesLongChoice() {
                return innerRedefinesLongChoice;
            }
        
            public void setInnerRedefinesLongChoice(InnerRedefinesLongChoice innerRedefinesLongChoice) {
                this.innerRedefinesLongChoice = innerRedefinesLongChoice;
            }
        
            @CobolChoice(cobolName = "INNER-REDEFINES-LONG", maxBytesLen = 5)
            public static class InnerRedefinesLongChoice {
            
                @CobolString(cobolName = "INNER-REDEFINES-LONG", charNum = 5)
                private String innerRedefinesLong;
            
                @CobolString(cobolName = "INNER-REDEFINES-SHORT", charNum = 3)
                private String innerRedefinesShort;
            
                public String getInnerRedefinesLong() {
                    return innerRedefinesLong;
                }
            
                public void setInnerRedefinesLong(String innerRedefinesLong) {
                    this.innerRedefinesLong = innerRedefinesLong;
                }
            
                public String getInnerRedefinesShort() {
                    return innerRedefinesShort;
                }
            
                public void setInnerRedefinesShort(String innerRedefinesShort) {
                    this.innerRedefinesShort = innerRedefinesShort;
                }
            
                @Override
                public String toString() {
                    StringBuilder sb = new StringBuilder();
                    sb.append("{")
                        .append("innerRedefinesLong=").append(innerRedefinesLong)
                        .append(", innerRedefinesShort=").append(innerRedefinesShort)
                        .append("}");
                    return sb.toString();
                }
            
            }
        
            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("{")
                    .append("innerRedefinesLongChoice=").append(innerRedefinesLongChoice)
                    .append("}");
                return sb.toString();
            }
        
        }
    
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{")
                .append("outerRedefinesLong=").append(outerRedefinesLong)
                .append(", outerRedefinesShort=").append(outerRedefinesShort)
                .append("}");
            return sb.toString();
        }
    
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("outerRedefinesLongChoice=").append(outerRedefinesLongChoice)
            .append(", footer=").append(footer)
            .append("}");
        return sb.toString();
    }

}
