package io.dopamine.build.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class AutoConfigurationImportWriterPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val generateTask = project.tasks.register("generateCombinedAutoConfigurationImports") {
            group = "build setup"
            description = "Combines all module-level imports.txt into a single .imports file"

            doLast {
                val collectedImports = mutableListOf<String>()

                project.rootProject.subprojects.forEach { sub ->
                    val importFile = sub.layout.buildDirectory
                        .file("generated/auto-config/imports.txt")
                        .get()
                        .asFile
                    if (importFile.exists()) {
                        val entries = importFile.readLines()
                            .map { it.trim() }
                            .filter { it.isNotBlank() }

                        if (entries.isNotEmpty()) {
                            project.logger.info("[auto-config][writer] Found ${entries.size} imports from '${sub.name}'")
                            collectedImports += entries
                        }
                    }
                }

                if (collectedImports.isEmpty()) {
                    project.logger.warn("[auto-config][writer] No auto-configs found in any modules")
                    return@doLast
                }

                val outputFile = project.layout.projectDirectory
                    .file("src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports")
                    .asFile

                outputFile.parentFile.mkdirs()
                outputFile.writeText(collectedImports.sorted().joinToString("\n"))

                project.logger.lifecycle("[auto-config][writer] Generated .imports with ${collectedImports.size} entries")
            }
        }

        project.tasks.named("processResources").configure {
            dependsOn(generateTask)
        }

        project.tasks.named("clean").configure {
            doFirst {
                val importsFile = project.file("src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports")
                if (importsFile.exists()) {
                    importsFile.delete()
                    project.logger.lifecycle("[auto-config][writer] Deleted old .imports file during clean")
                }
            }
        }
    }
}
