plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.spring.boot.starter.web)

    testApi(project(":modules:test:dopamine-test-support"))
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.framework.engine)
    testImplementation(libs.kotest.extensions.spring)
}
