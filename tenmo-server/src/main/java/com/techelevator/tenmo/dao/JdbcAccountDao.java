package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JdbcAccountDao is responsible for interacting with the database to perform CRUD operations
 * on the `accounts` table. It uses JDBC to connect to the database and execute SQL queries.
 */
public class JdbcAccountDao implements AccountDao {
    private final Connection connection;

    /**
     * Constructor that accepts a database connection.
     * @param connection the Connection object to the database.
     */
    public JdbcAccountDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves an account by its accountId.
     * @param accountId the ID of the account to retrieve.
     * @return the Account object if found, null otherwise.
     */
    @Override
    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM accounts WHERE accountId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the accountId in the prepared statement
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();

            // If an account is found, create an Account object and return it
            if (rs.next()) {
                Account account = new Account();
                account.setAccountId(rs.getInt("accountId"));
                account.setUserId(rs.getInt("userId"));
                account.setBalance(rs.getBigDecimal("balance"));
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving account by ID", e);
        }
        return null; // Return null if no account is found
    }

    /**
     * Retrieves an account by its associated userId.
     * @param userId the ID of the user whose account is to be retrieved.
     * @return the Account object if found, null otherwise.
     */
    @Override
    public Account getAccountByUserId(int userId) {
        String sql = "SELECT * FROM accounts WHERE userId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the userId in the prepared statement
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            // If an account is found, create an Account object and return it
            if (rs.next()) {
                Account account = new Account();
                account.setAccountId(rs.getInt("accountId"));
                account.setUserId(rs.getInt("userId"));
                account.setBalance(rs.getBigDecimal("balance"));
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving account by User ID", e);
        }
        return null; // Return null if no account is found
    }

    /**
     * Creates a new account in the database.
     * @param account the Account object containing the userId and initial balance.
     */
    @Override
    public void createAccount(Account account) {
        String sql = "INSERT INTO accounts (userId, balance) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the userId and balance in the prepared statement
            pstmt.setInt(1, account.getUserId());
            pstmt.setBigDecimal(2, account.getBalance());
            pstmt.executeUpdate(); // Execute the insert statement
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating account", e);
        }
    }

    /**
     * Updates an existing account in the database.
     * @param account the Account object containing the updated userId and balance.
     */
    @Override
    public void updateAccount(Account account) {
        String sql = "UPDATE accounts SET userId = ?, balance = ? WHERE accountId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the updated userId, balance, and accountId in the prepared statement
            pstmt.setInt(1, account.getUserId());
            pstmt.setBigDecimal(2, account.getBalance());
            pstmt.setInt(3, account.getAccountId());
            pstmt.executeUpdate(); // Execute the update statement
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating account", e);
        }
    }

    /**
     * Deletes an account from the database using its accountId.
     * @param accountId the ID of the account to delete.
     */
    @Override
    public void deleteAccount(int accountId) {
        String sql = "DELETE FROM accounts WHERE accountId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the accountId in the prepared statement
            pstmt.setInt(1, accountId);
            pstmt.executeUpdate(); // Execute the delete statement
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting account", e);
        }
    }

    /**
     * Retrieves a list of all accounts in the database.
     * @return a List of Account objects representing all accounts.
     */
    @Override
    public List<Account> getAllAccounts() {
        String sql = "SELECT * FROM accounts";
        List<Account> accounts = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // Iterate through the result set and populate the accounts list
            while (rs.next()) {
                Account account = new Account();
                account.setAccountId(rs.getInt("accountId"));
                account.setUserId(rs.getInt("userId"));
                account.setBalance(rs.getBigDecimal("balance"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all accounts", e);
        }
        return accounts;
    }

    /**
     * Updates the balance of an account by adding a specified amount.
     * @param accountId the ID of the account whose balance is to be updated.
     * @param amount the amount to add to the current balance.
     */
    @Override
    public void updateAccountBalance(int accountId, BigDecimal amount) {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE accountId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the amount and accountId in the prepared statement
            pstmt.setBigDecimal(1, amount);
            pstmt.setInt(2, accountId);
            pstmt.executeUpdate(); // Execute the update statement
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating account balance", e);
        }
    }
}