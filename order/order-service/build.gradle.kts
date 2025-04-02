plugins {
    java
    id("java-convention")
    id("web-convention")
    id("r2dbc-postgres-convention")
    id("rabbitmq-convention")
    id("shedlock-convention")
    id("reactive-redis-convention")
    id("springcloud-convention")
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
    api(project(":order-domain"))
    api(project(":event-outbox-core"))
}