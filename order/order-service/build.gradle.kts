plugins {
    java
    id("java-convention")
    id("web-convention")
    id("r2dbc-postgres-convention")
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
}