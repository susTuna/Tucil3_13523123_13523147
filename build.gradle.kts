plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    
    // Guava dependency
    implementation("com.google.guava:guava:33.3.1-jre")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    
    // Define custom source sets
    sourceSets {
        main {
            java {
                srcDirs = ["src/main/java"]
            }
            resources {
                srcDirs = ["src/main/resources"]
            }
        }
        test {
            java {
                srcDirs = ["test/java"]
            }
            resources {
                srcDirs = ["test/resources"]
            }
        }
    }
}

// Configure output directories
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    destinationDirectory.set(file("bin"))
}

// Configure test output directory
tasks.withType<Test> {
    useJUnitPlatform()
    binaryResultsDirectory.set(file("bin/test-results"))
}

application {
    mainClass.set("org.example.App")
}