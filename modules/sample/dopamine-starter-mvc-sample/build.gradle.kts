plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(project(":modules:starter:dopamine-starter-mvc"))

    implementation(libs.spring.boot.starter.web)
    testImplementation(libs.spring.boot.starter.test)
}
