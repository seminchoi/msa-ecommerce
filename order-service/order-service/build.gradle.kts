plugins {
    java
    id("java-convention")
    id("web-convention")
}

dependencies {
    api(project(":order-service:order-service-domain"))
}