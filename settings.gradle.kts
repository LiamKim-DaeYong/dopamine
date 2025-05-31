rootProject.name = "dopamine"

val moduleGroups = listOf(
    "core",
    "docs",
    "response",
    "starter",
    "support",
    "trace"
)

moduleGroups.forEach { group ->
    val groupDir = file("modules/$group")
    if (groupDir.exists()) {
        groupDir.listFiles()
            ?.filter { it.isDirectory && file("${it.path}/build.gradle.kts").exists() }
            ?.forEach { subModule ->
                include(":modules:$group:${subModule.name}")
                project(":modules:$group:${subModule.name}").projectDir = subModule
            }
    }
}
