package legstar.samples.jaxb.ardo01;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.annotation.*;
import jakarta.xml.bind.annotation.*;

@CobolGroup(cobolName = "ARDO01-RECORD")
@XmlRootElement(name = "ardo01Record")
@XmlType (propOrder={"comNumber", "comName", "comNbr", "comArray"})
public class Ardo01Record {

    @CobolZonedDecimal(cobolName = "COM-NUMBER", totalDigits = 6)
    private BigDecimal comNumber;

    @CobolString(cobolName = "COM-NAME", charNum = 20)
    private String comName;

    @CobolBinaryNumber(cobolName = "COM-NBR", totalDigits = 4, odoObject = true)
    private Short comNbr;

    @CobolArray(minOccurs=0, maxOccurs=5, dependingOn = "COM-NBR")
    @CobolPackedDecimal(cobolName = "COM-ARRAY", signed = true, totalDigits = 15, fractionDigits = 2)
    private BigDecimal[] comArray;

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

    public Short getComNbr() {
        return comNbr;
    }

    public void setComNbr(Short comNbr) {
        this.comNbr = comNbr;
    }

    public BigDecimal[] getComArray() {
        return comArray;
    }

    public void setComArray(BigDecimal[] comArray) {
        this.comArray = comArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("comNumber=").append(comNumber)
            .append(", comName=").append(comName)
            .append(", comNbr=").append(comNbr)
            .append(", comArray=").append(Arrays.toString(comArray))
            .append("}");
        return sb.toString();
    }

}
