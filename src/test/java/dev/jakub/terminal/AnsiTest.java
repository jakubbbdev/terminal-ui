package dev.jakub.terminal;

import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnsiTest {

    @Test
    void resetIsEscapeSequence() {
        assertTrue(Ansi.RESET.startsWith("\u001B["));
        assertTrue(Ansi.RESET.endsWith("m"));
    }

    @Test
    void boldIsEscapeSequence() {
        assertTrue(Ansi.BOLD.startsWith("\u001B["));
    }

    @Test
    void allColorsHaveCode() {
        for (Color c : Color.values()) {
            assertNotNull(c.ansiCode());
            assertTrue(c.ansiCode().startsWith("\u001B["));
        }
    }

    @Test
    void cursorAndEraseCodesPresent() {
        assertNotNull(Ansi.HIDE_CURSOR);
        assertNotNull(Ansi.SHOW_CURSOR);
        assertNotNull(Ansi.CURSOR_UP);
        assertNotNull(Ansi.CARRIAGE_RETURN);
        assertNotNull(Ansi.ERASE_LINE);
    }
}
