package dev.jakub.terminal;

import dev.jakub.terminal.core.TerminalSupport;
import dev.jakub.terminal.interactive.Menu;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    @Test
    void singleOptionReturnsThatOption() {
        TerminalSupport support = new TerminalSupport(false, 80);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Menu menu = new Menu(support).title("Pick one").option("Only").output(new PrintStream(baos));
        String choice = menu.select();
        assertEquals("Only", choice);
    }

    @Test
    void noOptionsReturnsNull() {
        TerminalSupport support = new TerminalSupport(false, 80);
        Menu menu = new Menu(support).title("Empty");
        assertNull(menu.select());
    }

    @Test
    void selectWithInputReturnsChosenOption() {
        TerminalSupport support = new TerminalSupport(false, 80);
        ByteArrayOutputStream outBaos = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream("2\n".getBytes(StandardCharsets.UTF_8));
        Menu menu = new Menu(support)
                .title("Env")
                .option("Development")
                .option("Staging")
                .option("Production")
                .output(new PrintStream(outBaos))
                .input(in);
        String choice = menu.select();
        assertEquals("Staging", choice);
    }

    @Test
    void invalidInputThenValidSelectsOption() {
        TerminalSupport support = new TerminalSupport(false, 80);
        ByteArrayInputStream in = new ByteArrayInputStream("x\n99\n1\n".getBytes(StandardCharsets.UTF_8));
        Menu menu = new Menu(support)
                .title("Choose")
                .option("First")
                .option("Second")
                .input(in);
        String choice = menu.select();
        assertEquals("First", choice);
    }

    @Test
    void staticTerminalMenuApiWorks() {
        Menu m = Terminal.menu().title("Select environment").option("Dev").option("Prod");
        assertNotNull(m);
    }
}
