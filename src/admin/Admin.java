package admin;

import java.util.ArrayList;
import java.util.ArrayList;
import java.io.Serializable;
import java.math.BigDecimal;

import exceptions.*;
import security.PasswordUtil;
import customer.*;
import account.*;

public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private PasswordUtil password;

    public Admin(String username, String passKey) throws InvalidPasswordFormatException {
        this.username = username;
        this.password = new PasswordUtil(passKey);

    }

    public String getUsername() {
        return this.username;
    }

    public boolean login(String password) {
        return this.password.toVerify(password);
    }

    public Account findAccountByNum(String accNum, ArrayList<Customer> allCustomer) {
        for (Customer c : allCustomer) {
            for (Account a : c.getAccounts()) {
                if (a.getAccountNumber().equals(accNum)) {
                    return a;
                }
            }
        }
        return null;
    }

    public Customer findCustomerByCNIC(String accCNIC, ArrayList<Customer> allCustomer) {
        for (Customer c : allCustomer) {
            if (c.getCNIC().equals(accCNIC)) {
                return c;
            }
        }
        return null;
    }

    public void freezeAccount(String accNum, ArrayList<Customer> allCustomer)
            throws InvalidStatusChangeException {
        Account acc = findAccountByNum(accNum, allCustomer);
        if (acc != null) {
            if (acc.getAccountStatus() == AccountStatus.FROZEN) {
                throw new InvalidStatusChangeException("Account" + accNum + " has already been freozen.");
            }
            if (acc.getAccountStatus() == AccountStatus.CLOSED) {
                throw new InvalidStatusChangeException("Account " + accNum + " has been closed.");
            }
            acc.statusFrozen();
            System.out.println("Account " + accNum + " has been freozen.");
        } else {
            System.out.println("Account " + accNum + "doesn't exist.");
        }
    }

    public void closeAccount(String accNum, ArrayList<Customer> allCustomer)
            throws InvalidStatusChangeException {
        Account acc = findAccountByNum(accNum, allCustomer);
        if (acc != null) {
            if (acc.getAccountStatus() == AccountStatus.CLOSED) {
                throw new InvalidStatusChangeException("Account " + accNum + " has been closed.");
            }
            if (acc != null) {
                acc.statusClosed();
                System.out.println("Account " + accNum + " has been closed.");
            }
        } else {
            System.out.println("Account " + accNum + "doesn't exist.");
        }
    }

    public void reactiveAccount(String accNum, ArrayList<Customer> allCustomer)
            throws InvalidStatusChangeException {

        Account acc = findAccountByNum(accNum, allCustomer);
        if (acc != null) {
            if (acc.getAccountStatus() == AccountStatus.ACTIVE) {
                throw new InvalidStatusChangeException("Account " + accNum + " has already been active.");
            }
            if (acc.getAccountStatus() == AccountStatus.CLOSED) {
                throw new InvalidStatusChangeException("Account " + accNum + " has been closed.");
            }
            acc.statusActive();
            System.out.println("Account " + accNum + " has been activated.");
        } else {
            System.out.println("Account " + accNum + "doesn't exist.");
        }
    }

    public BigDecimal getTotalBankBalance(ArrayList<Customer> allCustomers) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Customer c : allCustomers) {
            for (Account a : c.getAccounts()) {
                if (a.getAccountStatus() != AccountStatus.CLOSED) {
                    sum = sum.add(a.getCurrentBalance());
                }
            }
        }
        return sum;
    }

    public void viewAllCustomers(ArrayList<Customer> allCustomers) {
        for (Customer c : allCustomers) {
            System.out.println("Customer: " + c.getFullName() + "| CNIC: " + c.getCNIC());
            for (Account a : c.getAccounts()) {
                System.out.println(
                        "Account number:  " + a.getAccountNumber() + "| Current Balance: " + a.getCurrentBalance());
            }
        }
    }

}
