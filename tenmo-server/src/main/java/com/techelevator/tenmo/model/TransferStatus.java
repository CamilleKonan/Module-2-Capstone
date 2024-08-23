package com.techelevator.tenmo.model;



public class TransferStatus {

    private int transferStatusId;
    private String transferStatusDesc;

    public static final TransferStatus PENDING = new TransferStatus(1, "Pending");
    public static final TransferStatus APPROVED = new TransferStatus(2, "Approved");
    public static final TransferStatus REJECTED = new TransferStatus(3, "Rejected");

    public TransferStatus(int transferStatusId, String transferStatusDesc) {
        this.transferStatusId = transferStatusId;
        this.transferStatusDesc = transferStatusDesc;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }
    // To String Method for Displaying Status
    @Override
    public String toString() {
        return "TransferStatus{" + "transferStatusID=" + transferStatusId + ", transferStatusDesc'" +
                transferStatusDesc + '\'' + '}';
    }
}