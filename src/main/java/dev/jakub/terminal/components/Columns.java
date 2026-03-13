package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Multi-column layout. Use via {@link Terminal#columns()}.
 * Distributes 2–4 columns evenly across terminal width.
 */
public final class Columns {

    private static final int MAX_COLUMNS = 4;
    private static final int MIN_COLUMNS = 2;
    private static final String COL_SEP = "  ";

    private final TerminalSupport support;
    private final List<String> columnTexts = new ArrayList<>();

    public Columns(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Adds a column. Order is preserved. Supports 2–4 columns.
     */
    public Columns column(String text) {
        if (columnTexts.size() < MAX_COLUMNS) {
            columnTexts.add(text != null ? text : "");
        }
        return this;
    }

    /**
     * Prints columns side by side, evenly distributed across terminal width.
     */
    public void print(PrintStream out) {
        int n = Math.max(MIN_COLUMNS, Math.min(MAX_COLUMNS, columnTexts.size()));
        if (n == 0) return;
        int width = support.getWidth();
        int sepLen = COL_SEP.length() * (n - 1);
        int colWidth = Math.max(8, (width - sepLen) / n);

        List<List<String>> columnLines = new ArrayList<>();
        int maxLines = 0;
        for (String text : columnTexts) {
            if (columnLines.size() >= n) break;
            String[] lines = (text != null ? text : "").split("\\n", -1);
            List<String> padded = new ArrayList<>();
            for (String line : lines) {
                if (displayWidth(line) > colWidth) {
                    int cut = 0;
                    int dw = 0;
                    for (int i = 0; i < line.length() && dw < colWidth; i++) {
                        dw += (line.charAt(i) >= 0x1100 && Character.getType(line.charAt(i)) == Character.OTHER_LETTER) ? 2 : 1;
                        cut = i + 1;
                    }
                    line = line.substring(0, cut);
                }
                padded.add(padRight(line, colWidth));
            }
            columnLines.add(padded);
            if (padded.size() > maxLines) maxLines = padded.size();
        }
        while (columnLines.size() < n) {
            columnLines.add(new ArrayList<>());
        }

        for (int row = 0; row < maxLines; row++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < n; c++) {
                if (c > 0) sb.append(COL_SEP);
                List<String> lines = columnLines.get(c);
                String line = row < lines.size() ? lines.get(row) : padRight("", colWidth);
                sb.append(line);
            }
            out.println(sb.toString());
        }
    }

    private int displayWidth(String s) {
        int w = 0;
        for (int i = 0; i < s.length(); i++) {
            w += (s.charAt(i) >= 0x1100 && Character.getType(s.charAt(i)) == Character.OTHER_LETTER) ? 2 : 1;
        }
        return w;
    }

    private String padRight(String s, int width) {
        int w = displayWidth(s);
        if (w >= width) return s;
        return s + " ".repeat(width - w);
    }

    /**
     * Prints to stdout.
     */
    public void print() {
        print(System.out);
    }
}
