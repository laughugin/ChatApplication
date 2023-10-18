plugins {
    application
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit test framework.
    testImplementation("junit:junit:4.13.2")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")

    // Add Log4j dependencies
    implementation("org.apache.logging.log4j:log4j-core:2.17.0")
    implementation("org.apache.logging.log4j:log4j-api:2.17.0")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    // Define the main class for the application.
    mainClass.set("guiapp.Main")
}

sourceSets {
    main {
        java.srcDirs("app/src/main/java", "app/src/main/java/guiapp")
    }
}

val fatJar = tasks.register<Jar>("fatJar") {
    from(sourceSets.main.get().output)
    archiveClassifier.set("fat")
    manifest {
        attributes["Implementation-Title"] = "Gradle Jar File Example"
        attributes["Implementation-Version"] = project.version
        attributes["Main-Class"] = "guiapp.Main"
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.named("build") {
    dependsOn(fatJar)
}