package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    Account getAccountById(int accountId);
    Account getAccountByUserId(int userId);
    void createAccount(Account account);
    void updateAccount(Account account);
    void deleteAccount(int accountId);
    List<Account> getAllAccounts();
    void updateAccountBalance(int accountId, BigDecimal amount);
}