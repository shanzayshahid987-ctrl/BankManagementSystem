package account;

import java.math.BigDecimal;
import java.util.ArrayList;

import customer.Customer;
import exceptions.*;

public class CurrentAccount extends Account {

    private final BigDecimal accountLimit = BigDecimal.valueOf(10000);

    public CurrentAccount(ArrayList<Customer> customers ,String PIN, BigDecimal balance)
    throws InvalidPinException, MinimumBalanceException{
        super(customers, PIN, balance);
        if(balance.compareTo(accountLimit)<0){
            throw new MinimumBalanceException
            ("Current account requires a minimum opening balance of 10,000");
        }
    }

    @Override
    public void monthlyInterestRate() {
        // Current accounts do not earn interest.
    }

    @Override
    public void yearlyInterestRate() {
        // Current accounts do not earn interest.
    }
    
    @Override 
    public void withdraw(BigDecimal amount)throws AccountNotActiveException,
            InvalidAmountException, MinimumBalanceException{
                BigDecimal balance = super.getCurrentBalance();
                if(balance.subtract(amount).compareTo(accountLimit)<0){
                    throw new MinimumBalanceException
                    ("Withdrawal will drop balance below, minimum 10,000 required ");
                }
                super.withdraw(amount);
            }
}
