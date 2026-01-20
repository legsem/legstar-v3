package legstar.samples.rdef03;

import java.math.BigDecimal;

import org.legstar.cobol.type.annotations.CobolBinaryNumber;
import org.legstar.cobol.type.annotations.CobolChoice;
import org.legstar.cobol.type.annotations.CobolGroup;
import org.legstar.cobol.type.annotations.CobolPackedDecimal;
import org.legstar.cobol.type.annotations.CobolString;
import org.legstar.cobol.type.annotations.CobolZonedDecimal;

@CobolGroup(cobolName = "RDEF03-RECORD")
public class Rdef03Record {
	
	@CobolBinaryNumber(cobolName = "COM-SELECT", totalDigits = 4)
	private int comSelect;
	
	private ComDetail1Choice comDetail1Choice;
	
	public int getComSelect() {
		return comSelect;
	}

	public void setComSelect(int comSelect) {
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

		private ComDetail3 comDetail3;

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

		public ComDetail3 getComDetail3() {
			return comDetail3;
		}

		public void setComDetail3(ComDetail3 comDetail3) {
			this.comDetail3 = comDetail3;
		}
		
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

		@CobolPackedDecimal(cobolName = "COM-AMOUNT", signed = true, totalDigits=7, fractionDigits=2)
		private BigDecimal comAmount;

		public BigDecimal getComAmount() {
			return comAmount;
		}

		public void setComAmount(BigDecimal comAmount) {
			this.comAmount = comAmount;
		}

	}

	@CobolGroup(cobolName = "COM-DETAIL3")
	public static class ComDetail3 {

		@CobolZonedDecimal(cobolName = "COM-NUMBER", totalDigits=5)
		private BigDecimal comNumber;

		public BigDecimal getComNumber() {
			return comNumber;
		}

		public void setComNumber(BigDecimal comNumber) {
			this.comNumber = comNumber;
		}

	}


}
