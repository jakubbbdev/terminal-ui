package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ASCII bar chart / sparkline. Use via {@link Terminal#chart()}.
 * Vertical bars using ▁▂▃▄▅▆▇█; ASCII fallback: |. Auto-scale to height and width.
 */
public final class Chart {

    private static final String UTF8_BARS = " ▁▂▃▄▅▆▇█";
    private static final String ASCII_BAR = "|";

    private final TerminalSupport support;
    private final List<Double> data = new ArrayList<>();
    private int height = 5;

    public Chart(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Adds data points (varargs).
     */
    public Chart data(double... values) {
        if (values != null) {
            for (double v : values) data.add(v);
        }
        return this;
    }

    /**
     * Sets chart height in lines (default 5).
     */
    public Chart height(int h) {
        this.height = Math.max(1, Math.min(h, 20));
        return this;
    }

    /**
     * Prints the chart to the given stream.
     */
    public void print(PrintStream out) {
        if (data.isEmpty()) return;
        double min = data.stream().min(Double::compareTo).orElse(0.0);
        double max = data.stream().max(Double::compareTo).orElse(1.0);
        double range = max - min;
        if (range == 0) range = 1;
        boolean ascii = !support.isUtf8Symbols();
        int n = data.size();
        double[] barHeights = new double[n];
        for (int i = 0; i < n; i++) {
            barHeights[i] = ((data.get(i) - min) / range) * height;
        }
        for (int row = height; row >= 0; row--) {
            double yVal = min + (range * row) / height;
            String label = row == height ? String.format("%6.0f", max) : (row == 0 ? String.format("%6.0f", min) : String.format("%6.0f", yVal));
            StringBuilder line = new StringBuilder();
            line.append(label).append(" | ");
            for (int i = 0; i < n; i++) {
                boolean fill = barHeights[i] >= row;
                if (ascii) {
                    line.append(fill ? ASCII_BAR : " ");
                } else {
                    int idx = (int) Math.round((barHeights[i] / height) * (UTF8_BARS.length() - 1));
                    idx = Math.max(0, Math.min(idx, UTF8_BARS.length() - 1));
                    line.append(fill ? UTF8_BARS.charAt(idx) : " ");
                }
            }
            out.println(line.toString());
        }
    }

    /**
     * Prints to stdout.
     */
    public void print() {
        print(System.out);
    }
}
