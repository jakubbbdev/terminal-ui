package dev.jakub.terminal.live;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * Live dashboard that refreshes widgets at an interval. Use via {@link Terminal#dashboard()}.
 * Clears screen and re-renders. Thread-safe. Call {@link #stop()} to end.
 */
public final class Dashboard {

    private static final String CLEAR = "\u001B[2J\u001B[H";

    private final TerminalSupport support;
    private final List<Widget> widgets = new ArrayList<>();
    private long intervalMs = 1000;
    private PrintStream out = System.out;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Thread thread;

    public Dashboard(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Sets refresh interval (e.g. 1, SECONDS).
     */
    public Dashboard refreshEvery(long amount, TimeUnit unit) {
        this.intervalMs = unit.toMillis(amount);
        return this;
    }

    /**
     * Adds a widget. The supplier returns a component that has print(PrintStream); we capture its output.
     */
    public Dashboard widget(String title, Supplier<Object> widgetSupplier) {
        widgets.add(new Widget(title, widgetSupplier));
        return this;
    }

    /**
     * Starts the dashboard in a background thread. Stops with {@link #stop()}.
     */
    public void start() {
        if (!running.compareAndSet(false, true)) return;
        thread = new Thread(this::run, "terminal-ui-dashboard");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Stops the dashboard.
     */
    public void stop() {
        running.set(false);
        if (thread != null) {
            try {
                thread.join(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void run() {
        while (running.get()) {
            try {
                if (support.isAnsiEnabled()) {
                    out.print(CLEAR);
                }
                for (Widget w : widgets) {
                    if (w.title != null && !w.title.isEmpty()) {
                        out.println("--- " + w.title + " ---");
                    }
                    try {
                        Object comp = w.supplier.get();
                        if (comp != null) {
                            capturePrint(comp);
                        }
                    } catch (Exception e) {
                        out.println("Error: " + e.getMessage());
                    }
                    out.println();
                }
            } catch (Exception e) {
                out.println("Dashboard error: " + e.getMessage());
            }
            try {
                Thread.sleep(intervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void capturePrint(Object comp) throws Exception {
        java.lang.reflect.Method m = comp.getClass().getMethod("print", PrintStream.class);
        m.invoke(comp, out);
    }

    private static final class Widget {
        final String title;
        final Supplier<Object> supplier;

        Widget(String title, Supplier<Object> supplier) {
            this.title = title;
            this.supplier = supplier;
        }
    }
}
