package dao;

import java.sql.*;
import java.util.ArrayList;

import admin.Admin;
import exceptions.InvalidPasswordFormatException;

public class AdminDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/BankManagementSystem";
    private static final String USER = "postgres";
    private static final String PASSWORD = "intelcorei5xtreme";

    public int save(Admin admin) {
        String sql = "INSERT INTO admins (username, hashed_password) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, admin.getUsername());
            pstmt.setString(2, admin.getHashedPassword());

            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next())
                    return keys.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error saving admin: " + e.getMessage());
        }
        return -1;
    }

    public ArrayList<Admin> findAll() {
        ArrayList<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM admins";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String hashedPassword = rs.getString("hashed_password");

                Admin admin = new Admin(username, hashedPassword, true);
                admins.add(admin);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching admins: " + e.getMessage());
        }catch (InvalidPasswordFormatException e) {
            System.out.println("Error reconstructing customer password: " + e.getMessage());
        }

        return admins;
    }
}