package io.dopamine.build

import org.gradle.api.Plugin
import org.gradle.api.Project

class AutoConfigurationImportGeneratorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.afterEvaluate {
            val kotlinSrc = project.file("src/main/kotlin")
            if (!kotlinSrc.exists()) {
                project.logger.info("[auto-config] Skipped: no Kotlin source in ${project.name}")
                return@afterEvaluate
            }

            val namespacePrefix = ModuleConvention.GROUP

            val autoConfigs = kotlinSrc.walkTopDown()
                .filter { it.isFile && it.extension == "kt" }
                .mapNotNull { file ->
                    file.useLines { lines ->
                        if (lines.any { it.trimStart().startsWith("@AutoConfiguration") }) {
                            val relative = file.relativeTo(kotlinSrc).path
                                .removeSuffix(".kt")
                                .replace('/', '.')
                                .replace('\\', '.')

                            val full = if (relative.startsWith(namespacePrefix)) {
                                relative
                            } else {
                                "$namespacePrefix.$relative"
                            }

                            full.replace("..", ".")
                        } else null
                    }
                }
                .sorted()
                .toList()

            if (autoConfigs.isEmpty()) return@afterEvaluate

            val outputDir = project.layout.projectDirectory.dir("src/main/resources/META-INF/spring").asFile

            val outputFile = outputDir.resolve("org.springframework.boot.autoconfigure.AutoConfiguration.imports")
            outputFile.parentFile.mkdirs()
            outputFile.writeText(autoConfigs.joinToString("\n"))

            project.logger.lifecycle("[auto-config] Generated ${autoConfigs.size} imports for '${project.name}'")
        }
    }
}
