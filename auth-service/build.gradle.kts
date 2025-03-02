plugins {
    id("java-convention")
    id("web-convention")
    id("jpa-mysql-convention")
}

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
}