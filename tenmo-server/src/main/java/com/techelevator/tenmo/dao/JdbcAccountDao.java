package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcAccountDao implements AccountDao {
    private final Connection connection;

    public JdbcAccountDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM accounts WHERE accountId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
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
        return null;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        String sql = "SELECT * FROM accounts WHERE userId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
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
        return null;
    }

    @Override
    public void createAccount(Account account) {
        String sql = "INSERT INTO accounts (userId, balance) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, account.getUserId());
            pstmt.setBigDecimal(2, account.getBalance());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating account", e);
        }
    }

    @Override
    public void updateAccount(Account account) {
        String sql = "UPDATE accounts SET userId = ?, balance = ? WHERE accountId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, account.getUserId());
            pstmt.setBigDecimal(2, account.getBalance());
            pstmt.setInt(3, account.getAccountId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating account", e);
        }
    }

    @Override
    public void deleteAccount(int accountId) {
        String sql = "DELETE FROM accounts WHERE accountId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting account", e);
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        String sql = "SELECT * FROM accounts";
        List<Account> accounts = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
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

    @Override
    public void updateAccountBalance(int accountId, BigDecimal amount) {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE accountId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, amount);
            pstmt.setInt(2, accountId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating account balance", e);
        }
    }
}