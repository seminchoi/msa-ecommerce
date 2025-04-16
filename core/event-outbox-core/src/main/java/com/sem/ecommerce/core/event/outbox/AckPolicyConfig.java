//package com.sem.ecommerce.core.event.outbox;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.Message;
//
//import java.util.function.Consumer;
//
//@Configuration
//public class AckPolicyConfig {
//    @Bean
//    public Consumer<Message<String>> confirmAcks() {
//        return message -> {
//            // ACK 메시지 처리
//            Boolean acked = message.getHeaders().get(RabbitHeaders.PUBLISH_CONFIRM, Boolean.class);
//            String correlationId = message.getHeaders().get(RabbitHeaders.PUBLISH_CONFIRM_CORRELATION, String.class);
//
//            if (acked != null && acked && correlationId != null) {
//                log.info("Received ACK for message with correlation ID: {}", correlationId);
//                // UUID로 변환하여 캐시에 저장
//                outBoxCache.addAckedOutboxEvent(correlationId)
//                        .subscribe();
//            } else {
//                log.warn("Received NACK for message with correlation ID: {}", correlationId);
//            }
//        };
//    }
//}
