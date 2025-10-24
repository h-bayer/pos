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

    private StorageLocation loc1;

    private StorageLocation loc2;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();

         loc1 = new StorageLocation(10); // capacity 10
         loc2 = new StorageLocation(5);  // capacity 5

        warehouse.addStorageLocation(loc1);
        warehouse.addStorageLocation(loc2);

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
        loc1.store(product, 5);

        var result = warehouse.store(product, 5);

        assertEquals(0, result.getRemainingCount());
        assertEquals(10, loc1.getStoredQuantity(product)); // filled up
    }

    @Test
    void shouldFillUpLocationAndReturnRemainingWhenNotEnoughFreeSpaceLeft() {

        product = new Product("12345-12", "TestProduct");
        product.setStatus(ProductStatus.APPROVED);

        loc1.store(product, 10); // full

        var result = warehouse.store(product, 7);

        assertEquals(2, result.getRemainingCount()); // loc2 can take 5 only
        assertEquals(5, loc2.getStoredQuantity(product));
    }
}