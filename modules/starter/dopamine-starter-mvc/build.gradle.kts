import io.dopamine.build.AutoConfigurationImportGeneratorPlugin

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    api(project(":modules:response:dopamine-response-mvc"))
    api(project(":modules:starter:dopamine-starter-common"))

    implementation(libs.spring.boot.starter.web)
    testImplementation(libs.spring.boot.starter.test)
}

apply<AutoConfigurationImportGeneratorPlugin>()
