plugins {
    // 按 MC 版本自动选择 loom 变体
    id("dev.kikugie.loom-back-compat")
}

// 每个版本节点各跑一次本脚本
version = "${sc.current.version}+${property("mod_version")}"
base.archivesName = property("mod_id") as String

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
    sc.current.parsed >= "1.17" -> JavaVersion.VERSION_16
    else -> JavaVersion.VERSION_1_8
}

repositories {
    // Fabric Carpet 官方 maven
    maven {
        name = "Carpet"
        url = uri("https://masa.dy.fi/maven")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    // 全矩阵统一 Mojang 官方映射（loom-back-compat 按 loom 变体适配）
    loomx.applyMojangMappings()
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")
    modImplementation("carpet:fabric-carpet:${property("deps.carpet")}")
}

java {
    sourceCompatibility = requiredJava
    targetCompatibility = requiredJava
}

// 统一让所有版本节点（含 active）消费 stonecutter 预处理产物。
// stonecutter 0.9.8 对 active 节点默认直接编译原始 src，这里一并接管，行为全矩阵一致。
val scGeneratedJava = file("build/generated/stonecutter/main/java")
val scGeneratedRes = file("build/generated/stonecutter/main/resources")
sourceSets {
    main {
        java.setSrcDirs(listOf(scGeneratedJava))
        resources.setSrcDirs(listOf(scGeneratedRes))
    }
}
tasks.named<JavaCompile>("compileJava") {
    dependsOn("stonecutterGenerate")
}
tasks.named<ProcessResources>("processResources") {
    dependsOn("stonecutterGenerate")
}

val modVersion = project.version.toString()
val mcVersion = sc.current.version
val javaLevel = when {
    sc.current.parsed >= "1.20.5" -> "JAVA_21"
    sc.current.parsed >= "1.18" -> "JAVA_17"
    sc.current.parsed >= "1.17" -> "JAVA_16"
    else -> "JAVA_8"
}
// 鸡 mixin 类按版本三选一（与 mixins.json 模板配合）
val chickenMixin = when {
    sc.current.parsed >= "1.21.11" -> "\"ChickenMixinModern\","
    sc.current.parsed >= "1.21.2" -> "\"ChickenMixinLegacy\","
    else -> "\"ChickenMixinClassic\","
}
tasks.processResources {
    inputs.property("version", modVersion)
    inputs.property("mc", mcVersion)
    inputs.property("java", javaLevel)
    filesMatching("fabric.mod.json") {
        expand("version" to modVersion, "mc" to mcVersion)
    }
    filesMatching("funnyport.mixins.json") {
        expand("java" to javaLevel, "chicken_mixin" to chickenMixin)
    }
}

tasks.jar {
    from(file("LICENSE"))
}
