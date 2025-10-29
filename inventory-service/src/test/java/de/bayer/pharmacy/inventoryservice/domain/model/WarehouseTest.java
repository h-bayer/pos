package de.bayer.pharmacy.inventoryservice.domain.model;

import de.bayer.pharmacy.inventoryservice.domain.exception.ProductStorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WarehouseTest {

    @InjectMocks
    private Warehouse warehouse;

    private Product product;


    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();

        warehouse.addNewStorageLocation("AF-12", 10);
        warehouse.addNewStorageLocation("AF-13", 5);

        product = new Product("12345-12", "TestProduct");
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsZero() {
        assertThrows(IllegalArgumentException.class,
                () -> warehouse.store(product, 0));
    }

    @Test
    void shouldThrowWhenProductNotAllowedForStorage() {
        Product notAllowed = new Product("2L", "Disallowed");

        notAllowed.setStatus(ProductStatus.CREATED);

        assertThrows(ProductStorageException.class,
                () -> warehouse.store(notAllowed, 5));

        notAllowed.setStatus(ProductStatus.RETIRED);

        assertThrows(ProductStorageException.class,
                () -> warehouse.store(notAllowed, 5));
    }

    @Test
    void shouldFillExistingStorageLocationsFirst() {
        product = new Product("12345-12", "TestProduct");
        product.setStatus(ProductStatus.APPROVED);

        // pre-fill first location with 5 units
        warehouse.store(product, 5);

        var result = warehouse.store(product, 5);

        assertEquals(0, result.getRemainingCount());
        assertEquals(10, warehouse.getStoredQuantity(product)); // filled up
    }

    @Test
    void shouldFillUpLocationAndReturnRemainingWhenNotEnoughFreeSpaceLeft() {

        product = new Product("12345-12", "TestProduct");
        product.setStatus(ProductStatus.APPROVED);

        warehouse.store(product, 10); // full

        var result = warehouse.store(product, 7);

        assertEquals(2, result.getRemainingCount());
        assertEquals(15, warehouse.getStoredQuantity(product));
    }
}