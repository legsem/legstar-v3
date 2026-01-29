package legstar.samples.alltypes;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.type.annotations.*;

@CobolGroup(cobolName = "ALLTYPES")
public class Alltypes {

    @CobolString(cobolName = "S-STRING", charNum = 4)
    private String sString;

    @CobolString(cobolName = "S-BINARY", charNum = 4)
    private String sBinary;

    @CobolBinaryNumber(cobolName = "S-SHORT", signed = true, totalDigits = 4)
    private Short sShort;

    @CobolBinaryNumber(cobolName = "S-USHORT", totalDigits = 4)
    private Short sUshort;

    @CobolBinaryNumber(cobolName = "S-INT", signed = true, totalDigits = 9)
    private Integer sInt;

    @CobolBinaryNumber(cobolName = "S-UINT", totalDigits = 9)
    private Integer sUint;

    @CobolBinaryNumber(cobolName = "S-LONG", signed = true, totalDigits = 18)
    private Long sLong;

    @CobolBinaryNumber(cobolName = "S-ULONG", totalDigits = 18)
    private Long sUlong;

    @CobolPackedDecimal(cobolName = "S-PDECIMAL", totalDigits = 9, fractionDigits = 2)
    private BigDecimal sPdecimal;

    @CobolFloat(cobolName = "S-FLOAT")
    private Float sFloat;

    @CobolDouble(cobolName = "S-DOUBLE")
    private Double sDouble;

    @CobolZonedDecimal(cobolName = "S-ZONED", totalDigits = 5, fractionDigits = 2)
    private BigDecimal sZoned;

    @CobolZonedDecimal(cobolName = "S-UZONED", totalDigits = 5, fractionDigits = 2)
    private BigDecimal sUzoned;

    @CobolZonedDecimal(cobolName = "S-ZONED-SL", totalDigits = 3, signLeading = true)
    private BigDecimal sZonedSl;

    @CobolZonedDecimal(cobolName = "S-ZONED-ST", totalDigits = 2)
    private BigDecimal sZonedSt;

    @CobolZonedDecimal(cobolName = "S-ZONED-SLS", totalDigits = 1, signLeading = true, signSeparate = true)
    private BigDecimal sZonedSls;

    @CobolZonedDecimal(cobolName = "S-ZONED-STS", totalDigits = 2, signSeparate = true)
    private BigDecimal sZonedSts;

    @CobolZonedDecimal(cobolName = "S-UZONED-BWZ", totalDigits = 5, fractionDigits = 2, blankWhenZero = true)
    private BigDecimal sUzonedBwz;

    public String getSString() {
        return sString;
    }

    public void setSString(String sString) {
        this.sString = sString;
    }

    public String getSBinary() {
        return sBinary;
    }

    public void setSBinary(String sBinary) {
        this.sBinary = sBinary;
    }

    public Short getSShort() {
        return sShort;
    }

    public void setSShort(Short sShort) {
        this.sShort = sShort;
    }

    public Short getSUshort() {
        return sUshort;
    }

    public void setSUshort(Short sUshort) {
        this.sUshort = sUshort;
    }

    public Integer getSInt() {
        return sInt;
    }

    public void setSInt(Integer sInt) {
        this.sInt = sInt;
    }

    public Integer getSUint() {
        return sUint;
    }

    public void setSUint(Integer sUint) {
        this.sUint = sUint;
    }

    public Long getSLong() {
        return sLong;
    }

    public void setSLong(Long sLong) {
        this.sLong = sLong;
    }

    public Long getSUlong() {
        return sUlong;
    }

    public void setSUlong(Long sUlong) {
        this.sUlong = sUlong;
    }

    public BigDecimal getSPdecimal() {
        return sPdecimal;
    }

    public void setSPdecimal(BigDecimal sPdecimal) {
        this.sPdecimal = sPdecimal;
    }

    public Float getSFloat() {
        return sFloat;
    }

    public void setSFloat(Float sFloat) {
        this.sFloat = sFloat;
    }

    public Double getSDouble() {
        return sDouble;
    }

    public void setSDouble(Double sDouble) {
        this.sDouble = sDouble;
    }

    public BigDecimal getSZoned() {
        return sZoned;
    }

    public void setSZoned(BigDecimal sZoned) {
        this.sZoned = sZoned;
    }

    public BigDecimal getSUzoned() {
        return sUzoned;
    }

    public void setSUzoned(BigDecimal sUzoned) {
        this.sUzoned = sUzoned;
    }

    public BigDecimal getSZonedSl() {
        return sZonedSl;
    }

    public void setSZonedSl(BigDecimal sZonedSl) {
        this.sZonedSl = sZonedSl;
    }

    public BigDecimal getSZonedSt() {
        return sZonedSt;
    }

    public void setSZonedSt(BigDecimal sZonedSt) {
        this.sZonedSt = sZonedSt;
    }

    public BigDecimal getSZonedSls() {
        return sZonedSls;
    }

    public void setSZonedSls(BigDecimal sZonedSls) {
        this.sZonedSls = sZonedSls;
    }

    public BigDecimal getSZonedSts() {
        return sZonedSts;
    }

    public void setSZonedSts(BigDecimal sZonedSts) {
        this.sZonedSts = sZonedSts;
    }

    public BigDecimal getSUzonedBwz() {
        return sUzonedBwz;
    }

    public void setSUzonedBwz(BigDecimal sUzonedBwz) {
        this.sUzonedBwz = sUzonedBwz;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("sString=").append(sString)
            .append(", sBinary=").append(sBinary)
            .append(", sShort=").append(sShort)
            .append(", sUshort=").append(sUshort)
            .append(", sInt=").append(sInt)
            .append(", sUint=").append(sUint)
            .append(", sLong=").append(sLong)
            .append(", sUlong=").append(sUlong)
            .append(", sPdecimal=").append(sPdecimal)
            .append(", sFloat=").append(sFloat)
            .append(", sDouble=").append(sDouble)
            .append(", sZoned=").append(sZoned)
            .append(", sUzoned=").append(sUzoned)
            .append(", sZonedSl=").append(sZonedSl)
            .append(", sZonedSt=").append(sZonedSt)
            .append(", sZonedSls=").append(sZonedSls)
            .append(", sZonedSts=").append(sZonedSts)
            .append(", sUzonedBwz=").append(sUzonedBwz)
            .append("}");
        return sb.toString();
    }

}
