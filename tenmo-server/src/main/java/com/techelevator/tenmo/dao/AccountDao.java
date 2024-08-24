package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getBalance(int userId);

    Account getAccountById(int userId);

    boolean updateBalance(int accountId, BigDecimal newBalance);
}
