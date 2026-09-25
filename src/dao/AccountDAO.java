package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;
import account.Account;
import account.AccountStatus;
import account.SavingAccount;
import customer.Customer;
import exceptions.InvalidPinException;
import account.CurrentAccount;

public class AccountDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/BankManagementSystem";
    private static final String USER = "postgres";
    private static final String PASSWORD = "intelcorei5xtreme";

    public int save(Account account, int customerId) {
        String sql = "INSERT INTO accounts (account_number, account_type, balance, status, hashed_pin, customer_id, interest_rate, withdrawal_count) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, account.getAccountNumber());

            if (account instanceof SavingAccount) {
                pstmt.setString(2, "SAVINGS");
            } else {
                pstmt.setString(2, "CURRENT");
            }

            pstmt.setBigDecimal(3, account.getCurrentBalance());
            pstmt.setString(4, account.getAccountStatus().toString());
            pstmt.setString(5, account.getHashedPin());
            pstmt.setInt(6, customerId);

            if (account instanceof SavingAccount) {
                pstmt.setBigDecimal(7, ((SavingAccount) account).getYearlyInterestRate());
                pstmt.setInt(8, ((SavingAccount) account).getTransactionLimitCounter());
            } else {
                pstmt.setNull(7, java.sql.Types.NUMERIC);
                pstmt.setInt(8, 0);
            }

            pstmt.executeUpdate();
            System.out.println("Account saved to database.");

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error saving account: " + e.getMessage());
        }
        return -1;
    }

    public ArrayList<Account> findByCustomerId(int customerId, Customer customer) {
        ArrayList<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String accountNumber = rs.getString("account_number");
                    String accountType = rs.getString("account_type");
                    BigDecimal balance = rs.getBigDecimal("balance");
                    AccountStatus status = AccountStatus.valueOf(rs.getString("status"));
                    String hashedPin = rs.getString("hashed_pin");

                    Account account;

                    if (accountType.equals("SAVINGS")) {
                        int withdrawalCount = rs.getInt("withdrawal_count");
                        account = new SavingAccount(accountNumber, status, balance,
                                hashedPin, customer, null, withdrawalCount);

                    } else if (accountType.equals("CURRENT")) {
                        account = new CurrentAccount(accountNumber, status, balance,
                                hashedPin, customer, null);

                    } else {
                        System.out.println("Unknown account type: " + accountType + " — skipping row.");
                        continue;
                    }

                    accounts.add(account);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching accounts: " + e.getMessage());
        } catch (InvalidPinException e) {
            System.out.println("Error reconstructing account PIN: " + e.getMessage());
        }

        return accounts;
    }

    public boolean updateBalance(String accountNumber, BigDecimal newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, newBalance);
            pstmt.setString(2, accountNumber);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error updating balance: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStatus(String accountNumber, AccountStatus status) {
        String sql = "UPDATE accounts SET status = ? WHERE account_number = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.toString());
            pstmt.setString(2, accountNumber);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error updating account status: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePin(String accountNumber, String hashedPin) {
    String sql = "UPDATE accounts SET hashed_pin = ? WHERE account_number = ?";

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, hashedPin);
        pstmt.setString(2, accountNumber);

        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;

    } catch (SQLException e) {
        System.out.println("Error updating PIN: " + e.getMessage());
        return false;
    }
}
}