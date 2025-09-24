package de.bayer.pharmacy.productservice.product.exceptions;

public class DuplicateSkuException extends RuntimeException {
    public DuplicateSkuException(String sku) { super("SKU already exists: " + sku); }
    public DuplicateSkuException(String sku, Throwable cause) { super("SKU already exists: " + sku, cause); }
}