plugins {
    id("java-convention")
    id("web-convention")
    id("eureka-convention")
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
}