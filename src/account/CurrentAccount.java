package account;

import java.math.BigDecimal;
import java.util.ArrayList;

import customer.Customer;
import exceptions.*;
import transaction.Transaction;
import transaction.TransactionType;

public class CurrentAccount extends Account  {
    private final BigDecimal accountLimit = BigDecimal.valueOf(10000);

    public CurrentAccount(String accountNumber, AccountStatus status, BigDecimal balance,
            String hashedPin, Customer customer, ArrayList<Customer> customers)
            throws InvalidPinException {
        super(accountNumber, status, balance, hashedPin, customer, customers);
    }

    public CurrentAccount(Customer custom, ArrayList<Customer> customers, String PIN, BigDecimal balance)
            throws InvalidPinException, MinimumBalanceException {
        super(custom, customers, PIN, balance);
        if (balance.compareTo(accountLimit) < 0) {
            throw new MinimumBalanceException("Current account requires a minimum opening balance of 10,000");
        }
    }

    public BigDecimal getAccountLimit() {
        return this.accountLimit;
    }

    @Override
    public void withdraw(BigDecimal amount) throws AccountNotActiveException,
            InvalidAmountException, MinimumBalanceException,
            InsufficientBalanceException, TransactionLimitExceededException {
        BigDecimal balance = super.getCurrentBalance();
        if (balance.subtract(amount).compareTo(accountLimit) < 0) {
            throw new MinimumBalanceException("Withdrawal will drop balance below, minimum 10,000 required ");
        }
        super.withdraw(amount);
    }

    public void transfer(Account targetAccount, BigDecimal amount)
            throws AccountNotActiveException, InvalidAmountException, MinimumBalanceException,
            InsufficientBalanceException, TransactionLimitExceededException {
        super.withdraw(amount);
        targetAccount.deposit(amount);

        Transaction t = new Transaction(TransactionType.TRANSFER, this, amount,
                super.getCurrentBalance(), transactionHistory);
        t.setTargetAccount(targetAccount);
        transactionHistory.add(t);
    }
}
