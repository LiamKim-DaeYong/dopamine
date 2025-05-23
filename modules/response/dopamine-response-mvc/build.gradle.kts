plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    api(project(":modules:response:dopamine-response-core"))
    implementation(libs.spring.boot.starter.web)
    testImplementation(libs.spring.boot.starter.test)
}
