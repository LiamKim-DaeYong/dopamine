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
subprojects {
    // Ktlint 설정
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")

    configure<KtlintExtension> {
        version.set(ktlintCliVersion)
        android.set(false)
        outputToConsole.set(true)

        filter {
            exclude("**/build/**")
            exclude("**/generated/**")
        }

        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)
        }
    }

    // 테스트 → jacoco 리포트 자동 실행 연결
    tasks.withType<Test>().configureEach {
        finalizedBy("jacocoTestReport")
    }

    // jacoco 리포트 설정 (자동 생성된 task에 설정만 덮어씌움)
    tasks.matching { it.name == "jacocoTestReport" }.configureEach {
        this as JacocoReport

        group = "verification"
        description = "Generates code coverage report for the test task using Jacoco"

        reports {
            html.required.set(true)
            xml.required.set(true)
        }

        val buildDirPath = layout.buildDirectory.asFile.get()

        classDirectories.setFrom(
            fileTree(buildDirPath.resolve("classes/kotlin/main")) {
                exclude("**/Q*")
            }
        )
        executionData.setFrom(files(buildDirPath.resolve("jacoco/test.exec")))
        sourceDirectories.setFrom(files("src/main/kotlin"))
    }

    // Spring Boot 프로젝트에 annotationProcessor 자동 추가
    pluginManager.withPlugin("org.springframework.boot") {
        dependencies {
            add("annotationProcessor", libs.spring.boot.configuration.processor)
        }
    }
}
