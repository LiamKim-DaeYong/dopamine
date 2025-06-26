package io.dopamine.build

object KtlintConvention {
    val excludes = listOf(
        "**/build/**",
        "**/generated/**",
        "**/src/main/java/**",
        "**/src/test/java/**"
    )

    val reporters = listOf("plain", "checkstyle")
}
