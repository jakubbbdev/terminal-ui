# Install

Published to [GitHub Packages](https://github.com/jakubbbdev/terminal-ui/packages). Use a [release](https://github.com/jakubbbdev/terminal-ui/releases) version (e.g. `1.0.1`) for `VERSION`.

## Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.github.com/jakubbbdev/terminal-ui") }
}
dependencies {
    implementation("dev.jakub.terminal:terminal-ui:VERSION")
}
```

## Gradle (Groovy)

```groovy
repositories {
    mavenCentral()
    maven { url "https://maven.pkg.github.com/jakubbbdev/terminal-ui" }
}
dependencies {
    implementation "dev.jakub.terminal:terminal-ui:VERSION"
}
```

## Maven

```xml
<repository>
  <id>github</id>
  <url>https://maven.pkg.github.com/jakubbbdev/terminal-ui</url>
</repository>
<dependency>
  <groupId>dev.jakub.terminal</groupId>
  <artifactId>terminal-ui</artifactId>
  <version>VERSION</version>
</dependency>
```

## Auth (if required)

For public packages, resolution often works without credentials. If your build asks for auth, use a [GitHub PAT](https://github.com/settings/tokens) with `read:packages`: username = your GitHub username, password = token.
