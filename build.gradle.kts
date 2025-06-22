import io.dopamine.build.JacocoConvention.configureJacocoReport
import io.dopamine.build.JacocoConvention.registerJacocoRootReport
import io.dopamine.build.KtlintConvention
import io.dopamine.build.ModuleConvention
import io.dopamine.build.TestConvention.configureStandardLogging
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    base
    jacoco
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
    alias(libs.plugins.ktlint) apply false
}

val springBootVersion = libs.versions.spring.boot.get()

allprojects {
    group = ModuleConvention.GROUP
    version = ModuleConvention.VERSION

    repositories {
        mavenCentral()
    }

    plugins.withType<JavaPlugin> {
        extensions.configure<JavaPluginExtension>("java") {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(ModuleConvention.JVM_TARGET))
            }
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.valueOf("JVM_${ModuleConvention.JVM_TARGET}"))
        }
    }
}

val ktlintCliVersion = libs.versions.ktlint.cli.get()
subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")
    apply(plugin = "io.spring.dependency-management")

    afterEvaluate {
        extensions.configure<DependencyManagementExtension> {
            imports {
                mavenBom("${ModuleConvention.Spring.BOM}:$springBootVersion")
            }
        }
    }

    configure<KtlintExtension> {
        version.set(ktlintCliVersion)
        android.set(false)
        outputToConsole.set(true)

        filter {
            exclude(KtlintConvention.excludes)
        }

        reporters {
            KtlintConvention.reporters.forEach {
                reporter(ReporterType.valueOf(it.uppercase()))
            }
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        configureStandardLogging()
        finalizedBy("jacocoTestReport")
    }

    plugins.withId("org.springframework.boot") {
        dependencies.add("annotationProcessor", libs.spring.boot.configuration.processor.get())
    }

    afterEvaluate {
        configureJacocoReport()

        plugins.withId("org.springframework.boot") {
            tasks.named("bootJar").configure { enabled = false }
            tasks.named("jar").configure { enabled = true }
        }
    }
}

registerJacocoRootReport()

tasks.named("clean") {
    doFirst {
        delete(ModuleConvention.buildDirs.map { file(it) })
    }
}
