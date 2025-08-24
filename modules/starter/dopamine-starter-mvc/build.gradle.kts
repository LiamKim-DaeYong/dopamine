import io.dopamine.build.convention.ModuleConvention
import io.dopamine.build.plugins.AutoConfigurationImportCollectorPlugin
import io.dopamine.build.plugins.AutoConfigurationImportWriterPlugin

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.shadow)
    id("maven-publish")
    id("signing")
}

version = ModuleConvention.VERSION

dependencies {
    api(project(":modules:file:dopamine-file-mvc"))
    api(project(":modules:response:dopamine-response-mvc"))
    api(project(":modules:trace:dopamine-trace-mvc"))
    api(project(":modules:starter:dopamine-starter-common"))

    implementation(libs.spring.boot.starter.web)
    testImplementation(libs.spring.boot.starter.test)
}

tasks {
    val jvmJar by registering(Jar::class) {
        dependsOn("classes")
        archiveClassifier.set("jvm")
        from(sourceSets["main"].output)
    }

    shadowJar {
        archiveClassifier.set("")
        dependsOn(jvmJar)

        from(jvmJar)
        from(sourceSets["main"].output)

        from("src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports") {
            into("META-INF/spring")
        }

        from(layout.buildDirectory.dir("generated/sources/annotationProcessor/java/main")) {
            include("META-INF/spring-configuration-metadata.json")
            into("META-INF")
        }

        mergeServiceFiles {
            include("META-INF/spring.factories")
        }
    }

    assemble {
        dependsOn(shadowJar)
    }

    val sourcesJar by registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    val javadocTask = named<Javadoc>("javadoc")

    val javadocJar by registering(Jar::class) {
        archiveClassifier.set("javadoc")
        from(javadocTask.map { it.outputs.files })
    }

    withType<Jar>().configureEach {
        if (archiveClassifier.getOrElse("") !in listOf("sources", "javadoc", "")) {
            enabled = false
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = ModuleConvention.GROUP
            artifactId = "dopamine-starter-mvc"
            version = project.version.toString()

            artifact(tasks.named("shadowJar").get()) {
                classifier = null
            }

            artifact(tasks.named("sourcesJar").get())
            artifact(tasks.named("javadocJar").get())

            pom {
                name.set("dopamine-starter-mvc")
                description.set("Spring Boot starter for shared infrastructure")
                url.set("https://github.com/LiamKim-DaeYong/dopamine")
                inceptionYear.set("2025")

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
                    connection.set("scm:git:git://github.com/LiamKim-DaeYong/dopamine.git")
                    developerConnection.set("scm:git:ssh://git@github.com/LiamKim-DaeYong/dopamine.git")
                    url.set("https://github.com/LiamKim-DaeYong/dopamine")
                }
            }

            pom.withXml {
                val root = asNode()
                val dependenciesNode = root.get("dependencies") as? MutableList<*>
                dependenciesNode?.clear()
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = findProperty("mavenCentralUsername") as String?
                    ?: error("mavenCentralUsername property is required for publishing")
                password = findProperty("mavenCentralPassword") as String?
                    ?: error("mavenCentralPassword property is required for publishing")
            }
        }
    }
}

if (project.hasProperty("mavenCentral")) {
    signing {
        sign(publishing.publications["maven"])
    }
}

apply<AutoConfigurationImportCollectorPlugin>()
apply<AutoConfigurationImportWriterPlugin>()
