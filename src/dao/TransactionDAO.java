package dao;

import java.sql.*;
import transaction.Transaction;

public class TransactionDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/BankManagementSystem";
    private static final String USER = "postgres";
    private static final String PASSWORD = "intelcorei5xtreme";

    public void save(Transaction transaction, int accountId, Integer targetAccountId) {
        String sql = "INSERT INTO transactions (transaction_id, type, amount, balance_after, date_time, account_id, target_account_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, transaction.getTransactionID());
            pstmt.setString(2, transaction.getTransactionType().toString());
            pstmt.setBigDecimal(3, transaction.getAmount());
            pstmt.setBigDecimal(4, transaction.getBalanceAfter());
            pstmt.setTimestamp(5, Timestamp.valueOf(transaction.getDateTime()));
            pstmt.setInt(6, accountId);

            if (targetAccountId != null) pstmt.setInt(7, targetAccountId);
            else pstmt.setNull(7, Types.INTEGER);

            pstmt.executeUpdate();
            System.out.println("Transaction saved to database.");

        } catch (SQLException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }
}