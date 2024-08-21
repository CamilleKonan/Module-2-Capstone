package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JdbcTransferDao handles the data access operations related to 'Transfer' objects using JDBC.
 * It provides methods for creating, updating, deleting, and querying transfers in the database.
 */
public class JdbcTransferDao implements TransferDao {

    // Database connection object
    private final Connection connection;

    /**
     * Constructor that initializes the JdbcTransferDao with a given database connection.
     *
     * @param connection the database connection to be used for operations.
     */
    public JdbcTransferDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates a new transfer record in the database.
     *
     * @param transfer the Transfer object containing the transfer details to be inserted.
     */
    @Override
    public void createTransfer(Transfer transfer) {
        // SQL query for inserting a new transfer record
        String sql = "INSERT INTO transfers (transferTypeId, transferStatusId, accountFrom, accountTo, amount) VALUES (?, ?, ?, ?, ?)";

        // Try-with-resources block ensures the PreparedStatement will be closed automatically
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the values from the Transfer object into the query parameters
            pstmt.setInt(1, transfer.getTransferTypeId());
            pstmt.setInt(2, transfer.getTransferStatusId());
            pstmt.setInt(3, transfer.getAccountFrom());
            pstmt.setInt(4, transfer.getAccountTo());
            pstmt.setBigDecimal(5, transfer.getAmount());

            // Execute the query to insert the transfer
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Handle SQL exception and rethrow as a RuntimeException
            e.printStackTrace();
            throw new RuntimeException("Error creating transfer", e);
        }
    }

    /**
     * Retrieves a transfer by its ID from the database.
     *
     * @param transferId the ID of the transfer to retrieve.
     * @return the Transfer object if found, or null if not found.
     */
    @Override
    public Transfer getTransferById(int transferId) {
        // SQL query to select a transfer by ID
        String sql = "SELECT * FROM transfers WHERE transferId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the transferId parameter in the query
            pstmt.setInt(1, transferId);

            // Execute the query and get the result set
            ResultSet rs = pstmt.executeQuery();

            // If a transfer is found, create and return the Transfer object
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
            // Handle SQL exception and rethrow as a RuntimeException
            e.printStackTrace();
            throw new RuntimeException("Error retrieving transfer by ID", e);
        }
        // Return null if no transfer was found with the given ID
        return null;
    }

    /**
     * Updates an existing transfer in the database.
     *
     * @param transfer the Transfer object containing updated transfer details.
     */
    @Override
    public void updateTransfer(Transfer transfer) {
        // SQL query to update a transfer record
        String sql = "UPDATE transfers SET transferTypeId = ?, transferStatusId = ?, accountFrom = ?, accountTo = ?, amount = ? WHERE transferId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the updated values from the Transfer object into the query parameters
            pstmt.setInt(1, transfer.getTransferTypeId());
            pstmt.setInt(2, transfer.getTransferStatusId());
            pstmt.setInt(3, transfer.getAccountFrom());
            pstmt.setInt(4, transfer.getAccountTo());
            pstmt.setBigDecimal(5, transfer.getAmount());
            pstmt.setInt(6, transfer.getTransferId());

            // Execute the query to update the transfer
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Handle SQL exception and rethrow as a RuntimeException
            e.printStackTrace();
            throw new RuntimeException("Error updating transfer", e);
        }
    }

    /**
     * Deletes a transfer record by its ID from the database.
     *
     * @param transferId the ID of the transfer to delete.
     */
    @Override
    public void deleteTransfer(int transferId) {
        // SQL query to delete a transfer by its ID
        String sql = "DELETE FROM transfers WHERE transferId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the transferId parameter in the query
            pstmt.setInt(1, transferId);

            // Execute the query to delete the transfer
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Handle SQL exception and rethrow as a RuntimeException
            e.printStackTrace();
            throw new RuntimeException("Error deleting transfer", e);
        }
    }

    /**
     * Retrieves all transfer records from the database.
     *
     * @return a list of Transfer objects containing all transfers.
     */
    @Override
    public List<Transfer> getAllTransfers() {
        // SQL query to select all transfers
        String sql = "SELECT * FROM transfers";
        List<Transfer> transfers = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // Iterate through the result set and populate the list with Transfer objects
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
            // Handle SQL exception and rethrow as a RuntimeException
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all transfers", e);
        }
        return transfers;
    }

    /**
     * Retrieves all transfers involving a specific account, either as sender or receiver.
     *
     * @param accountId the ID of the account to search for transfers.
     * @return a list of Transfer objects for the given account.
     */
    @Override
    public List<Transfer> getTransfersByAccountId(int accountId) {
        // SQL query to select transfers where the account is either sending or receiving funds
        String sql = "SELECT * FROM transfers WHERE accountFrom = ? OR accountTo = ?";
        List<Transfer> transfers = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the accountId as both sender and receiver in the query
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, accountId);

            // Execute the query and populate the list with Transfer objects
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
            // Handle SQL exception and rethrow as a RuntimeException
            e.printStackTrace();
            throw new RuntimeException("Error retrieving transfers by account ID", e);
        }
        return transfers;
    }

    /**
     * Retrieves all transfers with a specific status from the database.
     *
     * @param statusId the ID of the status to filter transfers.
     * @return a list of Transfer objects with the given status.
     */
    @Override
    public List<Transfer> getTransfersByStatus(int statusId) {
        // SQL query to select transfers by status ID
        String sql = "SELECT * FROM transfers WHERE transferStatusId = ?";
        List<Transfer> transfers = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the statusId parameter in the query
            pstmt.setInt(1, statusId);

            // Execute the query and populate the list with Transfer objects
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
            // Handle SQL exception and rethrow as a RuntimeException
            e.printStackTrace();
            throw new RuntimeException("Error retrieving transfers by status", e);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getTransfersByType(int typeId) {
        return List.of();
    }

    @Override
    public List<Transfer> getTransfersByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        return List.of();
    }

    @Override
    public TransferStatus getTransferStatusById(int statusId) {
        return null;
    }

    @Override
    public List<TransferStatus> getAllTransferStatuses() {
        return List.of();
    }
}