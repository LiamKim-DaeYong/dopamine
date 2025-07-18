plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    api(project(":modules:core:dopamine-core"))
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.data.commons)
    implementation(libs.kotlin.reflect)

    testApi(project(":modules:test:dopamine-test-support"))
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.framework.engine)
    testImplementation(libs.kotest.extensions.spring)
}
