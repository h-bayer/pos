package de.bayer.pharmacy.productservice.infra.outbox;

public interface IMessageBus {

    void publish(String type, String payload);

}
