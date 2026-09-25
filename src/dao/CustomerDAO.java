package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import customer.Customer;
import exceptions.InvalidPasswordFormatException;
import account.Account;

public class CustomerDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/BankManagementSystem";
    private static final String USER = "postgres";
    private static final String PASSWORD = "intelcorei5xtreme";

    private AccountDAO accountDAO = new AccountDAO();

    public int save(Customer customer) {
        String sql = "INSERT INTO customers (full_name, dob, email, cnic, contact_number, home_address, username, hashed_password) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, customer.getFullName());
            pstmt.setDate(2, java.sql.Date.valueOf(customer.getDob()));
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getCNIC());
            pstmt.setString(5, customer.getContactNumber());
            pstmt.setString(6, customer.getHomeAddress());
            pstmt.setString(7, customer.getUsername());
            pstmt.setString(8, customer.getHashedPassword());

            pstmt.executeUpdate();
            System.out.println("Customer saved to database.");

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
        return -1;
    }

    public Customer findByUsername(String username) {
        String sql = "SELECT * FROM customers WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer(
                            rs.getInt("id"),
                            rs.getString("full_name"),
                            rs.getDate("dob").toString(),
                            rs.getString("email"),
                            rs.getString("cnic"),
                            rs.getString("contact_number"),
                            rs.getString("home_address"),
                            rs.getString("username"),
                            rs.getString("hashed_password"));

                    ArrayList<Account> accounts = accountDAO.findByCustomerId(customer.getdbId(), customer);
                    for (Account a : accounts) {
                        customer.addAccount(a);
                    }

                    return customer;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding customer: " + e.getMessage());
        } catch (InvalidPasswordFormatException e) {
            System.out.println("Error reconstructing customer password: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<Customer> findAll() {
        ArrayList<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getDate("dob").toString(),
                        rs.getString("email"),
                        rs.getString("cnic"),
                        rs.getString("contact_number"),
                        rs.getString("home_address"),
                        rs.getString("username"),
                        rs.getString("hashed_password"));

                ArrayList<Account> accounts = accountDAO.findByCustomerId(customer.getdbId(), customer);
                for (Account a : accounts) {
                    customer.addAccount(a);
                }

                customers.add(customer);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching customers: " + e.getMessage());
        } catch (InvalidPasswordFormatException e) {
            System.out.println("Error reconstructing customer password: " + e.getMessage());
        }

        return customers;
    }

    public boolean updatePassword(int customerId, String hashedPassword) {
        String sql = "UPDATE customers SET hashed_password = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hashedPassword);
            pstmt.setInt(2, customerId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    public boolean updateContact(int customerId, String contactNumber) {
        String sql = "UPDATE customers SET contact_number = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, contactNumber);
            pstmt.setInt(2, customerId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error updating contact number: " + e.getMessage());
            return false;
        }
    }

    public boolean updateAddress(int customerId, String homeAddress) {
        String sql = "UPDATE customers SET home_address = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, homeAddress);
            pstmt.setInt(2, customerId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error updating address: " + e.getMessage());
            return false;
        }
    }
}