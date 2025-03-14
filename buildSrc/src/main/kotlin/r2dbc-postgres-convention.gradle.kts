plugins {
    java
}

dependencies {
    implementation("org.postgresql:r2dbc-postgresql")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

    testImplementation("org.testcontainers:r2dbc")
    testImplementation("org.testcontainers:postgresql")
}