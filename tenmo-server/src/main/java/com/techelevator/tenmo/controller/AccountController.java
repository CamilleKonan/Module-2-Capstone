package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;


@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/account")
public class AccountController {
    private final JdbcAccountDao jdbcAccountDao;
    private final JdbcUserDao jdbcUserDao;

    public AccountController(JdbcAccountDao jdbcAccountDao, JdbcUserDao jdbcUserDao) {
        this.jdbcAccountDao = jdbcAccountDao;
        this.jdbcUserDao = jdbcUserDao;
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(Principal principal) {
        String username = principal.getName();
        int userId = jdbcUserDao.getUserByUsername(username).getId();
        BigDecimal balance = jdbcAccountDao.getBalance(userId);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Account> getAccount(Principal principal) {
        String username = principal.getName();
        int userId = jdbcUserDao.getUserByUsername(username).getId();
        Account account = jdbcAccountDao.getAccountById(userId);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
