plugins {
    id("java-convention")
    id("r2dbc-postgres-convention")
    id("eureka-convention")
}

tasks {
    bootJar {
        enabled = true
    }

    jar {
        enabled = true
    }
}

dependencies {
    api(project(":event-core"))
    api(project(":mapper-core"))
    api(project(":order-domain"))
    api(project(":order-infra"))
}