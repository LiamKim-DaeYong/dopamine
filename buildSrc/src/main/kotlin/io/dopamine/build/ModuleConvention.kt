package io.dopamine.build

object ModuleConvention {
    const val GROUP = "io.dopamine"
    const val VERSION = "0.1.0"
    const val JVM_TARGET = "21"

    const val MAIN_KOTLIN = "src/main/kotlin"
    const val MAIN_RESOURCES = "src/main/resources"

    const val TEST_KOTLIN = "src/test/kotlin"
    const val TEST_RESOURCES = "src/test/resources"

    val groups = listOf(
        "auth", "core", "docs", "file", "generator", "i18n",
        "response", "sample", "starter", "test", "trace"
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
