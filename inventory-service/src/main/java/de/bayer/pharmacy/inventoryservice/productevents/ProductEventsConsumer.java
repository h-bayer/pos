package de.bayer.pharmacy.inventoryservice.productevents;

import com.example.avro.ProductApproved;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class ProductEventsConsumer {
    //private final InventoryProductRepo repo;
   //public ProductEventsConsumer(InventoryProductRepo repo) { this.repo = repo; }


    @KafkaListener(topics = "${app.topics.productEvents}")
    public void onProductApproved(ConsumerRecord<String, ProductApproved> rec, Acknowledgment ack) {
        try {
            var evt = rec.value();
//            InventoryProduct ip = repo.findById(evt.getProductId()).orElseGet(InventoryProduct::new);
//            ip.setProductId(evt.getProductId());
//            ip.setSku(evt.getSku());
//            ip.setName(evt.getName());
//            repo.save(ip);
            ack.acknowledge();
        } catch (Exception e) { ack.acknowledge(); }
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