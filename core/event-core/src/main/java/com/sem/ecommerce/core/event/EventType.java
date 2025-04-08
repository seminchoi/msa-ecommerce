package com.sem.ecommerce.core.event;

import java.util.Arrays;
import java.util.List;

/**
 * 이벤트 타입을 표현하는 인터페이스
 * 모든 이벤트 타입 enum은 이 인터페이스를 구현해야 함
 */
public interface EventType {
    /**
     * 이벤트 타입 문자열 반환 (예: "order.created")
     */
    String getEventKey();

    /**
     * 출력 바인딩 채널 반환 (예: "orderProcessor-out-0")
     */
    String getOutputBinding();

    /**
     * 이벤트 타입 문자열로부터 해당하는 enum 상수를 찾아 반환
     *
     * @param eventKey 이벤트 타입 문자열 (예: "order.created")
     * @param enumClass 검색할 enum 클래스
     * @return 찾은 enum 상수, 없으면 Optional.empty()
     */
    static <T extends Enum<T> & EventType> T fromEventKey(String eventKey, Class<T> enumClass) {
        List<T> foundEvents = Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getEventKey().equals(eventKey))
                .toList();

        if(foundEvents.isEmpty()) {
            throw new IllegalArgumentException("There is no such event key: " + eventKey);
        }

        if(foundEvents.size() > 1) {
            throw new IllegalStateException("Duplicate event key detected: " + eventKey);
        }

        return foundEvents.get(0);
    }
}