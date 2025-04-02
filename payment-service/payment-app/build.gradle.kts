plugins {
    id("java-convention")
    id("web-convention")
}


tasks {
    bootJar {
        enabled = true
    }

    jar {
        enabled = true
    }
}