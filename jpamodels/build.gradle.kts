plugins {
    id("java-library-spring")
}

dependencies {
    implementation(project(":common"))

    implementation("org.springframework.data:spring-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.hsqldb:hsqldb")
    implementation(libs.commons.lang3)
}