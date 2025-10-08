package de.bayer.pharmacy.inventoryservice.productevents;


import com.example.avro.ProductPublished;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Component;

@Component
public class ProductEventsConsumer {
    //private final InventoryProductRepo repo;
   //public ProductEventsConsumer(InventoryProductRepo repo) { this.repo = repo; }
    private static final Logger log = LoggerFactory.getLogger(ProductEventsConsumer.class);

    @KafkaListener(topics = "${app.topics.productEvents}")
    public void onProductPublished(ConsumerRecord<String, ProductPublished> rec) {
        try {
            var evt = rec.value();
            log.debug(evt.toString());
//            InventoryProduct ip = repo.findById(evt.getProductId()).orElseGet(InventoryProduct::new);
//            ip.setProductId(evt.getProductId());
//            ip.setSku(evt.getSku());
//            ip.setName(evt.getName());
//            repo.save(ip);
            //ack.acknowledge();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


//    @KafkaListener(topics = "${app.topics.productEvents}")
//    public void onProductDeleted(ConsumerRecord<String, ProductDeleted> rec, Acknowledgment ack) {
//        try {
//            var evt = rec.value();
//            repo.deleteById(evt.getProductId());
//            ack.acknowledge();
//        } catch (Exception e) { ack.acknowledge(); }
//    }
}