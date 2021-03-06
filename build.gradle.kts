import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.collections.mutableListOf

group = "rust"

buildscript {
  repositories {
    mavenCentral()
    jcenter()
  }
  dependencies { classpath(kotlin("gradle-plugin", "1.3.61")) }
}
repositories {
    mavenCentral()
    jcenter()
}

dependencies {
  testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.6.2")
  testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.6.2")
  implementation(kotlin("stdlib-jdk8"))
  implementation("org.usb4java:usb4java:1.3.0")
  implementation("org.usb4java:usb4java-javax:1.3.0")
}

plugins {
  java
  kotlin("jvm") version "1.3.72"
  id("org.jetbrains.intellij") version "0.4.21"
}

tasks.withType<JavaCompile> {
  sourceCompatibility = "1.8"
  targetCompatibility = "1.8"
}

// compile bytecode to java 8 (default is java 6)
tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
    freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
  }
}

val ide :String by project

val pluginsX = mutableListOf("org.toml.lang:0.2.129.3308-202", "org.rust.lang:0.3.129.3308-202")
if(ide in listOf("IC")) {
  pluginsX.add("java")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
  version = "2020.2"
  type = ide
  setPlugins("org.toml.lang:0.2.129.3308-202", "org.rust.lang:0.3.129.3308-202")
  downloadSources = true
}
