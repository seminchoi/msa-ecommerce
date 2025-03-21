plugins {
    java
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-rabbit")
    implementation("org.springframework.boot:spring-boot-starter-amqp")

    testImplementation("org.springframework.amqp:spring-rabbit-test")
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-binder")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:rabbitmq")
}