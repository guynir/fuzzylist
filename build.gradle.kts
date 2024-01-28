buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.postgresql:postgresql:42.2.23")
    }
}

plugins {
    id("org.flywaydb.flyway") version "8.0.0-beta3"
    id("com.nocwriter.runsql") version "1.0.3"
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly("org.postgresql:postgresql:42.5.1")
}

//
// Task for running database schema update.
//
tasks.create("updatedb") {
    dependsOn(project(":jpa-migration-tool").getTasksByName("updatedb", false))
}

//
// Task for running database schema validation.
//
tasks.create("validatedb") {
    dependsOn(project(":jpa-migration-tool").getTasksByName("validatedb", false))
}

val dbHost: String by project
val dbPort: String by project
val dbUsername: String by project
val dbPassword: String by project
val dbSchema: String by project

//
// FlywayDB for managing database migrations.
//
flyway {
    url = "jdbc:postgresql://$dbHost:$dbPort/$dbSchema"
    user = dbUsername
    password = dbPassword
    locations = arrayOf("filesystem:./sys/db/migrations/data", "filesystem:./sys/db/migrations/schema")
    if (isDevelopmentProfile()) {
        print("Running in development environment -- adding dev SQLs for migration.")
        locations += "filesystem:./sys/db/migrations/dev"
    }
}

//
// Drops existing database and create an empty one.
//
tasks.create<RunSQL>("createDatabase") {
    doFirst {
        println(
            """
        
        Running database initialization process (all previous data will be dropped).
        
        """.trimIndent()
        )

        println(
            """Creating new database...
         Hostname: $dbHost
             Port: $dbPort
         Username: $dbUsername
    Database name: $dbSchema
            """.trimIndent()
        )
    }
    config {
        username = dbUsername
        password = dbPassword
        url = "jdbc:postgresql://$dbHost:$dbPort/"
        driverClassName = "org.postgresql.Driver"
        script = """
            DROP DATABASE IF EXISTS $dbSchema;
            CREATE DATABASE $dbSchema OWNER $dbUsername;
        """
    }
}

//
// Database initialization -- creates a new database and apply all migrations.
//
tasks.create("initdb") {
    dependsOn("createDatabase")
    dependsOn("flywayMigrate").mustRunAfter("createDatabase")
    dependsOn("flywayInfo").mustRunAfter("flywayMigrate")
}

/**
 * Check whether Gradle is running in development environment. This function checks if the property 'env' is
 * set to 'dev'.
 *
 * @return `true` if development profile is set, `false` if not.
 */
fun isDevelopmentProfile(): Boolean {
    return project.hasProperty("env") &&
            (project.property("env") as String).equals("dev", ignoreCase = true)
}