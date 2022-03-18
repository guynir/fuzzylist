import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java-core")
    id("java-library")
    id("org.springframework.boot") apply (false)
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

// Since we're not applying Spring framework boot plugin, we need to import BOM coordinates manually.
the<DependencyManagementExtension>().apply {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.20.2")
}

// Extending compile-only artifacts to support annotation processor.
configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

// Disable generating of bootable JAR (since it's a library, not an executable).
tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

// Enable generation of .jar artifact.
tasks.getByName<Jar>("jar") {
    enabled = true
}

tasks.withType<Test> {
    useJUnitPlatform()
}