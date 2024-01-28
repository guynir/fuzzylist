import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("java-spring-boot")
}

dependencies {
    implementation(project(":jpamodels"))

    //
    // Spring framework.
    //
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    //
    // JDBC drivers.
    //
    runtimeOnly("org.postgresql:postgresql")

    implementation("info.picocli:picocli:4.6.1")
}

//
// Task for running database schema validation.
//
tasks.create<BootRun>("validatedb") {
    classpath = tasks.getByName<BootRun>("bootRun").classpath
    mainClass.set("com.fuzzylist.jpa.tool.SchemaToolApp")
    setArgsString(constructDatabasePropertiesLine("validate"))
}

//
// Task for running database schema update.
//
tasks.create<BootRun>("updatedb") {
    classpath = tasks.getByName<BootRun>("bootRun").classpath
    mainClass.set("com.fuzzylist.jpa.tool.SchemaToolApp")
    setArgsString(constructDatabasePropertiesLine("update"))
}

/**
 * Construct a command-line arguments string including hostname, port, username, password and schema name for
 * calling JPA migration tool.
 */
fun constructDatabasePropertiesLine(cmd: String): String {
    val dbHost: String by project
    val dbPort: String by project
    val dbUsername: String by project
    val dbPassword: String by project
    val dbSchema: String by project

    return "$cmd --host $dbHost --port $dbPort --user $dbUsername --password $dbPassword --schema $dbSchema"
}