package com.diploma.order_service.requests;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.diploma.order_service.exceptions.BusinessException;
import com.diploma.order_service.models.product.BuyRequest;
import com.diploma.order_service.models.product.BuyResponse;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductClient {
    private final RestTemplate restTemplate;

    @Value("${application.config.product-url}") 
    private String productUrl;

    public List<BuyResponse> buyProducts(List<BuyRequest> request) {
        // dev_note: also it possible to pass a api key through headers 
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<List<BuyRequest>> requestEntity = new HttpEntity<>(request, headers);
        ParameterizedTypeReference<List<BuyResponse>> responseType = new ParameterizedTypeReference<List<BuyResponse>>() { };
        ResponseEntity<List<BuyResponse>> responseEntity = restTemplate.exchange(productUrl + "/buy", HttpMethod.POST, requestEntity, responseType);
        
        if(responseEntity.getStatusCode().isError()) {
            throw new BusinessException("error while processing the proucts to buy: " + responseEntity.getStatusCode());
        }
        return responseEntity.getBody();
    }


    
    

}
