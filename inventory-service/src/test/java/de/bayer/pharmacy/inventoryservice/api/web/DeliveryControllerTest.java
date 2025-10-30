package de.bayer.pharmacy.inventoryservice.api.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bayer.pharmacy.inventoryservice.api.dto.StoreProductRequest;
import de.bayer.pharmacy.inventoryservice.domain.model.Product;
import de.bayer.pharmacy.inventoryservice.domain.model.ProductStatus;
import de.bayer.pharmacy.inventoryservice.domain.model.ProductType;
import de.bayer.pharmacy.inventoryservice.domain.model.Warehouse;
import de.bayer.pharmacy.inventoryservice.infrastructure.repository.ProductRepository;
import de.bayer.pharmacy.inventoryservice.infrastructure.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = de.bayer.pharmacy.inventoryservice.app.InventoryServiceApplication.class)
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //for one object for all test methods. BeforeAll can be non static, context loading quicker
@AutoConfigureMockMvc
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // converts objects to JSON

    @MockitoBean
    private WarehouseRepository warehouseRepository;

    @MockitoBean
    private ProductRepository productRepository;

    @BeforeAll
    void setUp() {
        assertNotNull(warehouseRepository);
        assertNotNull(productRepository);
    }

    @Test
    void shouldStoreProductSuccessfully() throws Exception {
        var request = new StoreProductRequest("SKU-123", 5);

        // given
        var warehouse = new Warehouse("WH-01", "Central Warehouse");

        warehouse
                .addNewStorageLocation("SL-1", 10)
                .addNewStorageLocation("SL-2", 20);


        var product = new Product()
                .setSku("SKU-123")
                .setStatus(ProductStatus.APPROVED)
                .setName("Product Name")
                .setType(ProductType.STANDARD);

        // repository behavior
        when(warehouseRepository.findByCode("WH-01")).thenReturn(Optional.of(warehouse));
        when(productRepository.findBySku("SKU-123")).thenReturn(Optional.of(product));

        mockMvc.perform(post("/api/v1/delivery/WH-01/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("✅ Product stored successfully"));
    }

    @Test
    void shouldReturnNotFoundForInvalidWarehouse() throws Exception {
        var productSku = "SKU-123";

        var request = new StoreProductRequest(productSku, 5);

        mockMvc.perform(post("/api/v1/delivery/WH-01/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotProcessableForInvalidProduct() throws Exception {

        var productSku = "SKU-123";
        var warehouseCode = "WH-01";

        var request = new StoreProductRequest(productSku, 5);

        // given
        var warehouse = new Warehouse(warehouseCode, "Central Warehouse");

        warehouse
                .addNewStorageLocation("SL-1", 10)
                .addNewStorageLocation("SL-2", 20);


        var product = new Product()
                .setSku(productSku)
                .setStatus(ProductStatus.CREATED)
                .setName("Product Name")
                .setType(ProductType.STANDARD);

        // repository behavior
        when(warehouseRepository.findByCode(warehouseCode)).thenReturn(Optional.of(warehouse));
        when(productRepository.findBySku(productSku)).thenReturn(Optional.of(product));


        mockMvc.perform(post("/api/v1/delivery/WH-01/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }
}