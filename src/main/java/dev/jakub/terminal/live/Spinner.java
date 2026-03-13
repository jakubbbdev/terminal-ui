package dev.jakub.terminal.live;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe spinner for indeterminate progress. Use via {@link Terminal#spinner()}.
 */
public final class Spinner {

    /** Braille-style frames (UTF-8). */
    private static final String[] FRAMES_UTF8 = {"⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏"};
    /** ASCII fallback for Windows CMD / no UTF-8. */
    private static final String[] FRAMES_ASCII = {"|", "/", "-", "\\"};

    private final String message;
    private final TerminalSupport support;
    private final PrintStream out;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ReentrantLock lock = new ReentrantLock();
    private Thread thread;
    private int frameIndex;

    public Spinner(String message, TerminalSupport support, PrintStream out) {
        this.message = message != null ? message : "";
        this.support = support;
        this.out = out != null ? out : System.out;
    }

    /**
     * Starts the spinner in a background thread. Idempotent.
     */
    public Spinner start() {
        if (!running.compareAndSet(false, true)) {
            return this;
        }
        thread = new Thread(this::run, "terminal-ui-spinner");
        thread.setDaemon(true);
        thread.start();
        return this;
    }

    public void stop(String message) {
        if (!running.compareAndSet(true, false)) {
            return;
        }
        if (thread != null) {
            try {
                thread.join(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        lock.lock();
        try {
            out.print(Ansi.CARRIAGE_RETURN);
            if (support.isAnsiEnabled()) {
                out.print(Ansi.ERASE_LINE);
            }
            if (message != null && !message.isEmpty()) {
                out.println(message);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Stops the spinner without a final message.
     */
    public void stop() {
        stop(null);
    }

    private void run() {
        String[] frames = support.isUtf8Symbols() ? FRAMES_UTF8 : FRAMES_ASCII;
        while (running.get()) {
            lock.lock();
            try {
                String frame = frames[frameIndex % frames.length];
                frameIndex++;
                out.print(Ansi.CARRIAGE_RETURN);
                if (support.isAnsiEnabled()) {
                    out.print(Ansi.ERASE_LINE);
                }
                out.print(frame + " " + message);
            } finally {
                lock.unlock();
            }
            try {
                Thread.sleep(support.isAnsiEnabled() ? 80 : 120);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
