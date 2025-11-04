package de.bayer.pharmacy.common.domain;

import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

public class AbstractDomainEntity {

    @Transient
    private final List<Object> domainEvents = new ArrayList<>();

    protected void record(Object event) {
        domainEvents.add(event);
    }

    public List<Object> pullDomainEvents() {
        var copy = List.copyOf(domainEvents);
        domainEvents.clear();
        return copy;
    }
}
