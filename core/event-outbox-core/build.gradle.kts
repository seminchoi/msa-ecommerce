plugins {
    id("java-convention")
    id("rabbitmq-convention")
    id("reactive-redis-convention")
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    api(project(":event-core"))
    api(project(":mapper-core"))
}