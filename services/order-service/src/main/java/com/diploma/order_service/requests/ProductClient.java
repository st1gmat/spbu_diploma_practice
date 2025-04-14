package com.diploma.order_service.requests;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.diploma.order_service.exceptions.BusinessException;
import com.diploma.order_service.models.product.BuyRequest;
import com.diploma.order_service.models.product.BuyResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductClient {
    private final RestTemplate restTemplate;

    @Value("${application.config.product-url}") 
    private String productUrl;

    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000, multiplier = 1.5, maxDelay = 4000)
    )
    public List<BuyResponse> buyProducts(List<BuyRequest> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        log.info("Try!!!");
        HttpEntity<List<BuyRequest>> requestEntity = new HttpEntity<>(request, headers);
        ParameterizedTypeReference<List<BuyResponse>> responseType = new ParameterizedTypeReference<List<BuyResponse>>() {};
        
        ResponseEntity<List<BuyResponse>> responseEntity = restTemplate.exchange(
            productUrl + "/buy",
            HttpMethod.POST,
            requestEntity,
            responseType
        );

        if (responseEntity.getStatusCode().isError()) {
            throw new BusinessException("error while processing the products to buy: " + responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }

    @Recover
    public void recover(Exception e, List<BuyRequest> request) {
        System.out.println("[Fallback] Product purchase failed after retries.");
        // throw new BusinessException("Product service unavailable. Fallback after retries.", e);
    }
}    