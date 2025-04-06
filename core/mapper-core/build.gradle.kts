plugins {
    id("java-convention")
    id("helper-convention")
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}