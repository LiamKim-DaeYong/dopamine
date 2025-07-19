import com.vanniktech.maven.publish.SonatypeHost
import io.dopamine.build.AutoConfigurationImportGeneratorPlugin
import org.gradle.jvm.tasks.Jar
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.vanniktech.maven.publish)
}

version = io.dopamine.build.ModuleConvention.VERSION

dependencies {
    api(project(":modules:file:dopamine-file-mvc"))
    api(project(":modules:response:dopamine-response-mvc"))
    api(project(":modules:trace:dopamine-trace-mvc"))
    api(project(":modules:starter:dopamine-starter-common"))

    implementation(libs.spring.boot.starter.web)
    testImplementation(libs.spring.boot.starter.test)
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
}

mavenPublishing {
    coordinates(
        groupId = "io.github.liamkim-daeyong",
        artifactId = "dopamine-starter-mvc",
        version = project.version.toString(),
    )

    pom {
        name.set("dopamine-starter-mvc")
        description.set("Spring Boot starter for shared infrastructure")
        inceptionYear.set("2025")
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
            connection.set("scm:git:git://github.com/LiamKim-DaeYong/dopamine.git")
            developerConnection.set("scm:git:ssh://git@github.com/LiamKim-DaeYong/dopamine.git") // ✅ 수정됨
            url.set("https://github.com/LiamKim-DaeYong/dopamine")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}

apply<AutoConfigurationImportGeneratorPlugin>()
