//enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
        gradlePluginPortal()
    }
}
rootProject.name = "parent"

include("common")
include("jpamodels")
include("webapp-boot")

include("jpa-migration-tool")

//
// Version catalog.
//
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("commons-lang3", "org.apache.commons:commons-lang3:3.12.0")
            library("commons-validator", "commons-validator:commons-validator:1.7")
        }

        create("testLibs") {
            library("json-path-assert", "com.jayway.jsonpath:json-path-assert:2.6.0")
        }
    }
}
