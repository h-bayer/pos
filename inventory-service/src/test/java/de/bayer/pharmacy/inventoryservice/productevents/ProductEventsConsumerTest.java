package de.bayer.pharmacy.inventoryservice.productevents;


import com.example.avro.ProductPublished;
import de.bayer.pharmacy.common.kafka.ProcessedEventRepository;
import de.bayer.pharmacy.inventoryservice.api.messaging.ProductEventsConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = de.bayer.pharmacy.inventoryservice.app.InventoryServiceApplication.class)
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //for one object for all test methods. BeforeAll can be non static, context loading quicker
@AutoConfigureMockMvc
class ProductEventsConsumerTest {

    @MockitoBean
    private ProcessedEventRepository processedEventRepository;

    @Autowired
    private ProductEventsConsumer productEventsConsumer;

    private static final Logger log = LoggerFactory.getLogger(ProductEventsConsumerTest.class);

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveEventWhenNotProcessedBefore() {
        // given
        var eventId = "evt-123";
        var topic = "product-events";
        var partition = 0;
        var offset = 5L;

        var event = new ProductPublished("SKU-123", "Test Product", "1");
        var record = new ConsumerRecord<>(topic, partition, offset, "key1", event);

        when(processedEventRepository.existsByEventId(eventId)).thenReturn(false);

        // when
        productEventsConsumer.onProductPublished(eventId, record);

        // then
        verify(processedEventRepository).save(argThat(saved ->
                saved.getEventId().equals(eventId)
                        && saved.getTopic().equals(topic)
                        && saved.getPartition() == partition
                        && saved.getOffset() == offset
        ));
    }

    @Test
    void shouldSkipWhenEventAlreadyProcessed() {
        // given
        var eventId = "evt-123";
        var event = new ProductPublished("SKU-123", "Test Product", "1");
        var record = new ConsumerRecord<>("product-events", 0, 5L, "key1", event);

        when(processedEventRepository.existsByEventId(eventId)).thenReturn(true);

        // when
        productEventsConsumer.onProductPublished(eventId, record);

        // then
        verify(processedEventRepository, never()).save(any());
    }

    @Test
    void shouldCatchAndLogException() {
        // given
        var eventId = "evt-123";
        var event = new ProductPublished("SKU-123", "Test Product", "1");
        var record = new ConsumerRecord<>("product-events", 0, 5L, "key1", event);

        when(processedEventRepository.existsByEventId(eventId)).thenThrow(new RuntimeException("DB down"));

        // when
        productEventsConsumer.onProductPublished(eventId, record);

        // then
        verify(processedEventRepository, never()).save(any());
        // The log is written but not thrown — no exception expected
    }


}