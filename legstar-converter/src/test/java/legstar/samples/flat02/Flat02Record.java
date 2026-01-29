package legstar.samples.flat02;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.type.annotations.*;

@CobolGroup(cobolName = "FLAT02-RECORD")
public class Flat02Record {

    @CobolZonedDecimal(cobolName = "COM-NUMBER", totalDigits = 6)
    private BigDecimal comNumber;

    @CobolString(cobolName = "COM-NAME", charNum = 20)
    private String comName;

    @CobolPackedDecimal(cobolName = "COM-AMOUNT", totalDigits = 7, fractionDigits = 2)
    private BigDecimal comAmount;

    @CobolArray(minOccurs=5, maxOccurs=5)
    @CobolBinaryNumber(cobolName = "COM-ARRAY", signed = true, totalDigits = 4)
    private Short[] comArray;

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

    public Short[] getComArray() {
        return comArray;
    }

    public void setComArray(Short[] comArray) {
        this.comArray = comArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("comNumber=").append(comNumber)
            .append(", comName=").append(comName)
            .append(", comAmount=").append(comAmount)
            .append(", comArray=").append(Arrays.toString(comArray))
            .append("}");
        return sb.toString();
    }

}
