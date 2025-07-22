package io.dopamine.build.plugins

import io.dopamine.build.convention.ModuleConvention
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class AutoConfigurationImportCollectorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val generateTask = project.tasks.register("generateAutoConfigurationImportsForThisModule") {
            group = "build setup"
            description = "Scans this module for @AutoConfiguration classes and saves to imports.txt"

            doLast {
                val kotlinSrc = project.file("src/main/kotlin")
                if (!kotlinSrc.exists()) {
                    project.logger.info("[auto-config][collector] Skipped: no Kotlin source in ${project.name}")
                    return@doLast
                }

                val namespacePrefix = ModuleConvention.GROUP

                val autoConfigs = AutoConfigurationCollector.collectAutoConfigurationClasses(
                    sourceDir = kotlinSrc,
                    namespacePrefix = namespacePrefix
                )

                if (autoConfigs.isEmpty()) {
                    project.logger.info("[auto-config][collector] No auto-configs found in ${project.name}")
                    return@doLast
                }

                val outputFile = project.layout.buildDirectory
                    .file("generated/auto-config/imports.txt")
                    .get()
                    .asFile

                outputFile.parentFile.mkdirs()
                outputFile.writeText(autoConfigs.joinToString("\n"))

                project.logger.lifecycle("[auto-config][collector] Saved ${autoConfigs.size} imports to ${outputFile.relativeTo(project.projectDir)}")
            }
        }

        project.tasks.matching { it.name == "processResources" }.configureEach {
            dependsOn(generateTask)
        }
    }
}
