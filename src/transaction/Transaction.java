package transaction;

import account.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.ArrayList;

public class Transaction {
    private TransactionType type;
    private String transactionID;
    private Account account;
    private Account targetAccount;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private LocalDateTime dateTime;

    public Transaction(TransactionType type, Account acc, BigDecimal amount,
            BigDecimal balanceAfter, ArrayList<Transaction> allTransaction) {
        this.transactionID = generateUniqueTransactionID(allTransaction);
        this.type = type;
        this.account = acc;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.dateTime = LocalDateTime.now();

    }

    public String getTransactionID() {
        return this.transactionID;
    }

    public TransactionType getTransactionType() {
        return this.type;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public BigDecimal getBalanceAfter() {
        return this.balanceAfter;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public void setTargetAccount(Account acc) {
        this.targetAccount = acc;
    }

    public Account getTargetAccount() {
        return this.targetAccount;
    }

    public String generateTransactionID() {
        Random rand = new Random();
        long number = 100000000L + (long) (rand.nextDouble() * 9000000000L);
        return "TXN" + number;
    }

    public String generateUniqueTransactionID(ArrayList<Transaction> trans) {
        String transID;
        Boolean exist;
        do {
            transID = generateTransactionID();
            exist = false;
            for (Transaction t : trans) {
                if (t.getTransactionID().equals(transID)) {
                    exist = true;
                    break;
                }
            }
        } while (exist);
        return transID;
    }

    public String generateBreifReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("--------RECEIPT--------");
        receipt.append("VaultEdge");
        receipt.append("Transaction ID: ").append(this.transactionID).append("\n");
        receipt.append("Type: ").append(this.type).append("\n");
        receipt.append("Amount: ").append(this.amount).append("\n");
        receipt.append(this.dateTime.toLocalDate()).append(" ");
        receipt.append(this.dateTime.toLocalTime());
        return receipt.toString();
    }

    public String generateDetailedReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("--------RECEIPT--------");
        receipt.append("VaultEdge");
        receipt.append("Transaction ID: ").append(this.transactionID).append("\n");
        receipt.append("Type: ").append(this.type).append("\n");
        receipt.append("Amount: ").append(this.amount).append("\n");
        if (this.type == TransactionType.TRANSFER) {
            receipt.append("Receiver Name: ").append(this.targetAccount.getCustomer().getFullName()).append("\n");
            receipt.append("Receiver Account: ").append(this.targetAccount.getAccountNumber()).append("\n");
        } else {
            // DEPOSIT or WITHDRAW
            receipt.append("Account Holder: ").append(this.account.getCustomer().getFullName()).append("\n");
            receipt.append("Account Number: ").append(this.account.getAccountNumber()).append("\n");
            receipt.append("Balance After: ").append(this.balanceAfter).append("\n");
        }
        receipt.append("Date: ").append(this.dateTime.toLocalDate()).append("\n");
        receipt.append("Time: ").append(this.dateTime.toLocalTime());
        return receipt.toString();
    }

}
