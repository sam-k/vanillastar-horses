@file:Suppress("PropertyName")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("fabric-loom") version "1.7-SNAPSHOT"
  id("maven-publish")
  id("org.jetbrains.kotlin.jvm") version "2.0.0"
  id("com.diffplug.spotless") version "7.0.0.BETA1"
}

val mod_version: String by project
val maven_group: String by project
val archives_base_name: String by project
val access_widener_path: String by project

val minecraft_version: String by project
val minecraft_target_version: String by project
val fabric_yarn_mappings: String by project
val fabric_loader_version: String by project
val fabric_api_version: String by project
val fabric_kotlin_version: String by project

val palantir_java_format_version: String by project
val ktfmt_version: String by project

version = mod_version
group = maven_group

base {
  archivesName.set(archives_base_name)
}

dependencies {
  minecraft("com.mojang", "minecraft", minecraft_version)
  mappings("net.fabricmc", "yarn", fabric_yarn_mappings, classifier = "v2")
  modImplementation("net.fabricmc", "fabric-loader", fabric_loader_version)
  modImplementation("net.fabricmc.fabric-api", "fabric-api", fabric_api_version)
  modImplementation("net.fabricmc", "fabric-language-kotlin", fabric_kotlin_version)
}

fabricApi {
  configureDataGeneration()
}

loom {
  accessWidenerPath = file(access_widener_path)
}

tasks {
  processResources {
    inputs.property("version", version)

    filesMatching("fabric.mod.json") {
      expand(
        mapOf(
          "version" to version,
          "minecraft_target_version" to minecraft_target_version,
          "fabric_loader_version" to fabric_loader_version,
          "fabric_api_version" to fabric_api_version,
          "fabric_kotlin_version" to fabric_kotlin_version,
        )
      )
    }
  }

  compileJava {
    options.release = 21
  }

  compileKotlin {
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_21)
    }
  }

  java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to
    // the `build` task if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  jar {
    from("LICENSE") {
      rename {
        "${it}_${archives_base_name}"
      }
    }
  }
}

spotless {
  ratchetFrom("origin/main")

  format("misc") {
    target("*.gradle", ".git-blame-ignore-revs", ".gitignore")

    trimTrailingWhitespace()
    indentWithSpaces(2)
    endWithNewline()
  }

  java {
    palantirJavaFormat(palantir_java_format_version).style("GOOGLE").formatJavadoc(true)
  }

  kotlin {
    ktfmt(ktfmt_version).googleStyle().configure {
      it.setBlockIndent(2)
      it.setContinuationIndent(4)
    }
  }
}
