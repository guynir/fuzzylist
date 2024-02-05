plugins {
    id("java-library-spring")
}

dependencies {
    implementation("org.springframework:spring-core")

    //
    // Apache commons.
    //
    implementation("org.apache.commons:commons-text:1.10.0")
}