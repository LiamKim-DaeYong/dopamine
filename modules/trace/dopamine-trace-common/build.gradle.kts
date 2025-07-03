plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    api(project(":modules:core:dopamine-core"))
    api(project(":modules:generator:dopamine-id-generator"))
    implementation(libs.spring.boot.starter.web)
}
