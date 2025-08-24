package io.dopamine.build.plugins

import java.io.File

object AutoConfigurationCollector {
    fun collectAutoConfigurationClasses(sourceDir: File, namespacePrefix: String): List<String> {
        if (!sourceDir.exists()) return emptyList()

        return sourceDir.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .mapNotNull { file ->
                file.useLines { lines ->
                    if (lines.any { line ->
                            val trimmed = line.trimStart()
                            trimmed.startsWith("@AutoConfiguration") &&
                                (trimmed.length == "@AutoConfiguration".length ||
                                    trimmed["@AutoConfiguration".length].isWhitespace() ||
                                    trimmed["@AutoConfiguration".length] == '(')
                        }
                    ) {
                        val relative = file.relativeTo(sourceDir).path
                            .removeSuffix(".kt")
                            .replace('/', '.')
                            .replace('\\', '.')

                        val cleanPrefix = namespacePrefix.trimEnd('.')
                        if (cleanPrefix.isBlank()) relative else "$cleanPrefix.$relative"
                    } else null
                }
            }
            .sorted()
            .toList()
    }
}
