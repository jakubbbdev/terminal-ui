package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;

/**
 * System info widget. Use via {@link Terminal#sysinfo()}.
 * OS, JVM, CPU, RAM from System.getProperty() and Runtime.getRuntime().
 */
public final class SysInfo {

    private final TerminalSupport support;

    public SysInfo(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Prints system info as a key-value block to the given stream.
     */
    public void print(PrintStream out) {
        Runtime rt = Runtime.getRuntime();
        long maxMem = rt.maxMemory() == Long.MAX_VALUE ? rt.totalMemory() : rt.maxMemory();
        long usedMem = rt.totalMemory() - rt.freeMemory();
        String os = System.getProperty("os.name", "?") + " " + System.getProperty("os.version", "");
        String jvm = System.getProperty("java.vm.name", "?") + " " + System.getProperty("java.version", "");
        int cores = rt.availableProcessors();
        String ram = formatBytes(usedMem) + " / " + formatBytes(maxMem) + " used";

        KeyValue kv = new KeyValue(support);
        kv.row("OS", os.trim());
        kv.row("JVM", jvm.trim());
        kv.row("CPU", cores + " cores");
        kv.row("RAM", ram);
        kv.print(out);
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }

    /**
     * Prints to stdout.
     */
    public void print() {
        print(System.out);
    }
}
