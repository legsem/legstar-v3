package legstar.samples.jaxb.stru01;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.annotation.*;
import jakarta.xml.bind.annotation.*;

@CobolGroup(cobolName = "STRU01-RECORD")
@XmlRootElement(name = "stru01Record", namespace = "")
@XmlType (propOrder={"comNumber", "comName", "comAmount", "comSubRecord"})
public class Stru01Record {

    @CobolZonedDecimal(cobolName = "COM-NUMBER", totalDigits = 6)
    private BigDecimal comNumber;

    @CobolString(cobolName = "COM-NAME", charNum = 20)
    private String comName;

    @CobolPackedDecimal(cobolName = "COM-AMOUNT", totalDigits = 7, fractionDigits = 2)
    private BigDecimal comAmount;

    private ComSubRecord comSubRecord;

    public BigDecimal getComNumber() {
        return comNumber;
    }

    public void setComNumber(BigDecimal comNumber) {
        this.comNumber = comNumber;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public BigDecimal getComAmount() {
        return comAmount;
    }

    public void setComAmount(BigDecimal comAmount) {
        this.comAmount = comAmount;
    }

    public ComSubRecord getComSubRecord() {
        return comSubRecord;
    }

    public void setComSubRecord(ComSubRecord comSubRecord) {
        this.comSubRecord = comSubRecord;
    }

    @CobolGroup(cobolName = "COM-SUB-RECORD")
    @XmlType (propOrder={"comItem1", "comItem2"})
    public static class ComSubRecord {
    
        @CobolBinaryNumber(cobolName = "COM-ITEM1", signed = true, totalDigits = 4)
        private Short comItem1;
    
        @CobolString(cobolName = "COM-ITEM2", charNum = 2)
        private String comItem2;
    
        public Short getComItem1() {
            return comItem1;
        }
    
        public void setComItem1(Short comItem1) {
            this.comItem1 = comItem1;
        }
    
        public String getComItem2() {
            return comItem2;
        }
    
        public void setComItem2(String comItem2) {
            this.comItem2 = comItem2;
        }
    
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{")
                .append("comItem1=").append(comItem1)
                .append(", comItem2=").append(comItem2)
                .append("}");
            return sb.toString();
        }
    
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("comNumber=").append(comNumber)
            .append(", comName=").append(comName)
            .append(", comAmount=").append(comAmount)
            .append(", comSubRecord=").append(comSubRecord)
            .append("}");
        return sb.toString();
    }

}
