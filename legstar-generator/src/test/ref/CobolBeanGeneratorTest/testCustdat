package legstar.samples.custdat;

import java.math.BigDecimal;
import java.util.Arrays;

import org.legstar.cobol.type.annotations.*;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("customerName=").append(customerName)
            .append(", customerAddress=").append(customerAddress)
            .append(", customerPhone=").append(customerPhone)
            .append("}");
        return sb.toString();
    }
}

@CobolGroup(cobolName = "TRANSACTIONS")
public static class Transactions {

    @CobolBinaryNumber(cobolName = "TRANSACTION-NBR", totalDigits = 9, odoObject = true)
    private Integer transactionNbr;

    @CobolArray(minOccurs=0, maxOccurs=5, dependingOn = "TRANSACTION-NBR")
    private Transaction[] transaction;

    public Integer getTransactionNbr() {
        return transactionNbr;
    }

    public void setTransactionNbr(Integer transactionNbr) {
        this.transactionNbr = transactionNbr;
    }

    public Transaction[] getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction[] transaction) {
        this.transaction = transaction;
    }

@CobolGroup(cobolName = "TRANSACTION")
public static class Transaction {

    private TransactionDateChoice transactionDateChoice;

    @CobolPackedDecimal(cobolName = "TRANSACTION-AMOUNT", signed = true, totalDigits = 15, fractionDigits = 2)
    private BigDecimal transactionAmount;

    @CobolString(cobolName = "TRANSACTION-COMMENT", charNum = 9)
    private String transactionComment;

    public TransactionDateChoice getTransactionDateChoice() {
        return transactionDateChoice;
    }

    public void setTransactionDateChoice(TransactionDateChoice transactionDateChoice) {
        this.transactionDateChoice = transactionDateChoice;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionComment() {
        return transactionComment;
    }

    public void setTransactionComment(String transactionComment) {
        this.transactionComment = transactionComment;
    }

@CobolChoice(cobolName = "TRANSACTION-DATE", maxBytesLen = 8)
public static class TransactionDateChoice {

    @CobolString(cobolName = "TRANSACTION-DATE", charNum = 8)
    private String transactionDate;

    private Filler filler;

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Filler getFiller() {
        return filler;
    }

    public void setFiller(Filler filler) {
        this.filler = filler;
    }

@CobolGroup(cobolName = "FILLER")
public static class Filler {

    @CobolString(cobolName = "TRANSACTION-DAY", charNum = 2)
    private String transactionDay;

    @CobolString(cobolName = "FILLER", charNum = 1)
    private String filler;

    @CobolString(cobolName = "TRANSACTION-MONTH", charNum = 2)
    private String transactionMonth;

    @CobolString(cobolName = "FILLER", charNum = 1)
    private String filler_3;

    @CobolString(cobolName = "TRANSACTION-YEAR", charNum = 2)
    private String transactionYear;

    public String getTransactionDay() {
        return transactionDay;
    }

    public void setTransactionDay(String transactionDay) {
        this.transactionDay = transactionDay;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    public String getTransactionMonth() {
        return transactionMonth;
    }

    public void setTransactionMonth(String transactionMonth) {
        this.transactionMonth = transactionMonth;
    }

    public String getFiller_3() {
        return filler_3;
    }

    public void setFiller_3(String filler_3) {
        this.filler_3 = filler_3;
    }

    public String getTransactionYear() {
        return transactionYear;
    }

    public void setTransactionYear(String transactionYear) {
        this.transactionYear = transactionYear;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("transactionDay=").append(transactionDay)
            .append(", filler=").append(filler)
            .append(", transactionMonth=").append(transactionMonth)
            .append(", filler_3=").append(filler_3)
            .append(", transactionYear=").append(transactionYear)
            .append("}");
        return sb.toString();
    }
}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("transactionDate=").append(transactionDate)
            .append(", filler=").append(filler)
            .append("}");
        return sb.toString();
    }
}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("transactionDateChoice=").append(transactionDateChoice)
            .append(", transactionAmount=").append(transactionAmount)
            .append(", transactionComment=").append(transactionComment)
            .append("}");
        return sb.toString();
    }
}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("transactionNbr=").append(transactionNbr)
            .append(", transaction=").append(Arrays.toString(transaction))
            .append("}");
        return sb.toString();
    }
}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
            .append("customerId=").append(customerId)
            .append(", personalData=").append(personalData)
            .append(", transactions=").append(transactions)
            .append("}");
        return sb.toString();
    }
}
