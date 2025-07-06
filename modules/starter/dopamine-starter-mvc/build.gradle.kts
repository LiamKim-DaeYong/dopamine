import io.dopamine.build.AutoConfigurationImportGeneratorPlugin

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    id("signing")
    `maven-publish`
}

dependencies {
    api(project(":modules:file:dopamine-file-mvc"))
    api(project(":modules:response:dopamine-response-mvc"))
    api(project(":modules:trace:dopamine-trace-mvc"))
    api(project(":modules:starter:dopamine-starter-common"))

    implementation(libs.spring.boot.starter.web)
    testImplementation(libs.spring.boot.starter.test)
}

tasks.jar {
    archiveClassifier.set("")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "io.github.liamkim-daeyong"
            artifactId = "dopamine-starter-mvc"

            pom {
                name.set("dopamine-starter-mvc")
                description.set("Spring Boot starter for shared infrastructure")
                url.set("https://github.com/LiamKim-DaeYong/dopamine")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("liamkim1018")
                        name.set("DaeYong Kim")
                        email.set("liamkim1018@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/LiamKim-DaeYong/dopamine")
                    connection.set("scm:git:git://github.com/LiamKim-DaeYong/dopamine.git")
                    developerConnection.set("scm:git:ssh://github.com:LiamKim-DaeYong/dopamine.git")
                }
            }
        }
    }

    repositories {
        maven {
            name = "Releases"
            url =
                rootProject.layout.buildDirectory
                    .dir("repos/releases")
                    .get()
                    .asFile
                    .toURI()
        }
    }
}

apply<AutoConfigurationImportGeneratorPlugin>()
