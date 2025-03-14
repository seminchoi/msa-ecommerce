plugins {
    `java-library`
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}


tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = false
    }
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = "com.sem"
    version = "0.0.1-SNAPSHOT"
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    val springCloudVersion = "2024.0.0"

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}")
        }
    }
}
