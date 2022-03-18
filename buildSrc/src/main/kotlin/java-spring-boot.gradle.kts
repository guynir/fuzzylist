plugins {
    id("java-core")
    id("application")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
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

tasks.withType<Test> {
    useJUnitPlatform()
}

//tasks.getByName<BootJar>("bootJar") {
//    archiveClassifier.set("boot")
//}
//
//tasks.getByName<Jar>("jar") {
//    archiveClassifier.set("boot")
//}