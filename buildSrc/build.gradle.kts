plugins {
    `kotlin-dsl`
    id("java")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.0.0-M1")
    implementation("io.spring.gradle:dependency-management-plugin:1.0.11.RELEASE")
}