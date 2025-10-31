package de.bayer.pharmacy.inventoryservice.application.handler;

import de.bayer.pharmacy.inventoryservice.application.command.PublishProductCommand;
import de.bayer.pharmacy.inventoryservice.domain.exception.ProductException;
import de.bayer.pharmacy.inventoryservice.domain.exception.ProductNotFoundException;
import de.bayer.pharmacy.inventoryservice.domain.model.Product;
import de.bayer.pharmacy.inventoryservice.infrastructure.repository.ProductRepository;
import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.BeforeEach;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublishProductCommandHandlerTest {

    private ProductRepository productRepository;
    private PublishProductCommandHandler handler;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        handler = new PublishProductCommandHandler(productRepository);
    }

    @Test
    void shouldApproveAndSaveProductWhenPublishable() {
        // given
        String sku = "SKU-123";
        PublishProductCommand command = new PublishProductCommand(sku);

        Product product = mock(Product.class);

        when(productRepository.findBySku(sku)).thenReturn(Optional.of(product));
        when(product.canBePublished()).thenReturn(true);

        // when
        handler.handle(command);

        // then
        verify(product).approve();
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowProductExceptionWhenProductCannotBePublished() {
        // given
        String sku = "SKU-456";
        PublishProductCommand command = new PublishProductCommand(sku);

        Product product = mock(Product.class);

        when(productRepository.findBySku(sku)).thenReturn(Optional.of(product));

        when(product.canBePublished()).thenReturn(false);
        when(product.getSku()).thenReturn(sku);

        // when / then
        ProductException ex = assertThrows(ProductException.class, () -> handler.handle(command));

        assertTrue(ex.getMessage().contains(sku));

        verify(product, never()).approve();
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowNotFoundWhenProductDoesNotExist() {
        // given
        String sku = "SKU-999";
        PublishProductCommand command = new PublishProductCommand(sku);

        when(productRepository.findBySku(sku)).thenReturn(Optional.empty());

        // when / then
        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class,
                () -> handler.handle(command));

        assertTrue(ex.getMessage().contains(sku));
        verify(productRepository, never()).save(any());
    }
}