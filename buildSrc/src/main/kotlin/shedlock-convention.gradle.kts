plugins {
    java
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    implementation("net.javacrumbs.shedlock:shedlock-spring:6.3.0")
    implementation("net.javacrumbs.shedlock:shedlock-provider-redis-spring:6.3.0")
}