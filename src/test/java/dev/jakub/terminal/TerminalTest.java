package dev.jakub.terminal;

import dev.jakub.terminal.components.Table;
import dev.jakub.terminal.core.StyledText;
import dev.jakub.terminal.core.TerminalSupport;
import dev.jakub.terminal.interactive.Menu;
import dev.jakub.terminal.live.ProgressBar;
import dev.jakub.terminal.live.ProgressBarBuilder;
import dev.jakub.terminal.live.SpinnerBuilder;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class TerminalTest {

    @Test
    void tableReturnsNewTableInstance() {
        Table t = Terminal.table();
        assertNotNull(t);
    }

    @Test
    void printReturnsStyledText() {
        StyledText st = Terminal.print("hello");
        assertNotNull(st);
    }

    @Test
    void progressBarReturnsBuilder() {
        ProgressBarBuilder b = Terminal.progressBar();
        assertNotNull(b);
        ProgressBar bar = b.total(100).width(40).style(ProgressBar.Style.BLOCK).build();
        assertNotNull(bar);
    }

    @Test
    void spinnerReturnsBuilder() {
        SpinnerBuilder b = Terminal.spinner();
        assertNotNull(b);
    }

    @Test
    void menuReturnsMenuWithOptions() {
        Menu m = Terminal.menu().title("T").option("A").option("B");
        assertNotNull(m);
    }

    @Test
    void tableWithCustomSupportUsesThatSupport() {
        TerminalSupport support = new TerminalSupport(false, 80);
        Table t = Terminal.table(support);
        assertNotNull(t);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        t.header("A").row("b").print(new PrintStream(baos));
        assertTrue(baos.toString(StandardCharsets.UTF_8).contains("A"));
    }

    @Test
    void fullTableExampleFromSpec() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Terminal.table()
                .header("Name", "Status", "CPU")
                .row("nginx", "✅ running", "2%")
                .row("redis", "❌ stopped", "-")
                .print(new PrintStream(baos));
        String out = baos.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("Name") && out.contains("Status") && out.contains("CPU"));
        assertTrue(out.contains("nginx") && out.contains("redis"));
        assertTrue(out.contains("running") && out.contains("stopped"));
    }
}
