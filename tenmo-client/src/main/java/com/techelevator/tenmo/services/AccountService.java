package com.techelevator.tenmo.services;
import com.techelevator.tenmo.model.AccountDto;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;

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
    public AccountDto getAccount(int userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<AccountDto> response = restTemplate.exchange(baseUrl + "account", HttpMethod.GET, entity, AccountDto.class);
        return response.getBody();
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
}
