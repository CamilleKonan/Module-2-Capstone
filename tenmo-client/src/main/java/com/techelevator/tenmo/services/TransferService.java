package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class TransferService {
    private static final String API_BASE_URL = "http://localhost:8080/transfer/";
    private RestTemplate restTemplate = new RestTemplate();
    private String token = null;
    private AuthenticatedUser currentUser = null;

    public void setCurrentUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Method to send TE Bucks to another user
    public Transfer sendTransfer(int toUserId, BigDecimal amount) {
        Transfer transfer = new Transfer();
        transfer.setAccountFrom(currentUser.getUser().getId());
        transfer.setAccountTo(toUserId);
        transfer.setAmount(amount);
        transfer.setTransferTypeId(1); // Assuming 1 represents "Send"
        transfer.setTransferStatusId(2); // Assuming 2 represents "Approved"

        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(
                    API_BASE_URL + "send",
                    HttpMethod.POST,
                    new HttpEntity<>(transfer, makeAuthHeaders()),
                    Transfer.class
            );
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return null;
        }
    }

    // Method to request TE Bucks from another user
    public Transfer requestTransfer(int fromUserId, BigDecimal amount) {
        Transfer transfer = new Transfer();
        transfer.setAccountFrom(fromUserId);
        transfer.setAccountTo(currentUser.getUser().getId());
        transfer.setAmount(amount);
        transfer.setTransferTypeId(2); // Assuming 2 represents "Request"
        transfer.setTransferStatusId(1); // Assuming 1 represents "Pending"

        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(
                    API_BASE_URL + "request",
                    HttpMethod.POST,
                    new HttpEntity<>(transfer, makeAuthHeaders()),
                    Transfer.class
            );
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return null;
        }
    }

    // Method to view all transfers for the current user
    public List<Transfer> viewTransfers() {
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(
                    API_BASE_URL + "list",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class
            );
            return List.(response.getBody());
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return List();
        }
    }

    // Method to view details of a specific transfer
    public Transfer getTransferDetails(int transferId) {
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(
                    API_BASE_URL + transferId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer.class
            );
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return null;
        }
    }

    // Method to approve or reject a transfer request
    public Transfer updateTransferStatus(int transferId, int newStatusId) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(transferId);
        transfer.setTransferStatusId(newStatusId);

        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(
                    API_BASE_URL + "update",
                    HttpMethod.PUT,
                    new HttpEntity<>(transfer, makeAuthHeaders()),
                    Transfer.class
            );
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return null;
        }
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        if (token != null) {
            headers.setBearerAuth(token);
        } else {
            throw new IllegalStateException("Cannot make a call without a token");
        }
        return new HttpEntity<>(headers);
    }

    private HttpHeaders makeAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        if (token != null) {
            headers.setBearerAuth(token);
        } else {
            throw new IllegalStateException("Cannot make a call without a token");
        }
        return headers;
    }
}
