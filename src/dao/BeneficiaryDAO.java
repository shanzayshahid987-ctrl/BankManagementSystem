package dao;

import java.sql.*;
import beneficiary.Beneficiary;

public class BeneficiaryDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/BankManagementSystem";
    private static final String USER = "postgres";
    private static final String PASSWORD = "intelcorei5xtreme";

    public int save(Beneficiary beneficiary, int customerId) {
        String sql = "INSERT INTO beneficiaries (beneficiary_name, account_number, nickname, customer_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, beneficiary.getBeneficiaryName());
            pstmt.setString(2, beneficiary.getAccountNumber());
            pstmt.setString(3, beneficiary.getNickname());
            pstmt.setInt(4, customerId);

            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error saving beneficiary: " + e.getMessage());
        }
        return -1;
    }
}