plugins {
    kotlin("jvm") version "1.4.10"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.pinterest.ktlint:ktlint-core:0.39.0")
    ktlintRuleset(files("./build/libs/ktlint-custom-rules-1.0-SNAPSHOT.jar"))
}

tasks {
    ktlint {
        filter {
            include("**/main/kotlin/**")
        }
    }
}
