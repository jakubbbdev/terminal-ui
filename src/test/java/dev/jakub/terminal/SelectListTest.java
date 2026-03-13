package dev.jakub.terminal;

import dev.jakub.terminal.core.TerminalSupport;
import dev.jakub.terminal.interactive.SelectList;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SelectListTest {

    @Test
    void emptyOptionsReturnsNull() {
        SelectList list = new SelectList(new TerminalSupport(false, 80));
        assertNull(list.select());
    }

    @Test
    void singleOptionReturnsIt() {
        TerminalSupport support = new TerminalSupport(false, 80);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SelectList list = new SelectList(support).option("Only").output(new PrintStream(out)).input(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));
        assertEquals("Only", list.select());
    }

    @Test
    void numberInputSelectsOption() {
        TerminalSupport support = new TerminalSupport(false, 80);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream("2\n".getBytes(StandardCharsets.UTF_8));
        SelectList list = new SelectList(support)
                .title("Pick")
                .option("A")
                .option("B")
                .option("C")
                .output(new PrintStream(out))
                .input(in);
        String result = list.select();
        assertEquals("B", result);
    }
}
