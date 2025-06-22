package io.dopamine.build

object ModuleConvention {
    const val GROUP = "io.dopamine"
    const val VERSION = "0.0.1-SNAPSHOT"
    const val JVM_TARGET = "21"

    const val MAIN_SOURCE_DIR = "src/main/kotlin"

    val groups = listOf(
        "auth", "core", "docs", "file", "i18n", "response",
        "sample", "starter", "test", "trace"
    )

    object Spring {
        const val BOM_GROUP = "org.springframework.boot"
        const val BOM_ARTIFACT = "spring-boot-dependencies"
        const val BOM = "$BOM_GROUP:$BOM_ARTIFACT"
    }

    fun allPaths(base: String = "modules"): List<String> =
        groups.map { "$base/$it" }

    val buildDirs = allPaths().map { "$it/build" } +
        listOf("modules/build", "buildSrc/build")
}
