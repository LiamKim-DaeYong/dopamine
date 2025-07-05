import io.dopamine.build.AutoConfigurationImportGeneratorPlugin

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)

    id("org.jreleaser") version "1.19.0"
    id("signing")
}

dependencies {
    api(project(":modules:file:dopamine-file-mvc"))
    api(project(":modules:response:dopamine-response-mvc"))
    api(project(":modules:trace:dopamine-trace-mvc"))
    api(project(":modules:starter:dopamine-starter-common"))

    implementation(libs.spring.boot.starter.web)
    testImplementation(libs.spring.boot.starter.test)
}

apply<AutoConfigurationImportGeneratorPlugin>()

signing {
    useGpgCmd()
}

jreleaser {
    gitRootSearch.set(true)

    project {
        name.set("dopamine-starter-mvc")
        description.set("Spring Boot starter for shared infrastructure like response, traceId, i18n, etc")
        authors.set(listOf("DaeYong Kim"))
        license.set("MIT")
        inceptionYear.set("2025")
    }
}
