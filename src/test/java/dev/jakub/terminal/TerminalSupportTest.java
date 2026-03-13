package dev.jakub.terminal;

import dev.jakub.terminal.core.TerminalSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerminalSupportTest {

    @Test
    void explicitSupportRespectsAnsiAndWidth() {
        TerminalSupport support = new TerminalSupport(true, 120);
        assertTrue(support.isAnsiEnabled());
        assertEquals(120, support.getWidth());
    }

    @Test
    void widthIsClampedToMinimum() {
        TerminalSupport support = new TerminalSupport(false, 5);
        assertTrue(support.getWidth() >= 10);
    }

    @Test
    void defaultSupportHasReasonableWidth() {
        TerminalSupport support = new TerminalSupport();
        assertTrue(support.getWidth() >= 10);
        assertTrue(support.getWidth() <= 1000);
    }
}
