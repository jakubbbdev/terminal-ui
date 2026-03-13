plugins {
    id("java")
    id("java-library")
    id("application")
}

tasks.named<JavaExec>("run") {
    jvmArgs("-Ddev.jakub.terminal.ansi=true", "-Dfile.encoding=UTF-8")
}


group = "dev.jakub.terminal"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}