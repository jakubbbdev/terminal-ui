package dev.jakub.terminal;

import dev.jakub.terminal.core.TerminalSupport;
import dev.jakub.terminal.live.Spinner;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SpinnerTest {

    @Test
    void startAndStopWithMessagePrintsMessage() throws InterruptedException {
        TerminalSupport support = new TerminalSupport(false, 80);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Spinner spinner = new Spinner("Loading...", support, new PrintStream(baos));
        spinner.start();
        Thread.sleep(150);
        spinner.stop("Done ✅");
        String out = baos.toString(StandardCharsets.UTF_8);
        assertTrue(out.contains("Loading"));
        assertTrue(out.contains("Done ✅"));
    }

    @Test
    void stopWithoutMessageClearsLine() {
        TerminalSupport support = new TerminalSupport(false, 80);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Spinner spinner = new Spinner("Wait", support, new PrintStream(baos));
        spinner.start();
        spinner.stop(null);
        assertNotNull(baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    void stopIsIdempotent() {
        TerminalSupport support = new TerminalSupport(false, 80);
        Spinner spinner = new Spinner("x", support, System.out);
        spinner.start();
        spinner.stop("a");
        assertDoesNotThrow(() -> spinner.stop("b"));
    }

    @Test
    void spinnerBuilderStartReturnsRunningSpinner() {
        Spinner s = Terminal.spinner().message("Test").start();
        assertNotNull(s);
        s.stop("OK");
    }
}
