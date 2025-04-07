package com.sem.ecormmerce.core.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DomainEventArchive {
    private List<DomainEvent> events = new ArrayList<>();

    public void register(DomainEvent event) {
        events.add(event);
    }

    public List<DomainEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public void clearEvents() {
        events.clear();
    }
}
