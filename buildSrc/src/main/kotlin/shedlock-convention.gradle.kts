import gradle.kotlin.dsl.accessors._2c95f20277cbe6143532f6e8d67e36cc.implementation
import gradle.kotlin.dsl.accessors._2c95f20277cbe6143532f6e8d67e36cc.java
import gradle.kotlin.dsl.accessors._2c95f20277cbe6143532f6e8d67e36cc.testImplementation
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.java

plugins {
    java
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    implementation("net.javacrumbs.shedlock:shedlock-spring:6.3.0")
    implementation("net.javacrumbs.shedlock:shedlock-provider-redis-spring:6.3.0")
}