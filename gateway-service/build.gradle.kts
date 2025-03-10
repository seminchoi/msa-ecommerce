plugins {
    id("java-convention")
    id("eureka-convention")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")

    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")
}