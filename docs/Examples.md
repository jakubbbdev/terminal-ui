# Examples

All examples use `import dev.jakub.terminal.Terminal;` and `import dev.jakub.terminal.core.Color;` where needed. Use `System.out` or omit the argument where `.print()` has a no-arg overload.

---

## Text & rules

```java
Terminal.print("Hello").color(Color.CYAN).bold().println();

Terminal.rule().character('=').print();
Terminal.rule().doubles().prefix("-- ").suffix(" --").print();
```

## Key-value & box

```java
Terminal.keyValue()
    .row("Name", "terminal-ui")
    .row("Version", "1.0")
    .separator(" = ")
    .print(System.out);

Terminal.box()
    .title("Info")
    .line("First line")
    .lines("Second", "Third")
    .print(System.out);
```

## Table

```java
Terminal.table()
    .header("Col A", "Col B", "Col C")
    .row("1", "2", "3")
    .row("4", "5", "6")
    .print(System.out);
```

## Tree

```java
Terminal.tree()
    .node("root")
        .child("src")
            .child("main").end()
            .child("test").end()
        .end()
        .child("docs")
    .end()
    .print(System.out);
```

## Columns

```java
Terminal.columns()
    .column("Left\ncolumn\ntext")
    .column("Middle")
    .column("Right")
    .print(System.out);
```

## Steps (wizard-style)

```java
Terminal.steps()
    .step("Setup", Steps.Status.DONE)
    .step("Build", Steps.Status.RUNNING)
    .step("Deploy", Steps.Status.PENDING)
    .print(System.out);
```

(Use `import dev.jakub.terminal.components.Steps;` for `Steps.Status`.)

## Breadcrumb

```java
Terminal.breadcrumb()
    .crumb("Home")
    .crumb("Projects")
    .crumb("terminal-ui")
    .separator(" / ")
    .print(System.out);
```

## Interactive: Prompt

```java
String name = Terminal.prompt("Name: ").ask();
String secret = Terminal.prompt("Password: ").masked().ask();
// With shared scanner (e.g. in a loop):
String line = Terminal.prompt("Input: ").ask(sharedScanner);
```

## Interactive: Confirm

```java
boolean yes = Terminal.confirm("Continue?").defaultYes().ask();
boolean no = Terminal.confirm("Delete?").defaultNo().ask();
boolean answer = Terminal.confirm("Proceed?").ask(sharedScanner);
```

## Interactive: Menu

```java
String env = Terminal.menu()
    .title("Choose environment")
    .option("Development")
    .option("Staging")
    .option("Production")
    .select();
```

## Interactive: SelectList (arrows + Enter)

```java
String choice = Terminal.selectList()
    .title("Pick one")
    .option("Option A")
    .option("Option B")
    .option("Option C")
    .select();
```

## Interactive: Pager

```java
Terminal.pager()
    .lines("Line 1", "Line 2", "Line 3", "Line 4", "Line 5")
    .pageSize(3)
    .interactive();
```

## Progress bar

```java
ProgressBar bar = Terminal.progressBar()
    .total(100)
    .width(40)
    .style(ProgressBar.Style.BLOCK)
    .build();
for (int i = 0; i <= 100; i++) {
    bar.update(i);
    Thread.sleep(30);
}
```

(Use `import dev.jakub.terminal.live.ProgressBar;`.)

## Spinner

```java
Spinner spin = Terminal.spinner().message("Loading...").start();
// ... do work ...
spin.stop();
```

(Use `import dev.jakub.terminal.live.Spinner;`.)

## Diff

```java
Terminal.diff()
    .before("old line 1\nold line 2")
    .after("new line 1\nnew line 2\nnew line 3")
    .print(System.out);
```

## Log

```java
Terminal.log()
    .info("Started")
    .warn("Deprecation notice")
    .error("Something failed")
    .withTimestamp()
    .print(System.out);
```

## Timeline

```java
Terminal.timeline()
    .event("2024-01-01", "First release")
    .event("2024-06-01", "Added SelectList")
    .event("2025-01-01", "GitHub Packages")
    .print(System.out);
```

## Heatmap

```java
Terminal.heatmap()
    .row("Mon", 10, 20, 5, 30)
    .row("Tue", 15, 25, 10, 20)
    .row("Wed", 5, 15, 25, 35)
    .print(System.out);
```

## Chart (sparkline / bar)

```java
Terminal.chart()
    .data(1.0, 2.0, 1.5, 3.0, 2.5)
    .height(5)
    .print(System.out);
```

## Badge & notification

```java
Terminal.badge("OK", Color.GREEN).println();
Terminal.badge("WARN", Color.YELLOW).print(System.out);

Terminal.notify("Done!", Notification.Type.SUCCESS);
Terminal.notify("Warning", Notification.Type.WARNING);
Terminal.notify("Error", Notification.Type.ERROR);
Terminal.notify("Info", Notification.Type.INFO);
```

(Use `import dev.jakub.terminal.components.Notification;`.)

## Code block

```java
Terminal.code("java")
    .line("public static void main(String[] args) {")
    .line("    System.out.println(\"Hello\");")
    .line("}")
    .lineNumbers()
    .print(System.out);
```

## SysInfo

```java
Terminal.sysinfo().print(System.out);
```

## Markdown

```java
Terminal.markdown("# Title\n\n**Bold** and *italic*.\n\n- Item 1\n- Item 2\n\n---")
    .print(System.out);
```

## Table of contents

```java
Terminal.toc()
    .section("Introduction").sub("Overview").sub("Install")
    .section("API").sub("Reference")
    .section("Examples")
    .print(System.out);
```

---

For more, see the [README](https://github.com/jakubbbdev/terminal-ui#usage-excerpt) and the source.
