package com.techelevator.tenmo.services;
import com.techelevator.tenmo.model.AccountDto;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

public class AccountService {
    private final String baseUrl;
    private final RestTemplate restTemplate;
    private String authToken;

    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public BigDecimal getBalance(int userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<BigDecimal> response = restTemplate.exchange(baseUrl + "account/balance", HttpMethod.GET, entity, BigDecimal.class);
        return response.getBody();

    }
    public User[] getUsers() {
        User[] users = null;
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(baseUrl + "account/user/", HttpMethod.GET, makeAuthEntity(), User[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                users = response.getBody();
            } else {
                System.out.println("Error: " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return users;
    }
    public boolean transferBalance(int fromUserId, int toUserId, BigDecimal amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "account/transfer?toUserId=" + toUserId + "&amount=" + amount,
                HttpMethod.POST,
                entity,
                Void.class);
        return response.getStatusCode() == HttpStatus.OK;
    }
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        if (authToken != null) {
            headers.setBearerAuth(authToken);
        } else {
            throw new IllegalStateException("Cannot make a call without a token");
        }
        return new HttpEntity<>(headers);
    }
}
