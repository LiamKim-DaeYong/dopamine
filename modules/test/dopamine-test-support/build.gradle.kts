plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(project(":modules:response:dopamine-response-common"))
    implementation(project(":modules:trace:dopamine-trace-common"))

    implementation(libs.spring.boot.starter.test)
    implementation(libs.kotest.assertions.core)
    implementation(libs.kotest.framework.engine)
    implementation(libs.kotest.extensions.spring)
}
