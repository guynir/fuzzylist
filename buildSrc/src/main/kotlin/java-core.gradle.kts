/**********************************************************************
 *
 * Base configuration for all Java/Kotlin projects.
 *
 * - Supports default group/version.
 * - Support Java 17.
 * - Provides JUnit 5 by default.
 *
 *********************************************************************/

plugins {
    id("java")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        url = uri("https://maven.pkg.github.com/guynir/jack")
    }

}

group = "com.fuzzylist"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
