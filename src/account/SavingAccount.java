package account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import exceptions.*;
import security.TransactionPIN;
import transaction.*;
import customer.*;
import java.time.LocalDate;
import java.math.RoundingMode;

import customer.Customer;

public class SavingAccount extends Account {

    private final BigDecimal yearlyInterestRate = BigDecimal.valueOf(0.05);

    private final BigDecimal monthlyInterestRate = yearlyInterestRate.divide(BigDecimal.valueOf(12), 6,
            RoundingMode.HALF_UP);
    private int transactionLimitCounter = 0;
    private int currentMonth = LocalDate.now().getMonthValue();

    public SavingAccount(String accountNumber, AccountStatus status, BigDecimal balance,
            String hashedPin, Customer customer, ArrayList<Customer> customers, int withdrawalCount)
            throws InvalidPinException {
        super(accountNumber, status, balance, hashedPin, customer, customers);
        this.transactionLimitCounter = withdrawalCount;
    }

    public SavingAccount(Customer custom, ArrayList<Customer> customers, String PIN, BigDecimal balance)
            throws InvalidPinException {
        super(custom, customers, PIN, balance);
    }

    public int getTransactionLimitCounter() {
        return this.transactionLimitCounter;
    }

    public BigDecimal getYearlyInterestRate() {
        return this.yearlyInterestRate;
    }

    public void calculateYearlyInterest() {
        BigDecimal currentBalance = super.getCurrentBalance();
        BigDecimal interestAmount = currentBalance.multiply((this.yearlyInterestRate));
        super.setCurrentBalance(currentBalance.add(interestAmount));
        Transaction t = new Transaction(TransactionType.INTEREST, this,
                interestAmount, super.getCurrentBalance(), transactionHistory);
        transactionHistory.add(t);
    }

    public void calculateMonthlyInterest() throws AccountNotActiveException {
        BigDecimal currentBalance = super.getCurrentBalance();
        if (this.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(this.getAccountNumber() + " is not active. Cannot credit interest.");
        }
        BigDecimal interestAmount = currentBalance.multiply((this.monthlyInterestRate));
        super.setCurrentBalance(currentBalance.add(interestAmount));
        Transaction t = new Transaction(TransactionType.INTEREST, this,
                interestAmount, super.getCurrentBalance(), transactionHistory);
        transactionHistory.add(t);
    }

    @Override
    public void withdraw(BigDecimal amount) throws AccountNotActiveException,
            InvalidAmountException, InsufficientBalanceException, TransactionLimitExceededException,
            MinimumBalanceException {
        resetIfNextMonth();
        if (this.transactionLimitCounter >= 3) {
            throw new TransactionLimitExceededException("Max Transaction Limit is 3");
        }
        super.withdraw(amount);
        this.transactionLimitCounter++;
    }

    public void resetIfNextMonth() {
        int nowMonth = LocalDate.now().getMonthValue();
        if (nowMonth != currentMonth) {
            this.transactionLimitCounter = 0;
            this.currentMonth = nowMonth;
        }
    }

}
