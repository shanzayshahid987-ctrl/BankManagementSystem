import java.util.ArrayList;
import account.Account;
import account.CurrentAccount;
import account.SavingAccount;
import java.math.BigDecimal;
import customer.*;
import account.Account;
import dao.*;
import transaction.Transaction;
import beneficiary.Beneficiary;
import admin.Admin;

public class DAOTest {
    public static void main(String[] args) {
        try {
            Customer testCustomer = new Customer(
                    "Shanzay", "2007-01-18", "betonicalFL0WERS123!", "shanzayshahidd@example.com",
                    "45678-7777777-3", "0324-1234555", "Staduim Road, karachi", "shanzay");

            ArrayList<Customer> allCustomers = new ArrayList<>();
            allCustomers.add(testCustomer);

            CustomerDAO customerDAO = new CustomerDAO();
            int customerId = customerDAO.save(testCustomer);
            System.out.println("Customer saved with ID: " + customerId);

            if (customerId != -1) {
                Account testAccount = new CurrentAccount(testCustomer, allCustomers, "1234", new BigDecimal("12000"));

                AccountDAO accountDAO = new AccountDAO();
                int accountId = accountDAO.save(testAccount, customerId);
                System.out.println("Account saved with ID: " + accountId);

                if (accountId != -1) {
                    testAccount.withdraw(new BigDecimal("1000")); // this creates a Transaction object internally

                    ArrayList<Transaction> history = testAccount.getTransactionHistory();
                    Transaction latest = history.get(history.size() - 1);

                    TransactionDAO transactionDAO = new TransactionDAO();
                    transactionDAO.save(latest, accountId, null); // null since this isn't a transfer
                    System.out.println("Transaction saved.");

                    Beneficiary testBen = new Beneficiary("Faraz", "ACC1234567890", "loml");
                    BeneficiaryDAO beneficiaryDAO = new BeneficiaryDAO();
                    int benId = beneficiaryDAO.save(testBen, customerId);
                    System.out.println("Beneficiary saved with ID: " + benId);

                    Admin testAdmin = new Admin("Farjad Asghar", "SecurePass123!");
                    AdminDAO adminDAO = new AdminDAO();
                    int adminId = adminDAO.save(testAdmin);
                    System.out.println("Admin saved with ID: " + adminId);

                    System.out.println("=== Testing AccountDAO.findByCustomerId() ===");
                    Customer testC = customerDAO.findByUsername("shanzay");
                    ArrayList<Account> accts = accountDAO.findByCustomerId(testC.getdbId(), testC);
                    System.out.println("Accounts found: " + accts.size());
                    for (Account a : accts) {
                        System.out.println(a.getClass().getSimpleName() + " | " + a.getAccountNumber()
                                + " | Balance: " + a.getCurrentBalance() + " | Status: " + a.getAccountStatus());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}