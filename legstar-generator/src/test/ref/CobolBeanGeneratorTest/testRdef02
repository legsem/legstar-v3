package legstar.samples.rdef02;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.type.annotations.*;

@CobolGroup(cobolName = "RDEF02-RECORD")
public class Rdef02Record {

    private Rdef02Key rdef02Key;

    private ComDetail1Choice comDetail1Choice;

    @CobolPackedDecimal(cobolName = "COM-ITEM3", totalDigits = 7, fractionDigits = 2)
    private BigDecimal comItem3;

    public Rdef02Key getRdef02Key() {
        return rdef02Key;
    }

    public void setRdef02Key(Rdef02Key rdef02Key) {
        this.rdef02Key = rdef02Key;
    }

    public ComDetail1Choice getComDetail1Choice() {
        return comDetail1Choice;
    }

    public void setComDetail1Choice(ComDetail1Choice comDetail1Choice) {
        this.comDetail1Choice = comDetail1Choice;
    }

    public BigDecimal getComItem3() {
        return comItem3;
    }

    public void setComItem3(BigDecimal comItem3) {
        this.comItem3 = comItem3;
    }

@CobolGroup(cobolName = "RDEF02-KEY")
public static class Rdef02Key {

    private Rdef02Item1Choice rdef02Item1Choice;

    @CobolBinaryNumber(cobolName = "COM-SELECT", totalDigits = 4)
    private Short comSelect;

    public Rdef02Item1Choice getRdef02Item1Choice() {
        return rdef02Item1Choice;
    }

    public void setRdef02Item1Choice(Rdef02Item1Choice rdef02Item1Choice) {
        this.rdef02Item1Choice = rdef02Item1Choice;
    }

    public Short getComSelect() {
        return comSelect;
    }

    public void setComSelect(Short comSelect) {
        this.comSelect = comSelect;
    }

@CobolChoice(cobolName = "RDEF02-ITEM1", maxBytesLen = 6)
public static class Rdef02Item1Choice {

    @CobolPackedDecimal(cobolName = "RDEF02-ITEM1", signed = true, totalDigits = 10)
    private BigDecimal rdef02Item1;

    @CobolString(cobolName = "RDEF02-ITEM2", charNum = 6)
    private String rdef02Item2;

    public BigDecimal getRdef02Item1() {
        return rdef02Item1;
    }

    public void setRdef02Item1(BigDecimal rdef02Item1) {
        this.rdef02Item1 = rdef02Item1;
    }

    public String getRdef02Item2() {
        return rdef02Item2;
    }

    public void setRdef02Item2(String rdef02Item2) {
        this.rdef02Item2 = rdef02Item2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("rdef02Item1=").append(rdef02Item1)
            .append(", rdef02Item2=").append(rdef02Item2)
            .append("}");
        return sb.toString();
    }
}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("rdef02Item1Choice=").append(rdef02Item1Choice)
            .append(", comSelect=").append(comSelect)
            .append("}");
        return sb.toString();
    }
}

@CobolChoice(cobolName = "COM-DETAIL1", maxBytesLen = 10)
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
public static class ComDetail2 {

    @CobolPackedDecimal(cobolName = "COM-AMOUNT", totalDigits = 7, fractionDigits = 2)
    private BigDecimal comAmount;

    @CobolString(cobolName = "FILLER", charNum = 6)
    private String filler;

    public BigDecimal getComAmount() {
        return comAmount;
    }

    public void setComAmount(BigDecimal comAmount) {
        this.comAmount = comAmount;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("comAmount=").append(comAmount)
            .append(", filler=").append(filler)
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
            .append("}");
        return sb.toString();
    }
}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("rdef02Key=").append(rdef02Key)
            .append(", comDetail1Choice=").append(comDetail1Choice)
            .append(", comItem3=").append(comItem3)
            .append("}");
        return sb.toString();
    }
}
