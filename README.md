# 이벤트 기반 주문-결제 안전하게 구현하기

주문 및 결제 서버를 분리하고 이벤트로 주문-결제 프로세스를 구현하였습니다.


- 장애 전파 방지 - 주문 서비스가 동작한다면 결제 서비스에서 일시적으로 장애가 발생하더라도 복구가 되었을 때 주문 및 결제가 성공합니다.
- 확장 가능성 - 주문 이벤트를 발행하기만 하면 컨슈머가 자유롭게 이벤트를 소비하고, 컨슈머를 확장할 수 있어 기능 확장에 유리합니다.

위 이점을 최대한 활용할 수 있도록 고려하며 이벤트 기반으로 주문 결제 기능을 구현하였습니다.

## 주문 도메인 이벤트 발행 구현 상세
이벤트 발행 기능 설계에서도 기술과 비즈니스 관심사를 분리하기 위한 설계를 진행했습니다.
주문-결제 이벤트 발행은 Transaction Outbox 패턴과 RabbitMQ를 사용하지만, 애플리케이션 계층은 도메인 이벤트 인터페이스에만 의존하여 Transaction Outbox 패턴을 사용하지 않게 되거나 RabbitMQ가 아닌 다른 메세지 브로커를 사용하게 되더라도 변경이 전파되지 않도록 설계했습니다.

**도메인 계층 클래스 다이어그램**

![](https://velog.velcdn.com/images/just/post/884e409c-b5ae-4e1f-8e66-1bc6a40b92bd/image.png)


도메인 계층에서는 이벤트 관심사를 위와 같이 구현하였습니다.

- `DomainEvent`를 인터페이스로 정의하였습니다.
- 실제 도메인 이벤트(ex-`OrderCreatedEvent`)는 `DomainEvent`를 구현합니다.
- 도메인 객체는 이벤트 발생시 `DomainEvent` 인스턴스를 생성하고, `DomainEventArchive`에 기록합니다.
    - `DomainEventArchive`는 `DomainEvent` 들을 `List`로 관리합니다.
    - 상속 대신 Composition을 선택하여 도메인 객체의 확장성을 높이고 Lombok 어노테이션을 활용해 코드 중복을 최소화 했습니다.
  ```java
  public class Order {
    private UUID id;
    // 기타 도메인 속성 필드들
    ...
    
    // Composition으로 도메인 객체 확장 가능성을 열어둠
    // Lombok의 @Delegate를 사용하여 중복 코드 작성 최소화
    @Delegate
    @Builder.Default
    private DomainEventArchive archive = new DomainEventArchive();    
  }
  ```

**인프라 계층 클래스 다이어그램**
![](https://velog.velcdn.com/images/just/post/0a46a76e-7947-463b-9e79-75e3d84b6475/image.png)

인프라 계층에서는 위와 같이 Transaction Outbox 패턴을 구현하였습니다.

- `OutboxEventPersister`가 `DomainEvent`를 `OutboxEvent`로 변환하고 DB에 저장합니다.
- `OutboxEventPublisher`는 다음 단계들을 걸쳐 이벤트를 처리합니다.
    1. Redis에서 ACK 처리된 이벤트 목록을 조회하고, DB에 해당 이벤트들이 전송 완료되었다는 정보를 업데이트 합니다.
    2. DB에서 publish되지 않은 (=ACK처리 되지 않은) 이벤트들을 조회하여 publish합니다.
    3. publish 된 이벤트들은 RabbitTemplate Publish Confirm 설정에 의해 ACK응답이 온 이벤트들을 Redis에 캐싱합니다.
    4. 위 과정을 반복하며 이벤트가 최소 한 번 전송되는 것을 보장합니다.
- `OutboxEventScheduler`를 등록하여 위 작업이 짧은 주기로 실행될 수 있도록 합니다.
    - `OutboxEventScheduler` **ShedLock**을 적용하여 Scale Out시에도 하나의 서버가 Transaction Outbox 스케줄링을 수행하도록 하였습니다.

이처럼 Write Back 전략으로 이벤트 발행과 ACK를 처리하여 이벤트 발행이 시스템 리소스를 최소한으로 사용할 수 있도록 하면서도 최소 한 번 전송을 보장할 수 있도록 구현하였습니다.




**애플리케이션 서비스 구현**
위 구조를 통해 애플리케이션 계층에서는 인프라 계층의 기술 구현을 이해하지 않으면서도 비즈니스 로직 흐름을 명확히 표현할 수 있게 되었습니다.
```java
@Service
@RequiredArgsConstructor
public class OrderService {
    // 도메인 계층의 인터페이스들에만 의존
    private final OrderRepositoryPort orderRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Mono<UUID> createOrder(CreateOrderCommand command) {
        Order order = createOrderFromCommand(command);
        
        return orderRepository.save(order)
                .then(eventPublisher.publishAll(order.getEvents()))
                .then(Mono.fromRunnable(order::clearEvents))
                .then(Mono.fromCallable(order::getId));
    }
    
    ...
}
```
`OrderService` 의 코드 예시를 보면 인프라 계층의 구현체로부터 완벽히 관심사가 분리되어 단순히 Order 도메인 객체를 생성하고 이벤트를 발행하는 객체간의 메세지만 전달하여 비즈니스 로직에 집중할 수 있습니다.

## 결제 서비스에서의 이벤트 소비
### 멱등성을 보장하는 이벤트 소비
결제 서비스에서는 이벤트를 멱등성 있게 소비할 수 있도록 구현했습니다.

**EventConsumer 구현**

```java
@Bean
public Function<Flux<Message<String>>, Flux<Void>> orderCreatedConsumer() {
    return flux -> flux
            .flatMap(this::consumeEvent)
            .onErrorContinue((error, obj) -> {
                log.error("Error processing order payment: {}", error.getMessage(), error);
            });
}

@Transactional
private Mono<Void> consumeEvent(Message<String> message) {
    OrderCreatedEvent event = MapperUtils.fromJson(message.getPayload(), OrderCreatedEvent.class);

    return processedEventRepository.findById(event.eventId())
            .hasElement()
            .flatMap(exists -> {
                if (exists) {
                    return Mono.empty();
                }
                return processedEventRepository.save(new ProcessedEvent(event.eventId()))
                        .then(paymentService.processPayment(createPayment(event)))
                        .then();
            });
}
```

**PaymentService 구현**
```java
@Transactional
public Mono<Void> processPayment(Payment payment) {
    return paymentMethodRepository.findById(payment.getPaymentMethodId())
            .flatMap(paymentMethod -> paymentClient.processPayment(payment, paymentMethod))
            .then(paymentRepository.save(payment))
            .then(eventPublisher.publishAll(payment.getEvents()))
            .then(Mono.fromRunnable(payment::clearEvents))
            .then();
}
```
- eventId값을 통해 이벤트가 이전에 처리되지 않았는지 확인합니다.
- 이벤트가 처리되었음을 우선 마킹하고 이후 트랜잭션 처리를 수행합니다.
- 도메인에서 제공하는 Service 인터페이스를 통해 결제 처리를 위임하고, Application Service에서 비즈니스 로직을 집중해서 수행할 수 있도록 합니다.


### DLQ를 활용한 이벤트 실패 처리
결제 서비스 다운으로 인해 이벤트 처리가 일정시간 이상 진행되지 않았거나, 외부 요인(PG 서버) 등의 문제로 계속해서 이벤트 처리가 실패한다면 적정선에서 결제 실패 처리를 해야합니다.

이는 결제 서비스와 메세지브로커의 부하를 줄이기 위해서 이기도 하지만 사용자 입장에서 주문 실패에 대한 대처를 할 수 있도록 하기 위함이기도 합니다.

이에 대한 대처를 위해 다음과 같은 설정을 하였습니다.
- 일시적인 오류에는 대응할 수 있도록 최대 3번 재시도할 수 있도록 설정하였습니다.
- 결제 서비스 자체가 다운된 경우를 대비하여 메세지의 TTL을 10분으로 설정하였습니다.
    - 주문 생성 Outbox 이벤트의 TTL이 5분이므로 재시도 및 지연을 고려하여 여유롭게 설정
- DLQ로 이동한 메세지는 다시 결제 서비스가 소비하여 결제 실패 이벤트를 생성하고 발행

# DDD, Hexagonal Architecture 설계
## 모듈 분리 및 모듈 의존 관계 방향
DDD와 Hexagonal Architecture 의 개념을 적용해 모듈을 분리하고 의존성 방향이 도메인을 향하도록 다음과 같이 설계하였습니다.
![](https://velog.velcdn.com/images/just/post/994c5e81-2740-4d4f-a238-b4352666d37a/image.png)



- **Domain module**
    - 도메인 로직에 집중하고 객체간 메세지를 통해 기능이 구현될 수 있도록 설계
    - 기술에 의존적인 코드는 기본적으로 지양
    - 단, 기술 교체 가능성과 구현 비용의 트레이드 오프를 고려하여 유연하게 적용
- **Infrastructure module**
    - 특정 기술적 관심사를 실제로 구현하는 모듈
    - 모듈 내부적으로도 책임을 분리하여 특정 관심사에 대한 기술적 구현만 하도록 하고, 비즈니스 로직의 흐름은 Service를 통해 수행하도록 책임 분리
- **Application module**
    - 도메인 모듈의 객체와 인터페이스를 이용해 애플리케이션 수준에서의 비즈니스 로직 구현
    - 외부와의 통신을 담당하는 인터페이스 구현



