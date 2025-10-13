package de.bayer.pharmacy.common.outbox;

public interface IMessageBus {

    void publish(String type, String payload);

}
