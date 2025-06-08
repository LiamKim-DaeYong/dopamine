package io.dopamine.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class AutoConfigurationImportGeneratorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.afterEvaluate {
            val kotlinSrc = project.file("src/main/kotlin")
            if (!kotlinSrc.exists()) return@afterEvaluate

            val namespacePrefix = ModuleConvention.GROUP

            val autoConfigs = kotlinSrc.walkTopDown()
                .filter { it.isFile && it.extension == "kt" }
                .mapNotNull { file ->
                    val lines = file.readLines()
                    if (lines.any { it.contains(Regex("^\\s*@AutoConfiguration")) }) {
                        val relative = file.relativeTo(kotlinSrc).path
                            .removeSuffix(".kt")
                            .replace(File.separatorChar, '.')

                        // Fix accidental double-dot issue if namespacePrefix is nested in path
                        val full = if (relative.startsWith(namespacePrefix)) {
                            relative
                        } else {
                            "$namespacePrefix.$relative"
                        }

                        full.replace("..", ".")
                    } else null
                }
                .sorted()
                .toList()

            if (autoConfigs.isEmpty()) return@afterEvaluate

            val outputDir = project.layout.projectDirectory.dir("src/main/resources/META-INF/spring").asFile
            outputDir.mkdirs()
            val outputFile = File(outputDir, "org.springframework.boot.autoconfigure.AutoConfiguration.imports")
            outputFile.writeText(autoConfigs.joinToString("\n"))

            println("[auto-config] Generated imports for ${project.name}: ${autoConfigs.size} entries")
        }
    }
}
