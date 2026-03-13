package dev.jakub.terminal;

import dev.jakub.terminal.core.Color;
import dev.jakub.terminal.core.StyledText;
import dev.jakub.terminal.core.TerminalSupport;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class StyledTextTest {

    @Test
    void printWithoutAnsiOutputsPlainText() {
        TerminalSupport support = new TerminalSupport(false, 80);
        StyledText st = new StyledText("hello", support);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        st.color(Color.GREEN).bold().print(new PrintStream(baos));
        assertEquals("hello", baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    void printWithAnsiOutputsEscapes() {
        TerminalSupport support = new TerminalSupport(true, 80);
        StyledText st = new StyledText("hi", support);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        st.color(Color.RED).bold().print(new PrintStream(baos));
        String out = baos.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("\u001B"));
        assertTrue(out.contains("hi"));
    }

    @Test
    void printlnAppendsNewline() {
        TerminalSupport support = new TerminalSupport(false, 80);
        StyledText st = new StyledText("x", support);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        st.println(new PrintStream(baos));
        assertEquals("x" + System.lineSeparator(), baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    void nullTextTreatedAsEmpty() {
        TerminalSupport support = new TerminalSupport(false, 80);
        StyledText st = new StyledText(null, support);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        st.print(new PrintStream(baos));
        assertEquals("", baos.toString(StandardCharsets.UTF_8));
    }
}
