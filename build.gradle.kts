import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
    alias(libs.plugins.ktlint) apply false
    jacoco
}

allprojects {
    group = "io.dopamine"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    // Java toolchain 설정 (Gradle 8+ 대응)
    plugins.withType<JavaPlugin> {
        extensions.configure<JavaPluginExtension>("java") {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(21))
            }
        }
    }

    // Kotlin 컴파일러 JVM 타겟 설정
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "21"
    }

    // 테스트 공통 로깅 설정
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = true
        }
    }
}

val ktlintCliVersion = libs.versions.ktlint.cli.get()
val jacocoExcludes = listOf(
    "**/Q*",
    "**/*Config*",
    "**/dto/**",
    "**/generated/**",
    "**/Dummy*",
    "**/*Kt.class"
)

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")

    // Ktlint 설정
    configure<KtlintExtension> {
        version.set(ktlintCliVersion)
        android.set(false)
        outputToConsole.set(true)

        filter {
            exclude("**/build/**", "**/generated/**")
        }

        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)
        }
    }

    // 테스트 후 jacocoTestReport 실행
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        finalizedBy("jacocoTestReport")
    }

    afterEvaluate {
        // 각 모듈에 jacocoTestReport 정의
        tasks.matching { it.name == "jacocoTestReport" }.configureEach {
            this as JacocoReport

            group = "verification"
            description = "Configures Jacoco coverage report for individual module"

            dependsOn(tasks.named("test"))

            val classDir = layout.buildDirectory.dir("classes/kotlin/main").get().asFile
            val execDir = layout.buildDirectory.get().asFile

            classDirectories.setFrom(
                fileTree(classDir) {
                    include("**/*.class")
                    exclude(jacocoExcludes)
                }
            )
            sourceDirectories.setFrom(files("src/main/kotlin"))
            executionData.setFrom(fileTree(execDir).include("jacoco/test.exec"))

            reports {
                html.required.set(true)
                xml.required.set(true)
            }
        }

        // Spring Boot annotationProcessor 자동 추가
        if (pluginManager.hasPlugin("org.springframework.boot")) {
            project.dependencies.add(
                "annotationProcessor",
                libs.spring.boot.configuration.processor.get()
            )
        }

        // application 플러그인이 없는 경우 bootJar 비활성화
        if (!pluginManager.hasPlugin("application")) {
            tasks.findByName("bootJar")?.enabled = false
            tasks.findByName("jar")?.enabled = true
        }
    }
}

tasks.register<JacocoReport>("jacocoRootReport") {
    group = "verification"
    description = "Aggregated Jacoco coverage report across all modules"

    dependsOn(subprojects.flatMap { it.tasks.matching { it.name == "test" } })

    val classDirs = subprojects.mapNotNull { sub ->
        val dir = sub.layout.buildDirectory.get().asFile.resolve("classes/kotlin/main")
        if (dir.exists()) fileTree(dir) {
            include("**/*.class")
            exclude(jacocoExcludes)
        } else null
    }

    val sourceDirs = subprojects.mapNotNull { sub ->
        val src = sub.projectDir.resolve("src/main/kotlin")
        if (src.exists()) src else null
    }

    val execFiles = subprojects.mapNotNull { sub ->
        val exec = sub.layout.buildDirectory.get().asFile.resolve("jacoco/test.exec")
        if (exec.exists()) exec else null
    }

    classDirectories.setFrom(classDirs)
    sourceDirectories.setFrom(sourceDirs)
    executionData.setFrom(execFiles)

    reports {
        html.required.set(true)
        xml.required.set(true)
    }
}
