package io.dopamine.build.plugins

import java.io.File

object AutoConfigurationCollector {
    fun collectAutoConfigurationClasses(sourceDir: File, namespacePrefix: String): List<String> {
        if (!sourceDir.exists()) return emptyList()

        return sourceDir.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .mapNotNull { file ->
                file.useLines { lines ->
                    if (lines.any { it.trimStart().startsWith("@AutoConfiguration") }) {
                        val relative = file.relativeTo(sourceDir).path
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
    }
}
