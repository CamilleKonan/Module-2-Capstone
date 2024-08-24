package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    Transfer getTransferById(int transferId);
    boolean createTransfer(Transfer transfer);
    List<Transfer> getTransfersByUserId(int userId);
    List<Transfer> getPendingTransfers(int userId);
    boolean updateTransferStatus(int transferId, int statusId);
}
