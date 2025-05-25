plugins {
    id("java-library-spring")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":jpamodels"))

    //
    // Spring framework artifacts.
    //
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework:spring-core")

    //
    // Lombok project.
    //
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    //
    // Database drivers for testing.
    //
    testRuntimeOnly("com.h2database:h2")

    //
    // Mock framework.
    //
    testImplementation("org.mockito:mockito-core")

    //
    // JSON and YAML serializers.
    //
    implementation("com.fasterxml.jackson.core:jackson-databind")

    implementation("org.jetbrains:annotations:26.0.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}