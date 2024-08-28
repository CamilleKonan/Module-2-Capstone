package com.techelevator.tenmo.services;
import com.techelevator.tenmo.model.TransferDto;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class TransferService {
    private final String baseUrl;
    private final RestTemplate restTemplate;
    private String authToken;

    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public List<TransferDto> getTransfersByUserId(int userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<TransferDto[]> response = restTemplate.exchange(
                baseUrl + "transfers/user/" + userId,
                HttpMethod.GET,
                entity,
                TransferDto[].class);
        return List.of(response.getBody());
    }
    public TransferDto getTransferById(int transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<TransferDto> response = restTemplate.exchange(
                baseUrl + "transfers/" + transferId,
                HttpMethod.GET,
                entity,
                TransferDto.class);
        return response.getBody();
    }
    public boolean createTransfer(TransferDto transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<TransferDto> entity = new HttpEntity<>(transfer, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "transfers",
                HttpMethod.POST,
                entity,
                Void.class);
        return response.getStatusCode() == HttpStatus.CREATED;
    }
    public boolean approveTransfer(int transferId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "transfers/approve/" + transferId,
                HttpMethod.PUT,
                entity,
                Void.class);
        return response.getStatusCode() == HttpStatus.OK;
    }
    public boolean rejectTransfer(int transferId, int rejectorId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "transfers/" + transferId + "/reject",
                HttpMethod.PUT,
                entity,
                Void.class);
        return response.getStatusCode() == HttpStatus.OK;
    }
    public List<TransferDto> getPendingTransfers(int userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<TransferDto[]> response = restTemplate.exchange(
                baseUrl + "transfers/pending/" + userId,
                HttpMethod.GET,
                entity,
                TransferDto[].class);
        return response.getBody() != null ? Arrays.asList(response.getBody()) : List.of();
    }
}



















