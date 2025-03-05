import gradle.kotlin.dsl.accessors._2c95f20277cbe6143532f6e8d67e36cc.java

plugins {
    java
}

apply {
    plugin("org.springframework.boot")
    plugin("io.spring.dependency-management")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}