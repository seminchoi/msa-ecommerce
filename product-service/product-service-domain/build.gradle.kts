plugins {
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