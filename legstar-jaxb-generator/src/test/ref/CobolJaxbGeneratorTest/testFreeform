package legstar.samples.jaxb.freeform;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.annotation.*;
import jakarta.xml.bind.annotation.*;

@CobolGroup(cobolName = "REC-A")
@XmlRootElement(name = "recA")
@XmlType (propOrder={"recAHeader", "recAData"})
public class RecA {

    private RecAHeader recAHeader;

    private RecAData recAData;

    public RecAHeader getRecAHeader() {
        return recAHeader;
    }

    public void setRecAHeader(RecAHeader recAHeader) {
        this.recAHeader = recAHeader;
    }

    public RecAData getRecAData() {
        return recAData;
    }

    public void setRecAData(RecAData recAData) {
        this.recAData = recAData;
    }

    @CobolGroup(cobolName = "REC-A-HEADER")
    @XmlType (propOrder={"fld01", "fld02", "fld03", "fld04", "fld05", "fld06", "fld07", "fld08", "fld09RecType"})
    public static class RecAHeader {
    
        @CobolString(cobolName = "FLD-01", charNum = 4)
        private String fld01;
    
        @CobolString(cobolName = "FLD-02", charNum = 2)
        private String fld02;
    
        @CobolString(cobolName = "FLD-03", charNum = 8)
        private String fld03;
    
        @CobolString(cobolName = "FLD-04", charNum = 3)
        private String fld04;
    
        @CobolZonedDecimal(cobolName = "FLD-05", totalDigits = 6)
        private BigDecimal fld05;
    
        @CobolString(cobolName = "FLD-06", charNum = 1)
        private String fld06;
    
        @CobolZonedDecimal(cobolName = "FLD-07", totalDigits = 5)
        private BigDecimal fld07;
    
        @CobolString(cobolName = "FLD-08", charNum = 2)
        private String fld08;
    
        @CobolString(cobolName = "FLD-09-REC-TYPE", charNum = 1)
        private String fld09RecType;
    
        public String getFld01() {
            return fld01;
        }
    
        public void setFld01(String fld01) {
            this.fld01 = fld01;
        }
    
        public String getFld02() {
            return fld02;
        }
    
        public void setFld02(String fld02) {
            this.fld02 = fld02;
        }
    
        public String getFld03() {
            return fld03;
        }
    
        public void setFld03(String fld03) {
            this.fld03 = fld03;
        }
    
        public String getFld04() {
            return fld04;
        }
    
        public void setFld04(String fld04) {
            this.fld04 = fld04;
        }
    
        public BigDecimal getFld05() {
            return fld05;
        }
    
        public void setFld05(BigDecimal fld05) {
            this.fld05 = fld05;
        }
    
        public String getFld06() {
            return fld06;
        }
    
        public void setFld06(String fld06) {
            this.fld06 = fld06;
        }
    
        public BigDecimal getFld07() {
            return fld07;
        }
    
        public void setFld07(BigDecimal fld07) {
            this.fld07 = fld07;
        }
    
        public String getFld08() {
            return fld08;
        }
    
        public void setFld08(String fld08) {
            this.fld08 = fld08;
        }
    
        public String getFld09RecType() {
            return fld09RecType;
        }
    
        public void setFld09RecType(String fld09RecType) {
            this.fld09RecType = fld09RecType;
        }
    
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{")
                .append("fld01=").append(fld01)
                .append(", fld02=").append(fld02)
                .append(", fld03=").append(fld03)
                .append(", fld04=").append(fld04)
                .append(", fld05=").append(fld05)
                .append(", fld06=").append(fld06)
                .append(", fld07=").append(fld07)
                .append(", fld08=").append(fld08)
                .append(", fld09RecType=").append(fld09RecType)
                .append("}");
            return sb.toString();
        }
    
    }

    @CobolGroup(cobolName = "REC-A-DATA")
    @XmlType (propOrder={"fld10", "fld11"})
    public static class RecAData {
    
        @CobolString(cobolName = "FLD-10", charNum = 35)
        private String fld10;
    
        @CobolString(cobolName = "FLD-11", charNum = 1)
        private String fld11;
    
        public String getFld10() {
            return fld10;
        }
    
        public void setFld10(String fld10) {
            this.fld10 = fld10;
        }
    
        public String getFld11() {
            return fld11;
        }
    
        public void setFld11(String fld11) {
            this.fld11 = fld11;
        }
    
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{")
                .append("fld10=").append(fld10)
                .append(", fld11=").append(fld11)
                .append("}");
            return sb.toString();
        }
    
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("recAHeader=").append(recAHeader)
            .append(", recAData=").append(recAData)
            .append("}");
        return sb.toString();
    }

}
