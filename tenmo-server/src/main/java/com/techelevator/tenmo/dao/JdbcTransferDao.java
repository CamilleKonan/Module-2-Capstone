package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDao implements TransferDao {
    private final Connection connection;

    public JdbcTransferDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfers (transferTypeId, transferStatusId, accountFrom, accountTo, amount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transfer.getTransferTypeId());
            pstmt.setInt(2, transfer.getTransferStatusId());
            pstmt.setInt(3, transfer.getAccountFrom());
            pstmt.setInt(4, transfer.getAccountTo());
            pstmt.setBigDecimal(5, transfer.getAmount());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating transfer", e);
        }
    }

    @Override
    public Transfer getTransferById(int transferId) {
        String sql = "SELECT * FROM transfers WHERE transferId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transferId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Transfer transfer = new Transfer();
                transfer.setTransferId(rs.getInt("transferId"));
                transfer.setTransferTypeId(rs.getInt("transferTypeId"));
                transfer.setTransferStatusId(rs.getInt("transferStatusId"));
                transfer.setAccountFrom(rs.getInt("accountFrom"));
                transfer.setAccountTo(rs.getInt("accountTo"));
                transfer.setAmount(rs.getBigDecimal("amount"));
                return transfer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving transfer by ID", e);
        }
        return null;
    }

    @Override
    public void updateTransfer(Transfer transfer) {
        String sql = "UPDATE transfers SET transferTypeId = ?, transferStatusId = ?, accountFrom = ?, accountTo = ?, amount = ? WHERE transferId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transfer.getTransferTypeId());
            pstmt.setInt(2, transfer.getTransferStatusId());
            pstmt.setInt(3, transfer.getAccountFrom());
            pstmt.setInt(4, transfer.getAccountTo());
            pstmt.setBigDecimal(5, transfer.getAmount());
            pstmt.setInt(6, transfer.getTransferId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating transfer", e);
        }
    }

    @Override
    public void deleteTransfer(int transferId) {
        String sql = "DELETE FROM transfers WHERE transferId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transferId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting transfer", e);
        }
    }

    @Override
    public List<Transfer> getAllTransfers() {
        String sql = "SELECT * FROM transfers";
        List<Transfer> transfers = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Transfer transfer = new Transfer();
                transfer.setTransferId(rs.getInt("transferId"));
                transfer.setTransferTypeId(rs.getInt("transferTypeId"));
                transfer.setTransferStatusId(rs.getInt("transferStatusId"));
                transfer.setAccountFrom(rs.getInt("accountFrom"));
                transfer.setAccountTo(rs.getInt("accountTo"));
                transfer.setAmount(rs.getBigDecimal("amount"));
                transfers.add(transfer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all transfers", e);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getTransfersByAccountId(int accountId) {
        String sql = "SELECT * FROM transfers WHERE accountFrom = ? OR accountTo = ?";
        List<Transfer> transfers = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, accountId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Transfer transfer = new Transfer();
                transfer.setTransferId(rs.getInt("transferId"));
                transfer.setTransferTypeId(rs.getInt("transferTypeId"));
                transfer.setTransferStatusId(rs.getInt("transferStatusId"));
                transfer.setAccountFrom(rs.getInt("accountFrom"));
                transfer.setAccountTo(rs.getInt("accountTo"));
                transfer.setAmount(rs.getBigDecimal("amount"));
                transfers.add(transfer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving transfers by account ID", e);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getTransfersByStatus(int statusId) {
        String sql = "SELECT * FROM transfers WHERE transferStatusId = ?";
        List<Transfer> transfers = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, statusId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Transfer transfer = new Transfer();
                transfer.setTransferId(rs.getInt("transferId"));
                transfer.setTransferTypeId(rs.getInt("transferTypeId"));
                transfer.setTransferStatusId(rs.getInt("transferStatusId"));
                transfer.setAccountFrom(rs.getInt("accountFrom"));
                transfer.setAccountTo(rs.getInt("accountTo"));
                transfer.setAmount(rs.getBigDecimal("amount"));
                transfers.add(transfer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving transfers by status", e);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getTransfersByType(int typeId) {
        String sql = "SELECT * FROM transfers WHERE transferTypeId = ?";
        List<Transfer> transfers = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, typeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Transfer transfer = new Transfer();
                transfer.setTransferId(rs.getInt("transferId"));
                transfer.setTransferTypeId(rs.getInt("transferTypeId"));
                transfer.setTransferStatusId(rs.getInt("transferStatusId"));
                transfer.setAccountFrom(rs.getInt("accountFrom"));
                transfer.setAccountTo(rs.getInt("accountTo"));
                transfer.setAmount(rs.getBigDecimal("amount"));
                transfers.add(transfer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving transfers by type", e);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getTransfersByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        String sql = "SELECT * FROM transfers WHERE amount BETWEEN ? AND ?";
        List<Transfer> transfers = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, minAmount);
            pstmt.setBigDecimal(2, maxAmount);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Transfer transfer = new Transfer();
                transfer.setTransferId(rs.getInt("transferId"));
                transfer.setTransferTypeId(rs.getInt("transferTypeId"));
                transfer.setTransferStatusId(rs.getInt("transferStatusId"));
                transfer.setAccountFrom(rs.getInt("accountFrom"));
                transfer.setAccountTo(rs.getInt("accountTo"));
                transfer.setAmount(rs.getBigDecimal("amount"));
                transfers.add(transfer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving transfers by amount range", e);
        }
        return transfers;
    }
}