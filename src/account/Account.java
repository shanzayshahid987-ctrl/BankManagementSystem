import exceptions.*;
import transactionpin.*;
import java.util.Random;
import java.math.BigDecimal;

public class Account {
    private String accountNumber;
    private AccountStatus status;
    private BigDecimal currentBalance;
    private TransactionPIN pin;

    public Account(String PIN, BigDecimal balance) throws InvalidPinException {
        this.accountNumber = generateAccountNumber();
        this.status = ACTIVE;
        this.currentBalance = balance;
        this.pin = new TransactionPIN(PIN);
    }

    public String generateAccountNumber() {
        Random rand = new Random();
        long number = 1000000000L + (long) (rand.nextDouble() * 9000000000L);
        return "ACC" + number;
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

    public void statusFrozen() {
        this.status = FROZEN;
    }

    public void statusClosed() {
        this.status = CLOSED;
    }

    public void statusActive() {
        this.status = ACTIVE;
    }

    public void setTransactionPIN(String PIN) throws InvalidPinException {
        this.pin = new TransactionPIN(PIN);
    }

    public void deposit(BigDecimal amount) throws AccountNotActiveException,
            InvalidAmountException {
        if (this.status != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(this.accountNumber + "is not currently active.");
        }
        if (amount.compareTo(BigDecimal.ZER0) <= 0) {
            throw new InvalidAmountException("Invalid Amount.");
        }
        this.currentBalance = this.currentBalance.add(amount);
    }

    public void withdraw(BigDecimal amount)throws AccountNotActiveException,
            InvalidAmountException, InsufficientBalanceException {
        if (this.status != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(this.accountNumber + "is not currently active.");
        }
        if (amount.compareTo(BigDecimal.ZER0) <= 0) {
            throw new InvalidAmountException("Invalid Amount.");
        }
        if (amount.compareTo(this.currentBalance) > 0) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }
        this.currentBalance = this.currentBalance.subtract(amount);

    }

}