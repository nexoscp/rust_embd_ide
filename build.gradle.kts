import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "rust"

repositories {
    mavenCentral()
}

plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.6.10"
  id("org.jetbrains.intellij") version "1.4.0"
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
  implementation(kotlin("stdlib-jdk8"))
  implementation("org.usb4java:usb4java:1.3.0")
  implementation("org.usb4java:usb4java-javax:1.3.0")
}

tasks.withType<JavaCompile> {
  sourceCompatibility = "11"
  targetCompatibility = "11"
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "11"
  }
}

val ide :String by project

// See https://github.com/JetBrains/gradle-intellij-plugin/
tasks {
  intellij {
    version.set("2021.3.3")
    type.set(ide)
    plugins.set(listOf("org.toml.lang:213.5744.224"))
    downloadSources.set(true)
  }
}
