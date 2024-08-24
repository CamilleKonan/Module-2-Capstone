package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class  AccountService {
public static final String API_BASE_URL = "http://localhost:8080/account/";
private RestTemplate restTemplate = new RestTemplate();
private String token = null;
private AuthenticatedUser currentUser = null;

public void setCurrentUser(AuthenticatedUser currentUser) {
    this.currentUser = currentUser;
}

    public void setToken(String token) {
        this.token = token;
    }
    public BigDecimal getBalance(){
        BigDecimal balance = null;
        int userId = currentUser.getUser().getId();

        try{
            balance = restTemplate.exchange(API_BASE_URL + "/" + userId + "/" + balance, HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }
    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        if (token != null) {
            headers.setBearerAuth(token);
        } else {
            throw new IllegalStateException("Cannot make a call without a token");
        }
        return new HttpEntity<>(headers);


    }


}

