plugins {
    java
    id("java-convention")
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}