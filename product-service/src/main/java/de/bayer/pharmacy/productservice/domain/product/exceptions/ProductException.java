package de.bayer.pharmacy.productservice.domain.product.exceptions;

public class ProductException extends RuntimeException {
    public ProductException(String message) {
        super(message);
    }
}
