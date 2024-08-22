package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(int userId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }

    @Override
    public getByUserId(int userId) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            return mapRowToAccount(results);
        }
        return null;
    }

    @Override
    public boolean updateBalance(int accountId, BigDecimal newBalance) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, newBalance, accountId);
        return rowsAffected > 0;
    }

    private com.techelevator.tenmo.server.model.Account mapRowToAccount(SqlRowSet rs) {
        com.techelevator.tenmo.server.model.Account account = new com.techelevator.tenmo.server.model.Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}

