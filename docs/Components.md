# Components

Overview of what terminal-ui provides. All entry points are on `Terminal.*`.

## Output & text

| API | Description |
|-----|-------------|
| `Terminal.print(String)` | Styled text (chain `.color()`, `.bold()`, `.println()`) |
| `Terminal.rule()` | Horizontal rule (e.g. `====`) |
| `Terminal.keyValue()` | Key-value pairs |
| `Terminal.box()` | Box around content |

## Tables & layout

| API | Description |
|-----|-------------|
| `Terminal.table()` | Tables with header/rows |
| `Terminal.tree()` | Tree structure |
| `Terminal.columns()` | Multi-column layout |
| `Terminal.steps()` | Step indicator |
| `Terminal.breadcrumb()` | Breadcrumb trail |
| `Terminal.toc()` | Table of contents |

## Interactive

| API | Description |
|-----|-------------|
| `Terminal.prompt(String)` | Text input (`.ask()`, `.masked().ask()`, `.validate(Predicate).retryMessage(String)`) |
| `Terminal.confirm(String)` | Y/n confirmation |
| `Terminal.menu()` | Numbered menu (`.option()`, `.select()`) |
| `Terminal.selectList()` | List with arrow keys + Enter |
| `Terminal.pager()` | Paged output (Enter/arrows/q) |
| `Terminal.help()` | CLI usage block (`.option(opt, desc)`, `.title()`) |

## Live / progress

| API | Description |
|-----|-------------|
| `Terminal.progressBar()` | Progress bar (`.total()`, `.build()`) |
| `Terminal.spinner()` | Spinning indicator (`.message()`, `.start()`) |
| `Terminal.dashboard()` | Refreshing widget panel |

## Data & visuals

| API | Description |
|-----|-------------|
| `Terminal.diff()` | Diff view |
| `Terminal.log()` | Log-style output |
| `Terminal.timeline()` | Timeline |
| `Terminal.heatmap()` | Heatmap |
| `Terminal.chart()` | Charts |

## Terminal control (ANSI)

| API | Description |
|-----|-------------|
| `Terminal.clearScreen()` | Clear entire screen |
| `Terminal.cursorTo(row, col)` | Move cursor (1-based) |

## Other

| API | Description |
|-----|-------------|
| `Terminal.badge(String, Color)` | Colored badge |
| `Terminal.notify(String, Type)` | Notification (info/success/warning/error) |
| `Terminal.code(String)` | Code block with language |
| `Terminal.sysinfo()` | System info (OS, Java, etc.) |
| `Terminal.markdown(String)` | Render markdown to terminal |

Use the fluent API from `Terminal.*`. For tests or to disable ANSI, inject a custom `TerminalSupport`.
