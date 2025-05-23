plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(project(":modules:core:dopamine-core"))
    implementation(libs.spring.boot.starter.test)
}
