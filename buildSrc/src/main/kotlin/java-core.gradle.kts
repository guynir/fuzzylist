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
    id("version-catalog")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven { url = uri("https://repo.spring.io/milestone") }
}

group = "com.fuzzylist"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
