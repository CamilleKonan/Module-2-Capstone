package com.techelevator.tenmo.controller;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/transfers")
public class TransferController {
    private final JdbcTransferDao transferDao;
    private final AccountDao accountDao;

    public TransferController(JdbcTransferDao transferDao, AccountDao accountDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{transferId}")
    public TransferDto getTransferById(@PathVariable int transferId) {

        return transferDao.getTransferById(transferId);
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createTransfer(@RequestBody TransferDto transfer) {
        System.out.println(transfer.getTransferStatusId()+ " " + transfer.getTransferTypeId()+ " " +transfer.getAccountFrom()+ " " +transfer.getAccountTo() + " " + transfer.getAmount());
        int accountFromId = accountDao.getAccountById(transfer.getAccountFrom()).getAccountId();
        int accountToId = accountDao.getAccountById(transfer.getAccountTo()).getAccountId();
        transfer.setAccountFrom(accountFromId);
        transfer.setAccountTo(accountToId);
        boolean success = transferDao.createTransfer(transfer);
        return success ? "Transfer created successfully" : "Failed to create transfer";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{userId}")
    public List<TransferDto> getTransfersByUserId(@PathVariable int userId) {
        return transferDao.getTransfersByUserId(userId);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/pending/{userId}")
    public List<TransferDto> getPendingTransfers(@PathVariable int userId) {
        Account accountTo = accountDao.getAccountById(userId);
        return transferDao.getPendingTransfers(accountTo.getAccountId());
    }
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/status/{transferId}")
    public String updateTransferStatus(@PathVariable int transferId, @RequestParam int statusId) {
        boolean success = transferDao.updateTransferStatus(transferId, statusId);
        return success ? "Transfer status updated successfully" : "Failed to update transfer status";
    }
    @PutMapping("/approve/{transferId}")
    public String updateTransferApprove(@PathVariable int transferId) {
        TransferDto transfer = transferDao.getTransferById(transferId);

        Account accountFrom = accountDao.getAccountByAccountId(transfer.getAccountFrom());
        Account accountTo = accountDao.getAccountByAccountId(transfer.getAccountTo());

        accountFrom.withdraw(transfer.getAmount());
        accountTo.deposit(transfer.getAmount());

        accountDao.updateBalance(accountFrom.getAccountId(), accountFrom.getBalance());
        accountDao.updateBalance(accountTo.getAccountId(), accountTo.getBalance());

        boolean success = transferDao.updateTransferStatus(transferId, 2);
        return success ? "Transfer status updated successfully" : "Failed to update transfer status";
    }
}