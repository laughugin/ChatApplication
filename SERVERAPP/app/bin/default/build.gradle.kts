plugins {
    application
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.apache.logging.log4j:log4j-core:2.17.0")
    implementation("org.apache.logging.log4j:log4j-api:2.17.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("serverapp.Main")
}

sourceSets {
    named("main") {
        java.srcDirs("app/src/main/java", "app/src/main/java/serverapp", "D:/git/chatapp/SERVERAPP/app/src/main/java/serverapp/db")
        resources.srcDirs("D:/git/chatapp/SERVERAPP/app/src/main/java/serverapp/db")
    }
    named("test") {
        java.srcDirs("src/test/java")
    }
}

tasks.named<Jar>("jar") {
    from(sourceSets["main"].output)
    from(sourceSets["main"].resources)
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.named<Test>("test") {
    useJUnit()
}