package legstar.samples.custdat;

import java.math.BigDecimal;

import org.legstar.cobol.type.annotations.CobolArray;
import org.legstar.cobol.type.annotations.CobolBinaryNumber;
import org.legstar.cobol.type.annotations.CobolChoice;
import org.legstar.cobol.type.annotations.CobolGroup;
import org.legstar.cobol.type.annotations.CobolPackedDecimal;
import org.legstar.cobol.type.annotations.CobolString;
import org.legstar.cobol.type.annotations.CobolZonedDecimal;

@CobolGroup(cobolName = "CUSTOMER-DATA")
public class CustomerData {

	@CobolZonedDecimal(cobolName = "CUSTOMER-ID", totalDigits = 6)
	private BigDecimal customerId;

	private PersonalData personalData;

	private Transactions transactions;

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public PersonalData getPersonalData() {
		return personalData;
	}

	public void setPersonalData(PersonalData personalData) {
		this.personalData = personalData;
	}

	public Transactions getTransactions() {
		return transactions;
	}

	public void setTransactions(Transactions transactions) {
		this.transactions = transactions;
	}

	@CobolGroup(cobolName = "PERSONAL-DATA")
	public static class PersonalData {

		@CobolString(cobolName = "CUSTOMER-NAME", charNum = 20)
		private String customerName;

		@CobolString(cobolName = "CUSTOMER-ADDRESS", charNum = 20)
		private String customerAddress;

		@CobolString(cobolName = "CUSTOMER-PHONE", charNum = 8)
		private String customerPhone;

		public String getCustomerName() {
			return customerName;
		}

		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}

		public String getCustomerAddress() {
			return customerAddress;
		}

		public void setCustomerAddress(String customerAddress) {
			this.customerAddress = customerAddress;
		}

		public String getCustomerPhone() {
			return customerPhone;
		}

		public void setCustomerPhone(String customerPhone) {
			this.customerPhone = customerPhone;
		}
	}

	@CobolGroup(cobolName = "TRANSACTIONS")
	public static class Transactions {
		
		@CobolBinaryNumber(cobolName = "TRANSACTION-NBR", totalDigits=9, odoObject=true)
		private long transactionNbr;
		
		@CobolArray(minOccurs=0, maxOccurs=5, dependingOn="TRANSACTION-NBR")
		private Transaction[] transactionArray;

		public long getTransactionNbr() {
			return transactionNbr;
		}

		public void setTransactionNbr(long transactionNbr) {
			this.transactionNbr = transactionNbr;
		}

		public Transaction[] getTransactionArray() {
			return transactionArray;
		}

		public void setTransactionArray(Transaction[] transactionArray) {
			this.transactionArray = transactionArray;
		}
		
	}

	@CobolGroup(cobolName = "TRANSACTION")
	public static class Transaction {
		
		private TransactionDateChoice transactionDateChoice;

		@CobolPackedDecimal(cobolName = "TRANSACTION-AMOUNT", signed = true, totalDigits = 15, fractionDigits = 2)
		private java.math.BigDecimal transactionAmount;

		@CobolString(cobolName = "TRANSACTION-COMMENT", charNum = 9)
		private String transactionComment;

		public TransactionDateChoice getTransactionDateChoice() {
			return transactionDateChoice;
		}

		public void setTransactionDateChoice(TransactionDateChoice transactionDateChoice) {
			this.transactionDateChoice = transactionDateChoice;
		}

		public java.math.BigDecimal getTransactionAmount() {
			return transactionAmount;
		}

		public void setTransactionAmount(java.math.BigDecimal transactionAmount) {
			this.transactionAmount = transactionAmount;
		}

		public String getTransactionComment() {
			return transactionComment;
		}

		public void setTransactionComment(String transactionComment) {
			this.transactionComment = transactionComment;
		}
	}

	@CobolChoice(maxBytesLen = 8)
	public static class TransactionDateChoice {
		
		@CobolString(cobolName = "TRANSACTION-DATE", charNum = 8)
		private String transactionDate;
		
		private Filler_12 filler_12;

		public String getTransactionDate() {
			return transactionDate;
		}

		public void setTransactionDate(String transactionDate) {
			this.transactionDate = transactionDate;
			this.filler_12 = null;
		}

		public Filler_12 getFiller_12() {
			return filler_12;
		}

		public void setFiller_12(Filler_12 filler_12) {
			this.transactionDate = null;
			this.filler_12 = filler_12;
		}

	}

	@CobolGroup(cobolName = "FILLER")
	public static class Filler_12 {

		@CobolString(cobolName = "TRANSACTION-DAY", charNum = 2)
		private String transactionDay;

		@CobolString(cobolName = "FILLER", charNum = 1)
		private String filler_14;

		@CobolString(cobolName = "TRANSACTION-MONTH", charNum = 2)
		private String transactionMonth;

		@CobolString(cobolName = "FILLER", charNum = 1)
		private String filler_16;

		@CobolString(cobolName = "TRANSACTION-YEAR", charNum = 2)
		private String transactionYear;

		public String getTransactionDay() {
			return transactionDay;
		}

		public void setTransactionDay(String transactionDay) {
			this.transactionDay = transactionDay;
		}

		public String getFiller_14() {
			return filler_14;
		}

		public void setFiller_14(String filler_14) {
			this.filler_14 = filler_14;
		}

		public String getTransactionMonth() {
			return transactionMonth;
		}

		public void setTransactionMonth(String transactionMonth) {
			this.transactionMonth = transactionMonth;
		}

		public String getFiller_16() {
			return filler_16;
		}

		public void setFiller_16(String filler_16) {
			this.filler_16 = filler_16;
		}

		public String getTransactionYear() {
			return transactionYear;
		}

		public void setTransactionYear(String transactionYear) {
			this.transactionYear = transactionYear;
		}

	}

}
