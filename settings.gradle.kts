pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.8"
    // 26.1+ 需要新的 loom 变体，此插件按 MC 版本自动选择
    id("dev.kikugie.loom-back-compat") version "0.4.2"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

stonecutter {
    create(rootProject) {
        versions(
            "1.16.5", "1.17.1", "1.18.2", "1.19.2", "1.19.4", "1.20",
            "1.20.2", "1.20.3", "1.20.6",
            "1.21", "1.21.2", "1.21.4", "1.21.5", "1.21.6", "1.21.7",
            "1.21.9", "1.21.10", "1.21.11",
            "26.1", "26.2", "26.3"
        )
        vcsVersion = "1.20.6"
    }
}

rootProject.name = "funnyport"
