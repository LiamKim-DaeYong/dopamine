plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    api(project(":modules:core:dopamine-core"))
    api(libs.springdoc.openapi.webmvc.ui)
    api(libs.springdoc.openapi.common)

    implementation(libs.spring.boot.starter.web)
}
