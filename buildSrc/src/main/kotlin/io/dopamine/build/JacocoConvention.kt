package io.dopamine.build

import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.tasks.JacocoReport

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

            val classDir = layout.buildDirectory.dir("classes/kotlin/main").get().asFile
            val execDir = layout.buildDirectory.get().asFile

            classDirectories.setFrom(
                fileTree(classDir) {
                    include("**/*.class")
                    exclude(excludes)
                }
            )

            sourceDirectories.setFrom(files(ModuleConvention.MAIN_SOURCE_DIR))
            executionData.setFrom(fileTree(execDir).include("jacoco/test.exec"))

            reports {
                html.required.set(true) // NOSONAR
                xml.required.set(true) // NOSONAR
            }
        }
    }

    fun Project.registerJacocoRootReport() {
        tasks.register<JacocoReport>("jacocoRootReport") {
            group = "verification"
            description = "Aggregated Jacoco coverage report across all modules"

            dependsOn(subprojects.flatMap { it.tasks.matching { it.name == "test" } })

            val classDirs = subprojects.mapNotNull { sub ->
                val dir = sub.layout.buildDirectory.get().asFile.resolve("classes/kotlin/main")
                if (dir.exists()) fileTree(dir) {
                    include("**/*.class")
                    exclude(excludes)
                } else null
            }

            val sourceDirs = subprojects.mapNotNull { sub ->
                val src = sub.projectDir.resolve(ModuleConvention.MAIN_SOURCE_DIR)
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
                html.required.set(true) // NOSONAR
                xml.required.set(true) // NOSONAR
            }
        }
    }

}
