plugins {
    id("java")
    id("java-library")
    id("application")
    id("maven-publish")
}

application {
    mainClass.set("dev.jakub.terminal.example.Examples")
}

tasks.named<JavaExec>("run") {
    jvmArgs("-Ddev.jakub.terminal.ansi=true", "-Dfile.encoding=UTF-8")
}

group = "dev.jakub.terminal"
version = System.getenv("RELEASE_VERSION") ?: "1.0-SNAPSHOT"

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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "terminal-ui"
            version = project.version.toString()
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/jakubbbdev/terminal-ui")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gpr.user")?.toString()
                password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.token")?.toString()
            }
        }
    }
}