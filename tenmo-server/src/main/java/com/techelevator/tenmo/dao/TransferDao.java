package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    void createTransfer(Transfer transfer);
    Transfer getTransferById(int transferId);
    void updateTransfer(Transfer transfer);
    void deleteTransfer(int transferId);
    List<Transfer> getAllTransfers();
    List<Transfer> getTransfersByAccountId(int accountId);
    List<Transfer> getTransfersByStatus(int statusId);
    List<Transfer> getTransfersByType(int typeId);
    List<Transfer> getTransfersByAmountRange(BigDecimal minAmount, BigDecimal maxAmount);

    // New methods for managing TransferStatus
    TransferStatus getTransferStatusById(int statusId);
    List<TransferStatus> getAllTransferStatuses();
}