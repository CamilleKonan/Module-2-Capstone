package com.techelevator.tenmo.controller;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/transfer")
public class TransferController {
private TransferDao transferDao;


    @GetMapping("/{transferId}")
    public Transfer getTransferById(@PathVariable int transferId) {
        return transferDao.getTransferById(transferId);
    }
    @PostMapping("/create")
    public String createTransfer(@RequestBody Transfer transfer) {
        boolean success = transferDao.createTransfer(transfer);
        return success ? "Transfer created successfully" : "Failed to create transfer";
    }

    @GetMapping("/user/{userId}")
    public List<Transfer> getTransfersByUserId(@PathVariable int userId) {
        return transferDao.getTransfersByUserId(userId);
    }

    @GetMapping("/pending/{userId}")
    public List<Transfer> getPendingTransfers(@PathVariable int userId) {
        return transferDao.getPendingTransfers(userId);
    }
    @PutMapping("/status/{transferId}")
    public String updateTransferStatus(@PathVariable int transferId, @RequestParam int statusId) {
        boolean success = transferDao.updateTransferStatus(transferId, statusId);
        return success ? "Transfer status updated successfully" : "Failed to update transfer status";
    }
}