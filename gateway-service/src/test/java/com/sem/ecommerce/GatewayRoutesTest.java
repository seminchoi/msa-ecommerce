package com.sem.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.cloud.gateway.routes[0].id=order-service",
                "spring.cloud.gateway.routes[0].uri=http://localhost:${wiremock.server.port}",
                "spring.cloud.gateway.routes[0].predicates[0]=Path=/order-service/**",
                "spring.cloud.gateway.routes[0].filters[0]=StripPrefix=1",
                "spring.cloud.gateway.routes[1].id=product-service",
                "spring.cloud.gateway.routes[1].uri=http://localhost:${wiremock.server.port}",
                "spring.cloud.gateway.routes[1].predicates[0]=Path=/product-service/**",
                "spring.cloud.gateway.routes[1].filters[0]=StripPrefix=1"
        }
)
@AutoConfigureWireMock(port = 0)
public class GatewayRoutesTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testOrderServiceRoute() {
        // WireMock 스텁 설정
        stubFor(get("/orders/1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"orderId\":1,\"status\":\"COMPLETED\"}")
                )
        );

        // 게이트웨이를 통한 요청 테스트
        webTestClient.get()
                .uri("/order-service/orders/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.orderId").isEqualTo(1)
                .jsonPath("$.status").isEqualTo("COMPLETED");

        // WireMock 검증
        verify(getRequestedFor(urlEqualTo("/orders/1")));
    }

    @Test
    public void testProductServiceRoute() {
        // WireMock 스텁 설정
        stubFor(get("/products/2")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"productId\":2,\"name\":\"Test Product\"}")
                )
        );

        // 게이트웨이를 통한 요청 테스트
        webTestClient.get()
                .uri("/product-service/products/2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.productId").isEqualTo(2)
                .jsonPath("$.name").isEqualTo("Test Product");

        // WireMock 검증
        verify(getRequestedFor(urlEqualTo("/products/2")));
    }
}