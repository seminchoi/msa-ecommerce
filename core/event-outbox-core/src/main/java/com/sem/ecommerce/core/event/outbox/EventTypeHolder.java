package com.sem.ecommerce.core.event.outbox;

import java.util.HashMap;
import java.util.Map;

public class EventTypeHolder {
    private static final Map<String, EventType> EVENT_TYPE_MAP = new HashMap<>();

    private EventTypeHolder() {
        // Utility class, private constructor
    }

    public static void registerEventType(EventType eventType) {
        String eventKey = eventType.getEventKey();
        if (EVENT_TYPE_MAP.containsKey(eventKey)) {
            throw new IllegalArgumentException("Duplicate EventType key detected: " + eventKey + " for types: " +
                    EVENT_TYPE_MAP.get(eventKey).getClass().getSimpleName() + " and " + eventType.getClass().getSimpleName());
        }
        EVENT_TYPE_MAP.put(eventKey, eventType);
    }

    public static <T extends Enum<T> & EventType> void registerEventTypes(Class<T> enumClass) {
        for (T eventType : enumClass.getEnumConstants()) {
            registerEventType(eventType);
        }
    }

    public static EventType getEventType(String eventKey) {
        EventType eventType = EVENT_TYPE_MAP.get(eventKey);
        if (eventType == null) {
            throw new IllegalArgumentException("No EventType found for key: " + eventKey);
        }
        return eventType;
    }

    public static boolean containsEventType(String eventKey) {
        return EVENT_TYPE_MAP.containsKey(eventKey);
    }
}