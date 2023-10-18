plugins {
    application
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("org.apache.logging.log4j:log4j-core:2.17.0")
    implementation("org.apache.logging.log4j:log4j-api:2.17.0")
    testImplementation("org.mockito:mockito-core:5.3.1")
    testImplementation("junit:junit:4.13.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("serverapp.Main")
}

sourceSets {
    named("test") {
        java.srcDirs("src/test/java")
    }
}

tasks {
    named<Jar>("jar") {
        from(sourceSets["main"].output)
        from(sourceSets["main"].resources)
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    named<Test>("test") {
        useJUnit()
        ignoreFailures = true
    }
}