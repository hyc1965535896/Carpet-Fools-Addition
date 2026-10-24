plugins {
    id("dev.kikugie.stonecutter")
    // loom-back-compat 按版本二选一：老版本经典 loom，26.1+ 用 loom-remap
    id("fabric-loom") version "1.19.3" apply false
    id("net.fabricmc.fabric-loom-remap") version "1.19.3" apply false
}

// IDE / 默认构建使用的活动版本
stonecutter active "1.20.6"

// 一次性构建全部版本：gradlew buildAll
tasks.register("buildAll") {
    group = "build"
    dependsOn(stonecutter.tasks.named("build"))
}
