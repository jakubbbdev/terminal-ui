package dev.jakub.terminal.example;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.components.Log;
import dev.jakub.terminal.components.Notification;
import dev.jakub.terminal.components.Steps;
import dev.jakub.terminal.core.Color;

import java.util.List;


public final class Examples {

    public static void main(String[] args) {
        runAll();
    }

    public static void runAll() {
        textAndRule();
        keyValueAndBox();
        table();
        tableFromRows();
        tree();
        columns();
        steps();
        breadcrumb();
        helpBlock();
        log();
        logWithMinLevel();
        diff();
        timeline();
        heatmap();
        chart();
        badgeAndNotify();
        codeBlock();
        sysInfo();
        markdown();
        toc();
    }

    static void textAndRule() {
        Terminal.print("=== Text & Rule ===").color(Color.CYAN).bold().println();
        Terminal.rule().character('=').print();
        Terminal.print("Hello").color(Color.GREEN).println();
        Terminal.rule().doubles().prefix("-- ").suffix(" --").print();
        System.out.println();
    }

    static void keyValueAndBox() {
        Terminal.print("=== KeyValue & Box ===").color(Color.CYAN).bold().println();
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
        System.out.println();
    }

    static void table() {
        Terminal.print("=== Table ===").color(Color.CYAN).bold().println();
        Terminal.table()
                .header("Col A", "Col B", "Col C")
                .row("1", "2", "3")
                .row("4", "5", "6")
                .print(System.out);
        System.out.println();
    }

    static void tableFromRows() {
        Terminal.print("=== Table from rows ===").color(Color.CYAN).bold().println();
        List<String[]> rows = List.of(
                new String[]{"Alice", "10"},
                new String[]{"Bob", "20"}
        );
        Terminal.table()
                .header("Name", "Score")
                .rows(rows)
                .print(System.out);
        System.out.println();
    }

    static void tree() {
        Terminal.print("=== Tree ===").color(Color.CYAN).bold().println();
        Terminal.tree()
                .node("root")
                .child("src")
                .child("main").end()
                .child("test").end()
                .end()
                .child("docs")
                .end()
                .print(System.out);
        System.out.println();
    }

    static void columns() {
        Terminal.print("=== Columns ===").color(Color.CYAN).bold().println();
        Terminal.columns()
                .column("Left\ncolumn\ntext")
                .column("Middle")
                .column("Right")
                .print(System.out);
        System.out.println();
    }

    static void steps() {
        Terminal.print("=== Steps ===").color(Color.CYAN).bold().println();
        Terminal.steps()
                .step("Setup", Steps.Status.DONE)
                .step("Build", Steps.Status.RUNNING)
                .step("Deploy", Steps.Status.PENDING)
                .print(System.out);
        System.out.println();
    }

    static void breadcrumb() {
        Terminal.print("=== Breadcrumb ===").color(Color.CYAN).bold().println();
        Terminal.breadcrumb()
                .crumb("Home")
                .crumb("Projects")
                .crumb("terminal-ui")
                .separator(" / ")
                .print(System.out);
        System.out.println();
    }

    static void helpBlock() {
        Terminal.print("=== Help (CLI usage) ===").color(Color.CYAN).bold().println();
        Terminal.help()
                .title("Options")
                .option("-v, --verbose", "Enable verbose output")
                .option("-h, --help", "Show this help")
                .option("--file <path>", "Input file path")
                .print(System.out);
        System.out.println();
    }

    static void log() {
        Terminal.print("=== Log ===").color(Color.CYAN).bold().println();
        Terminal.log()
                .info("Started")
                .warn("Deprecation notice")
                .error("Something failed")
                .withTimestamp()
                .print(System.out);
        System.out.println();
    }

    static void logWithMinLevel() {
        Terminal.print("=== Log (minLevel WARN) ===").color(Color.CYAN).bold().println();
        Terminal.log()
                .info("Hidden")
                .warn("Visible")
                .error("Visible")
                .minLevel(Log.Level.WARN)
                .print(System.out);
        System.out.println();
    }

    static void diff() {
        Terminal.print("=== Diff ===").color(Color.CYAN).bold().println();
        Terminal.diff()
                .before("old line 1\nold line 2")
                .after("new line 1\nnew line 2\nnew line 3")
                .print(System.out);
        System.out.println();
    }

    static void timeline() {
        Terminal.print("=== Timeline ===").color(Color.CYAN).bold().println();
        Terminal.timeline()
                .event("2025-01-01", "First release")
                .event("2025-06-01", "Added SelectList")
                .event("2026-01-01", "GitHub Packages")
                .print(System.out);
        System.out.println();
    }

    static void heatmap() {
        Terminal.print("=== Heatmap ===").color(Color.CYAN).bold().println();
        Terminal.heatmap()
                .row("Mon", 10, 20, 5, 30)
                .row("Tue", 15, 25, 10, 20)
                .row("Wed", 5, 15, 25, 35)
                .print(System.out);
        System.out.println();
    }

    static void chart() {
        Terminal.print("=== Chart ===").color(Color.CYAN).bold().println();
        Terminal.chart()
                .data(1.0, 2.0, 1.5, 3.0, 2.5)
                .height(5)
                .print(System.out);
        System.out.println();
    }

    static void badgeAndNotify() {
        Terminal.print("=== Badge & Notify ===").color(Color.CYAN).bold().println();
        Terminal.badge("OK", Color.GREEN).println();
        Terminal.badge("WARN", Color.YELLOW).print(System.out);
        Terminal.notify("Done!", Notification.Type.SUCCESS);
        Terminal.notify("Warning", Notification.Type.WARNING);
        System.out.println();
    }

    static void codeBlock() {
        Terminal.print("=== Code block ===").color(Color.CYAN).bold().println();
        Terminal.code("java")
                .line("public static void main(String[] args) {")
                .line("    System.out.println(\"Hello\");")
                .line("}")
                .lineNumbers()
                .print(System.out);
        System.out.println();
    }

    static void sysInfo() {
        Terminal.print("=== SysInfo ===").color(Color.CYAN).bold().println();
        Terminal.sysinfo().print(System.out);
        System.out.println();
    }

    static void markdown() {
        Terminal.print("=== Markdown ===").color(Color.CYAN).bold().println();
        Terminal.markdown("# Title\n\n**Bold** and *italic*.\n\n- Item 1\n- Item 2\n\n---")
                .print(System.out);
        System.out.println();
    }

    static void toc() {
        Terminal.print("=== Table of contents ===").color(Color.CYAN).bold().println();
        Terminal.toc()
                .section("Introduction").sub("Overview").sub("Install")
                .section("API").sub("Reference")
                .section("Examples")
                .print(System.out);
    }
}
