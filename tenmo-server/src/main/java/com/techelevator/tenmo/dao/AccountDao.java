package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.server.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getBalance(int userId);


    Account getUserById(int userId);

    boolean updateBalance(int accountId, BigDecimal newBalance);
}
