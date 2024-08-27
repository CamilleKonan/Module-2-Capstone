package com.techelevator.tenmo.controller;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/transfers")
public class TransferController {
    private final JdbcTransferDao transferDao;

    // Constructor injection of JdbcTransferDao
    public TransferController(JdbcTransferDao transferDao) {

        this.transferDao = transferDao;
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{transferId}")
    public Transfer getTransferById(@PathVariable int transferId) {

        return transferDao.getTransferById(transferId);
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createTransfer(@RequestBody Transfer transfer) {
        boolean success = transferDao.createTransfer(transfer);
        return success ? "Transfer created successfully" : "Failed to create transfer";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{userId}")
    public List<Transfer> getTransfersByUserId(@PathVariable int userId) {
        return transferDao.getTransfersByUserId(userId);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/pending/{userId}")
    public List<Transfer> getPendingTransfers(@PathVariable int userId) {
        return transferDao.getPendingTransfers(userId);
    }
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/status/{transferId}")
    public String updateTransferStatus(@PathVariable int transferId, @RequestParam int statusId) {
        boolean success = transferDao.updateTransferStatus(transferId, statusId);
        return success ? "Transfer status updated successfully" : "Failed to update transfer status";
    }
}