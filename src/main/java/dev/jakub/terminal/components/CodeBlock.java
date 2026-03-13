package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Code block with optional syntax highlighting. Use via {@link Terminal#code(String)}.
 * Box around content. Optional line numbers. Keywords for java, json, xml, bash.
 */
public final class CodeBlock {

    private static final Set<String> JAVA_KW = Set.of("public", "private", "protected", "class", "interface", "extends", "implements", "return", "void", "int", "long", "boolean", "if", "else", "for", "while", "new", "null", "true", "false", "import", "package", "static", "final", "String");

    private final TerminalSupport support;
    private final String language;
    private final List<String> lines = new ArrayList<>();
    private boolean lineNumbers;

    public CodeBlock(String language, TerminalSupport support) {
        this.language = language != null ? language.toLowerCase() : "";
        this.support = support;
    }

    /**
     * Adds a line of code.
     */
    public CodeBlock line(String text) {
        lines.add(text != null ? text : "");
        return this;
    }

    /**
     * Enables line numbers.
     */
    public CodeBlock lineNumbers() {
        this.lineNumbers = true;
        return this;
    }

    /**
     * Prints the code block to the given stream.
     */
    public void print(PrintStream out) {
        if (lines.isEmpty()) return;
        int numWidth = lineNumbers ? String.valueOf(lines.size()).length() : 0;
        int contentWidth = 0;
        for (String s : lines) contentWidth = Math.max(contentWidth, s.length());
        contentWidth = Math.min(contentWidth, support.getWidth() - 6 - numWidth);
        int totalWidth = numWidth + contentWidth + 4;
        String border = "+" + "-".repeat(totalWidth - 2) + "+";
        if (support.isAnsiEnabled()) out.println(Ansi.BOLD + border + Ansi.RESET);
        for (int i = 0; i < lines.size(); i++) {
            String raw = lines.get(i);
            String content = raw.length() > contentWidth ? raw.substring(0, contentWidth) : raw;
            content = highlight(content, language);
            String numPart = lineNumbers ? padLeft(String.valueOf(i + 1), numWidth) + " | " : "";
            out.println("| " + numPart + padRight(content, contentWidth) + " |");
        }
        if (support.isAnsiEnabled()) out.println(Ansi.BOLD + border + Ansi.RESET);
        else out.println(border);
    }

    private String highlight(String line, String lang) {
        if (!support.isAnsiEnabled()) return line;
        if ("java".equals(lang)) {
            for (String kw : JAVA_KW) {
                line = line.replaceAll("\\b(" + Pattern.quote(kw) + ")\\b", Ansi.FG_BLUE + "$1" + Ansi.RESET);
            }
        }
        return line;
    }

    private static String padLeft(String s, int w) {
        return s.length() >= w ? s : " ".repeat(w - s.length()) + s;
    }

    private static String padRight(String s, int w) {
        return s.length() >= w ? s : s + " ".repeat(w - s.length());
    }

    /**
     * Prints to stdout.
     */
    public void print() {
        print(System.out);
    }
}
