package account;

import exceptions.*;
import security.*;
import java.util.Random;
import java.util.ArrayList;
import java.math.BigDecimal;
import transaction.Transaction;
import transaction.TransactionType;
import customer.*;

public abstract class Account {
    private String accountNumber;
    private AccountStatus status;
    private BigDecimal currentBalance;
    private TransactionPIN pin;
    protected ArrayList<Transaction> transactionHistory;
    private Customer customer;
    private ArrayList<Customer> customers;

    public Account(Customer customer, ArrayList<Customer> customers, String PIN, BigDecimal balance)
            throws InvalidPinException {
        this.customer = customer;
        this.customers = customers;
        this.transactionHistory = new ArrayList<>();
        this.accountNumber = generateUniqueAccountNumber();
        this.status = AccountStatus.ACTIVE;
        this.currentBalance = balance;
        this.pin = new TransactionPIN(PIN);
    }

    public String generateAccountNumber() {
        Random rand = new Random();
        long number = 1000000000L + (long) (rand.nextDouble() * 9000000000L);
        return "ACC" + number;
    }

    public String generateUniqueAccountNumber() {
        String accNum;
        boolean exist;
        do {
            accNum = generateAccountNumber();
            exist = false;
            for (Account a : this.allAccounts(this.customers)) {
                if (a.getAccountNumber().equals(accNum)) {
                    exist = true;
                    break;
                }
            }

        } while (exist);
        return accNum;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public AccountStatus getAccountStatus() {
        return this.status;
    }

    public BigDecimal getCurrentBalance() {
        return this.currentBalance;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public ArrayList<Transaction> getTransactionHistory(){
        return this.transactionHistory;
    }

    public void statusFrozen() {
        this.status = AccountStatus.FROZEN;
    }

    public void statusClosed() {
        this.status = AccountStatus.CLOSED;
    }

    public void statusActive() {
        this.status = AccountStatus.ACTIVE;
    }

    public void setTransactionPIN(String PIN) throws InvalidPinException {
        this.pin = new TransactionPIN(PIN);
    }

    public void setCurrentBalance(BigDecimal amount) {
        this.currentBalance = amount;
    }

    public void deposit(BigDecimal amount) throws AccountNotActiveException,
            InvalidAmountException {
        if (this.status != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(this.accountNumber + "is not currently active.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Invalid Amount.");
        }
        this.currentBalance = this.currentBalance.add(amount);
        Transaction t = new Transaction(TransactionType.DEPOSIT, this, amount, currentBalance, this.transactionHistory);
        transactionHistory.add(t);
    }

    public void withdraw(BigDecimal amount) throws AccountNotActiveException,
            InvalidAmountException, InsufficientBalanceException,
            TransactionLimitExceededException, MinimumBalanceException {
        if (this.status != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(this.accountNumber + "is not currently active.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Invalid Amount.");
        }
        if (amount.compareTo(this.currentBalance) > 0) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }
        this.currentBalance = this.currentBalance.subtract(amount);
        Transaction t = new Transaction(TransactionType.WITHDRAW, this, amount, currentBalance,
                this.transactionHistory);
        transactionHistory.add(t);
    }

    public ArrayList<Account> allAccounts(ArrayList<Customer> customers) {
        ArrayList<Account> accounts = new ArrayList<>();
        for (Customer c : customers) {
            accounts.addAll(c.getAccounts());

        }
        return accounts;
    }

}
