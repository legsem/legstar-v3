package legstar.samples.rdef01;

import java.math.BigDecimal;

import org.legstar.cobol.type.annotations.*;

@CobolGroup(cobolName = "RDEF01-RECORD")
public class Rdef01Record {

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

@CobolChoice(maxBytesLen = 10)
public static class ComDetail1Choice {

    private ComDetail1 comDetail1;

    private ComDetail2 comDetail2;

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

@CobolGroup(cobolName = "COM-DETAIL1")
public static class ComDetail1 {

    @CobolString(cobolName = "COM-NAME", charNum = 10)
    private String comName;

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

}

@CobolGroup(cobolName = "COM-DETAIL2")
public static class ComDetail2 {

    @CobolPackedDecimal(cobolName = "COM-AMOUNT", totalDigits = 7, fractionDigits = 2)
    private BigDecimal comAmount;

    public BigDecimal getComAmount() {
        return comAmount;
    }

    public void setComAmount(BigDecimal comAmount) {
        this.comAmount = comAmount;
    }

}

}

}
