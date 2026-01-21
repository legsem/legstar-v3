package legstar.samples.flat01;

import java.math.BigDecimal;

import org.legstar.cobol.type.annotations.*;

@CobolGroup(cobolName = "FLAT01-RECORD")
public class Flat01Record {

    @CobolZonedDecimal(cobolName = "COM-NUMBER", totalDigits = 6)
    private BigDecimal comNumber;

    @CobolString(cobolName = "COM-NAME", charNum = 20)
    private String comName;

    @CobolPackedDecimal(cobolName = "COM-AMOUNT", totalDigits = 7, fractionDigits = 2)
    private BigDecimal comAmount;

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
}
