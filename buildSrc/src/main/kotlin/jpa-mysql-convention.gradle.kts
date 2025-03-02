plugins {
    java
}

apply {
    plugin("org.springframework.boot")
    plugin("io.spring.dependency-management")
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("com.h2database:h2")
}