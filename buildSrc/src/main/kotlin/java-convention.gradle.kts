plugins {
    java
}

val lombokVersion by extra("1.18.36")

dependencies {
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
    implementation("io.projectreactor:reactor-core")


    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    testCompileOnly("org.projectlombok:lombok:${lombokVersion}")
    testAnnotationProcessor("org.projectlombok:lombok:${lombokVersion}")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.1.10")
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-jakarta-validation:1.1.10")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}
