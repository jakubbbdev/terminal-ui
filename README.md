# terminal-ui

Java library for terminal UI: tables, rules, colors, prompts, menus, SelectList, pager, and more. Fluent API, ANSI support, testable.

[![CI](https://github.com/jakubbbdev/terminal-ui/actions/workflows/ci.yml/badge.svg)](https://github.com/jakubbbdev/terminal-ui/actions/workflows/ci.yml)
[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk)](https://openjdk.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

[![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://codespaces.new/jakubbbdev/terminal-ui)

## Install

Published to [GitHub Packages](https://github.com/jakubbbdev/terminal-ui/packages). Replace `VERSION` with a [release](https://github.com/jakubbbdev/terminal-ui/releases) tag (e.g. `1.0.0`).

**Gradle (Kotlin DSL):**

```kotlin
repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.github.com/jakubbbdev/terminal-ui") }
}
dependencies {
    implementation("dev.jakub.terminal:terminal-ui:VERSION")
}
```

**Gradle (Groovy):**

```groovy
repositories {
    mavenCentral()
    maven { url "https://maven.pkg.github.com/jakubbbdev/terminal-ui" }
}
dependencies {
    implementation "dev.jakub.terminal:terminal-ui:VERSION"
}
```

**Maven:**

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

For public packages, read access usually works without credentials. If your tool asks for auth, use a [GitHub PAT](https://github.com/settings/tokens) with `read:packages` as username and the token as password.

## Requirements

- **Java 21**
- **Gradle 9.x** (wrapper included)

## Build & Test

```bash
./gradlew build
```

Run tests:

```bash
./gradlew test
```

## Usage (excerpt)

```java
import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Color;

// Colored text
Terminal.print("Hello").color(Color.CYAN).bold().println();

// Table
Terminal.table()
    .header("A", "B")
    .row("1", "2")
    .row("3", "4")
    .print(System.out);

// Prompt
String name = Terminal.prompt("Name: ").ask();

// Menu
String choice = Terminal.menu()
    .title("Environment")
    .option("Dev").option("Prod")
    .select();
```

## License

See [LICENSE](LICENSE).
