package de.bayer.pharmacy.inventoryservice.api.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bayer.pharmacy.inventoryservice.api.dto.CreateWarehouseRequest;
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
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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



    @BeforeAll
    void setUp() {
        assertNotNull(warehouseRepository);

    }

    @Test
    void shouldCreateWarehouseSuccessfully() throws Exception {



        String warehouseCode="WH-10";
        String address = "Berlin";

        // given
        Warehouse warehouse = new Warehouse(warehouseCode, address);
        warehouse
                .addNewStorageLocation("SL-1", 10)
                .addNewStorageLocation("SL-2", 20);

        //reflection to be used as id not accessible
        ReflectionTestUtils.setField(warehouse, "id", 42L);


        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);

        var request = new CreateWarehouseRequest(warehouseCode, address);



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