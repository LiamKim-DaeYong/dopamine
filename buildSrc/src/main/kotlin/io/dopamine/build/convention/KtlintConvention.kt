package io.dopamine.build.convention

object KtlintConvention {
    val excludes = listOf(
        "**/build/**",
        "**/generated/**",
        "**/src/main/java/**",
        "**/src/test/java/**"
    )

    val reporters = listOf("plain", "checkstyle")
}
