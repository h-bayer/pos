package de.bayer.pharmacy.clraiproducts.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ProductCreationService {
    private final RestClient client;

    public ProductCreationService(RestClient client) {
        this.client = client;
    }


}
