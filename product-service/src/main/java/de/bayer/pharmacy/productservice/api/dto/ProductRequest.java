package de.bayer.pharmacy.productservice.api.dto;

import jakarta.validation.constraints.NotNull;

public record ProductRequest(
        @NotNull String sku

) { }