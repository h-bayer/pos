package de.bayer.pharmacy.inventoryservice.api.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bayer.pharmacy.inventoryservice.api.dto.warehouseRequest;
import de.bayer.pharmacy.inventoryservice.domain.model.Warehouse;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = de.bayer.pharmacy.inventoryservice.app.InventoryServiceApplication.class)
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //for one object for all test methods. BeforeAll can be non static, context loading quicker
@AutoConfigureMockMvc
class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // converts objects to JSON

    @MockitoBean
    private WarehouseRepository warehouseRepository;

    private Warehouse warehouse;

    String warehouseCode="WH-10";
    String address = "Berlin";

    @BeforeAll
    void setUp() {
        assertNotNull(warehouseRepository);



        // given
        warehouse = new Warehouse(warehouseCode, address);
        warehouse
                .addNewStorageLocation("SL-1", 10)
                .addNewStorageLocation("SL-2", 20);

        //reflection to be used as id not accessible
        ReflectionTestUtils.setField(warehouse, "id", 42L);
    }

    @Test
    void shouldDeleteWarehouseSuccesfully() throws Exception {
        when(warehouseRepository.findByCode(warehouseCode)).thenReturn(Optional.of(warehouse));

        var request = new warehouseRequest(warehouseCode, "");

        mockMvc.perform(delete("/api/v1/warehouse/" + warehouseCode))
                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    void shouldDeleteWarehouseWarehouseNotFound() throws Exception {
        when(warehouseRepository.findByCode(warehouseCode)).thenReturn(Optional.of(warehouse));



        mockMvc.perform(delete("/api/v1/warehouse/" + "nonexisting"))
                .andDo(print())
                .andExpect(status().isNotFound());


    }

    @Test
    void shouldCreateWarehouseSuccessfully() throws Exception {




        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);

        var request = new warehouseRequest(warehouseCode, address);



        // when + then
        mockMvc.perform(post("/api/v1/warehouse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("/api/v1/warehouse/"+warehouseCode))) // flexible match
                .andExpect(jsonPath("$.warehouseCode").value(warehouseCode));;
    }

}