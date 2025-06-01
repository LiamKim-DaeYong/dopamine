package io.dopamine.build

object ModuleConvention {
    const val GROUP = "io.dopamine"
    const val VERSION = "0.0.1-SNAPSHOT"
    const val JVM_TARGET = "21"

    val groups = listOf(
        "core",
        "docs",
        "response",
        "sample",
        "starter",
        "test",
        "trace"
    )

    val jacocoExcludes = listOf(
        "**/Q*",
        "**/*Config*",
        "**/dto/**",
        "**/generated/**",
        "**/Dummy*",
        "**/*Kt.class"
    )

    fun allPaths(base: String = "modules"): List<String> =
        groups.map { "$base/$it" }
}
