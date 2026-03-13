package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 2D heatmap: values mapped to intensity blocks. Use via {@link Terminal#heatmap()}.
 * UTF-8: space, ░, ▒, ▓, █. ASCII fallback: ., +, # etc. Auto-scale to max value.
 */
public final class Heatmap {

    private static final String[] UTF8_BLOCKS = {" ", "░", "▒", "▓", "█"};
    private static final String[] ASCII_BLOCKS = {".", "-", "+", "#", "@"};

    private final TerminalSupport support;
    private final List<Row> rows = new ArrayList<>();
    private int globalMax = -1;

    public Heatmap(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Adds a row with label and numeric values.
     */
    public Heatmap row(String label, int... values) {
        if (label == null) label = "";
        int[] v = values != null ? values : new int[0];
        for (int x : v) {
            if (x > globalMax) globalMax = x;
        }
        rows.add(new Row(label, v));
        return this;
    }

    /**
     * Prints the heatmap to the given stream.
     */
    public void print(PrintStream out) {
        if (rows.isEmpty()) return;
        int maxVal = globalMax <= 0 ? 1 : globalMax;
        boolean ascii = !support.isUtf8Symbols();
        String[] blocks = ascii ? ASCII_BLOCKS : UTF8_BLOCKS;
        int labelWidth = 0;
        for (Row r : rows) {
            if (r.label.length() > labelWidth) labelWidth = r.label.length();
        }
        labelWidth = Math.max(3, labelWidth);
        for (Row r : rows) {
            StringBuilder sb = new StringBuilder();
            String pad = r.label.length() < labelWidth ? " ".repeat(labelWidth - r.label.length()) : "";
            sb.append(pad).append(r.label).append(" ");
            for (int i = 0; i < r.values.length; i++) {
                int idx = (int) Math.round((r.values[i] * (blocks.length - 1)) / (double) maxVal);
                idx = Math.max(0, Math.min(idx, blocks.length - 1));
                sb.append(blocks[idx]);
            }
            out.println(sb.toString());
        }
    }

    /**
     * Prints to stdout.
     */
    public void print() {
        print(System.out);
    }

    private static final class Row {
        final String label;
        final int[] values;

        Row(String label, int[] values) {
            this.label = label;
            this.values = values;
        }
    }
}
