plugins {
    `java-library`
    id("org.springframework.boot") version "3.4.3" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = "com.sem"
    version = "0.0.1-SNAPSHOT"
}

subprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
