plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(project(":modules:core:dopamine-core"))
    implementation(libs.spring.boot.starter.web)

    api(libs.springdoc.openapi.webmvc.ui)
    api(libs.springdoc.openapi.common)
}
