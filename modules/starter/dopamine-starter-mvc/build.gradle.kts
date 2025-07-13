import io.dopamine.build.AutoConfigurationImportGeneratorPlugin
import org.gradle.jvm.tasks.Jar
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)

    id("org.jreleaser") version "1.19.0"
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

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    named<BootJar>("bootJar") {
        enabled = false
    }

    named<Jar>("jar") {
        enabled = true
        archiveClassifier.set("")
    }

    named("assemble") {
        dependsOn("jar")
    }

    register<Copy>("copyPublicationToStaging") {
        dependsOn("publishToMavenLocal")

        val sourceDir =
            layout.buildDirectory
                .dir("publications/mavenJava")
                .get()
                .asFile
        val targetDir =
            layout.buildDirectory
                .dir("libs")
                .get()
                .asFile

        doFirst {
            if (!sourceDir.exists()) {
                throw GradleException("Expected publication directory does not exist: $sourceDir")
            }
        }

        from(sourceDir) {
            include("*.jar")
            include("*.pom")
        }

        into(targetDir)
    }

    named("jreleaserFullRelease") {
        dependsOn("copyPublicationToStaging")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "io.github.liamkim-daeyong"
            artifactId = "dopamine-starter-mvc"
            version = project.version.toString()

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
                    connection.set("scm:git:https://github.com/LiamKim-DaeYong/dopamine.git")
                    developerConnection.set("scm:git:ssh://git@github.com:LiamKim-DaeYong/dopamine.git")
                }
            }
        }
    }
}

jreleaser {
    gitRootSearch.set(true)
}

apply<AutoConfigurationImportGeneratorPlugin>()
