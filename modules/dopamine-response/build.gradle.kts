plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(project(":modules:dopamine-core"))
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.autoconfigure)
    implementation(libs.jackson.databind)
}
