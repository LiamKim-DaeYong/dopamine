package io.dopamine.build

import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.io.File

object JacocoConvention {
    val excludes = listOf(
        "**/Q*",
        "**/*Config*",
        "**/dto/**",
        "**/generated/**",
        "**/Dummy*",
        "**/*Kt.class"
    )

    fun Project.configureJacocoReport() {
        tasks.matching { it.name == "jacocoTestReport" }.configureEach {
            this as JacocoReport

            group = "verification"
            description = "Jacoco coverage report for module: $name"
            dependsOn(tasks.named("test"))

            val buildDir = layout.buildDirectory.get().asFile
            val classDir = buildDir.resolve("classes/kotlin/main")
            val execFile = buildDir.resolve("jacoco/test.exec")

            classDirectories.setFrom(
                fileTree(classDir) {
                    include("**/*.class")
                    exclude(JacocoConvention.excludes)
                }
            )

            sourceDirectories.setFrom(files(ModuleConvention.MAIN_KOTLIN))
            executionData.setFrom(files(execFile))

            enableStandardReports()
        }
    }

    fun Project.registerJacocoRootReport() {
        tasks.register<JacocoReport>("jacocoRootReport") {
            group = "verification"
            description = "Aggregated Jacoco coverage report across all modules"

            dependsOn(subprojects.flatMap { it.tasks.matching { it.name == "test" } })

            val classDirs = subprojects
                .map { it.layout.buildDirectory.dir("classes/kotlin/main").get().asFile }
                .filter(File::exists)
                .map {
                    fileTree(it) {
                        include("**/*.class")
                        exclude(excludes)
                    }
                }

            val sourceDirs = subprojects
                .map { it.projectDir.resolve(ModuleConvention.MAIN_KOTLIN) }
                .filter(File::exists)

            val execFiles = subprojects
                .map { it.layout.buildDirectory.get().asFile.resolve("jacoco/test.exec") }
                .filter(File::exists)

            classDirectories.setFrom(classDirs)
            sourceDirectories.setFrom(sourceDirs)
            executionData.setFrom(execFiles)

            enableStandardReports()
        }
    }

    private fun JacocoReport.enableStandardReports() {
        reports {
            html.required.set(true) // NOSONAR
            xml.required.set(true)  // NOSONAR
        }
    }
}
