plugins {
    id("java-convention")
    id("web-convention")
    id("r2dbc-postgres-convention")
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
    api(project(":payment-domain"))
    api(project(":payment-infra"))
}