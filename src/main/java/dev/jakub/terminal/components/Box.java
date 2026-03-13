package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Text in a box/frame. Use via {@link Terminal#box()}.
 */
public final class Box {

    private final TerminalSupport support;
    private final List<String> lines = new ArrayList<>();
    private String title = "";
    private String borderChar = "|";
    private String cornerChar = "+";
    private String horizontalChar = "-";

    public Box(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Sets an optional title (shown in the top border).
     */
    public Box title(String title) {
        this.title = title != null ? title : "";
        return this;
    }

    /**
     * Adds a line of content.
     */
    public Box line(String text) {
        lines.add(text != null ? text : "");
        return this;
    }

    /**
     * Adds multiple lines.
     */
    public Box lines(String... texts) {
        for (String t : texts) {
            lines.add(t != null ? t : "");
        }
        return this;
    }

    /**
     * Prints the box to the given stream.
     */
    public void print(PrintStream out) {
        int maxLen = lines.stream().mapToInt(this::displayWidth).max().orElse(0);
        if (title.length() > 0) {
            maxLen = Math.max(maxLen, displayWidth(title) + 2);
        }
        maxLen = Math.max(2, Math.min(maxLen, support.getWidth() - 4));
        int totalWidth = maxLen + 2;

        String topBorder = cornerChar + (horizontalChar.repeat(totalWidth - 2)) + cornerChar;
        if (title.length() > 0) {
            String t = " " + title + " ";
            int tw = displayWidth(t);
            if (tw <= totalWidth - 2) {
                topBorder = cornerChar + horizontalChar + t + horizontalChar.repeat(totalWidth - 2 - tw) + cornerChar;
            }
        }
        if (support.isAnsiEnabled()) {
            out.println(Ansi.BOLD + topBorder + Ansi.RESET);
        } else {
            out.println(topBorder);
        }
        for (String line : lines) {
            String padded = pad(line, maxLen);
            out.println(borderChar + " " + padded + " " + borderChar);
        }
        out.println(support.isAnsiEnabled() ? Ansi.BOLD + cornerChar + horizontalChar.repeat(totalWidth - 2) + cornerChar + Ansi.RESET : cornerChar + horizontalChar.repeat(totalWidth - 2) + cornerChar);
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

    private String pad(String s, int width) {
        int w = displayWidth(s);
        if (w >= width) return s;
        return s + " ".repeat(width - w);
    }
}
