package dev.jakub.terminal;

import dev.jakub.terminal.components.Table;
import dev.jakub.terminal.core.TerminalSupport;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    @Test
    void tableWithHeaderAndRowsPrintsStructure() {
        TerminalSupport support = new TerminalSupport(false, 80);
        Table table = new Table(support).header("Name", "Status", "CPU")
                .row("nginx", "✅ running", "2%")
                .row("redis", "❌ stopped", "-");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        table.print(new PrintStream(baos));
        String out = baos.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("Name"));
        assertTrue(out.contains("Status"));
        assertTrue(out.contains("CPU"));
        assertTrue(out.contains("nginx"));
        assertTrue(out.contains("running"));
        assertTrue(out.contains("redis"));
        assertTrue(out.contains("stopped"));
        assertTrue(out.contains("|"));
        assertTrue(out.contains("+") || out.contains("-"));
    }

    @Test
    void emptyTableDoesNotThrow() {
        TerminalSupport support = new TerminalSupport(false, 80);
        Table table = new Table(support);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> table.print(new PrintStream(baos)));
    }

    @Test
    void headerOnlyPrintsHeaderRow() {
        TerminalSupport support = new TerminalSupport(false, 80);
        Table table = new Table(support).header("A", "B");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        table.print(new PrintStream(baos));
        String out = baos.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("A"));
        assertTrue(out.contains("B"));
    }

    @Test
    void staticTerminalTableApiWorks() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Table t = Terminal.table();
        assertNotNull(t);
        t.header("X", "Y").row("a", "b").print(new PrintStream(baos));
        String out = baos.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("X") && out.contains("Y") && out.contains("a") && out.contains("b"));
    }
}
