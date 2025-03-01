plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly("org.springframework.boot:spring-boot-gradle-plugin:3.4.3")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")
    compileOnly("io.spring.gradle:dependency-management-plugin:1.1.7")

//    implementation("org.jetbrains.kotlin:kotlin-allopen:1.9.23")
//    implementation("org.jetbrains.kotlin:kotlin-noarg:1.9.23")
}