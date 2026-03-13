package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Key-value list with aligned labels. Use via {@link Terminal#keyValue()}.
 */
public final class KeyValue {

    private final TerminalSupport support;
    private final List<String> keys = new ArrayList<>();
    private final List<String> values = new ArrayList<>();
    private String separator = ": ";
    private int labelWidth = -1;

    public KeyValue(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Adds a row (label, value).
     */
    public KeyValue row(String key, String value) {
        keys.add(key != null ? key : "");
        values.add(value != null ? value : "");
        return this;
    }

    /**
     * Sets the separator between key and value (default ": ").
     */
    public KeyValue separator(String sep) {
        this.separator = sep != null ? sep : "";
        return this;
    }

    /**
     * Sets a fixed width for the key column (auto if not set).
     */
    public KeyValue labelWidth(int width) {
        this.labelWidth = width;
        return this;
    }

    /**
     * Prints the key-value block to the given stream.
     */
    public void print(PrintStream out) {
        if (keys.isEmpty()) return;
        int kw = labelWidth >= 0 ? labelWidth : keys.stream().mapToInt(this::displayWidth).max().orElse(0);
        kw = Math.max(1, kw);
        for (int i = 0; i < keys.size(); i++) {
            String k = keys.get(i);
            String v = i < values.size() ? values.get(i) : "";
            int pad = kw - displayWidth(k);
            String padded = k + (pad > 0 ? " ".repeat(pad) : "");
            if (support.isAnsiEnabled()) {
                out.print(Ansi.BOLD + padded + Ansi.RESET + separator + v);
            } else {
                out.print(padded + separator + v);
            }
            out.println();
        }
    }

    /**
     * Prints to stdout.
     */
    public void print() {
        print(System.out);
    }

    private int displayWidth(String s) {
        int len = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            len += (c >= 0x1100 && Character.getType(c) == Character.OTHER_LETTER) ? 2 : 1;
        }
        return len;
    }
}
