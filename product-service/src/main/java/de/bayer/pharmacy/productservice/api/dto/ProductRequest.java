package de.bayer.pharmacy.productservice.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record ProductRequest(
        @NotNull Long sku

) { }