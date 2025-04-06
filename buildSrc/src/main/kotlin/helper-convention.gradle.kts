import gradle.kotlin.dsl.accessors._2c95f20277cbe6143532f6e8d67e36cc.implementation
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.java

plugins {
    java
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}