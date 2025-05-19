plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(project(":modules:dopamine-core"))
    implementation(libs.spring.web)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.kotlin)

    implementation(libs.spring.boot.starter.test)
    implementation(libs.jakarta.servlet.api)
    implementation(libs.kotest.runner.junit5)
    implementation(libs.kotest.assertions.core)
    implementation(libs.kotest.framework.engine)
    implementation(libs.kotest.extensions.spring)
}
