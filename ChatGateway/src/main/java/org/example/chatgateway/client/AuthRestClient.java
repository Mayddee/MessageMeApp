package org.example.chatgateway.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class AuthRestClient {

    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public AuthRestClient(
            RestTemplate restTemplate,
            @Value("${messageme.auth.rest.url:http://messageme:8081}") String authServiceUrl
    ) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
    }

    public List<Map<String, Object>> searchUsers(String query, String authHeader) {
        String url = authServiceUrl + "/api/v1/users/search?q=" + query;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        return response.getBody();
    }
}