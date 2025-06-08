import io.dopamine.build.ModuleConvention
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    base
    jacoco
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
    alias(libs.plugins.ktlint) apply false
}

val springBootVersion = libs.versions.spring.boot.get()

allprojects {
    group = ModuleConvention.GROUP
    version = ModuleConvention.VERSION

    repositories {
        mavenCentral()
    }

    plugins.withType<JavaPlugin> {
        extensions.configure<JavaPluginExtension>("java") {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(ModuleConvention.JVM_TARGET))
            }
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.valueOf("JVM_${ModuleConvention.JVM_TARGET}"))
        }
    }
}

val ktlintCliVersion = libs.versions.ktlint.cli.get()
val jacocoExcludes = ModuleConvention.jacocoExcludes
subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")
    apply(plugin = "io.spring.dependency-management")

    // Apply Spring Boot BOM after dependency-management extension is available
    afterEvaluate {
        extensions.configure<DependencyManagementExtension> {
            imports {
                mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
            }
        }
    }

    configure<KtlintExtension> {
        version.set(ktlintCliVersion)
        android.set(false)
        outputToConsole.set(true)

        filter {
            exclude("**/build/**", "**/generated/**")
        }

        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = true
        }
        finalizedBy("jacocoTestReport")
    }

    // Automatically apply configuration processor if Spring Boot is used
    plugins.withId("org.springframework.boot") {
        dependencies.add("annotationProcessor", libs.spring.boot.configuration.processor.get())
    }

    afterEvaluate {
        tasks.matching { it.name == "jacocoTestReport" }.configureEach {
            this as JacocoReport

            group = "verification"
            description = "Configures Jacoco coverage report for individual module"

            dependsOn(tasks.named("test"))

            val classDir = layout.buildDirectory.dir("classes/kotlin/main").get().asFile
            val execDir = layout.buildDirectory.get().asFile

            classDirectories.setFrom(
                fileTree(classDir) {
                    include("**/*.class")
                    exclude(jacocoExcludes)
                }
            )
            sourceDirectories.setFrom(files("src/main/kotlin"))
            executionData.setFrom(fileTree(execDir).include("jacoco/test.exec"))

            reports {
                html.required.set(true)
                xml.required.set(true)
            }
        }

        plugins.withId("org.springframework.boot") {
            tasks.named("bootJar").configure { enabled = false }
            tasks.named("jar").configure { enabled = true }
        }
    }
}

// Aggregated Jacoco report across all modules
tasks.register<JacocoReport>("jacocoRootReport") {
    group = "verification"
    description = "Aggregated Jacoco coverage report across all modules"

    dependsOn(subprojects.flatMap { it.tasks.matching { it.name == "test" } })

    val classDirs = subprojects.mapNotNull { sub ->
        val dir = sub.layout.buildDirectory.get().asFile.resolve("classes/kotlin/main")
        if (dir.exists()) fileTree(dir) {
            include("**/*.class")
            exclude(jacocoExcludes)
        } else null
    }

    val sourceDirs = subprojects.mapNotNull { sub ->
        val src = sub.projectDir.resolve("src/main/kotlin")
        if (src.exists()) src else null
    }

    val execFiles = subprojects.mapNotNull { sub ->
        val exec = sub.layout.buildDirectory.get().asFile.resolve("jacoco/test.exec")
        if (exec.exists()) exec else null
    }

    classDirectories.setFrom(classDirs)
    sourceDirectories.setFrom(sourceDirs)
    executionData.setFrom(execFiles)

    reports {
        html.required.set(true)
        xml.required.set(true)
    }
}

tasks.named("clean") {
    doFirst {
        val orphanBuildDirs = ModuleConvention.allPaths().map { "$it/build" } +
            listOf("modules/build", "buildSrc/build")
        delete(orphanBuildDirs.map { file(it) })
    }
}
