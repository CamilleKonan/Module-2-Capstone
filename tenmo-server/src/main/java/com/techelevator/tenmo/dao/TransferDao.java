package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;

import java.util.List;

public interface TransferDao {
    TransferDto getTransferById(int transferId);
    boolean createTransfer(TransferDto transfer);
    List<TransferDto> getTransfersByUserId(int userId);
    List<TransferDto> getPendingTransfers(int userId);
    boolean updateTransferStatus(int transferId, int statusId);
}
