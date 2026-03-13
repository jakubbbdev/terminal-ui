package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.Ansi;
import dev.jakub.terminal.core.TerminalSupport;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Table of contents. Use via {@link Terminal#toc()}.
 * Optional sub-sections. Renders with numbers and rule.
 */
public final class TableOfContents {

    private final TerminalSupport support;
    private final List<Section> sections = new ArrayList<>();
    private static final String TITLE = "Table of Contents";

    public TableOfContents(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Adds a section. Returns a sub-builder for optional sub-sections.
     */
    public TocSection section(String title) {
        Section s = new Section(title != null ? title : "", new ArrayList<>());
        sections.add(s);
        return new TocSection(s, this);
    }

    /**
     * Prints the TOC to the given stream.
     */
    public void print(PrintStream out) {
        if (support.isAnsiEnabled()) out.println(Ansi.BOLD + TITLE + Ansi.RESET);
        else out.println(TITLE);
        String ruleChar = support.isUtf8Symbols() ? "─" : "-";
        out.println(ruleChar.repeat(Math.min(TITLE.length(), support.getWidth())));
        int num = 1;
        for (Section s : sections) {
            out.println("  " + num + ". " + s.title);
            for (String sub : s.subs) {
                out.println("      - " + sub);
            }
            num++;
        }
    }

    /**
     * Prints to stdout.
     */
    public void print() {
        print(System.out);
    }

    static final class Section {
        final String title;
        final List<String> subs;

        Section(String title, List<String> subs) {
            this.title = title;
            this.subs = subs;
        }
    }

    /**
     * Builder for adding sub-sections to a TOC section.
     */
    public static final class TocSection {
        private final Section section;
        private final TableOfContents toc;

        TocSection(Section section, TableOfContents toc) {
            this.section = section;
            this.toc = toc;
        }

        /**
         * Adds a sub-section.
         */
        public TocSection sub(String title) {
            section.subs.add(title != null ? title : "");
            return this;
        }

        /**
         * Adds another top-level section. Returns builder for that section.
         */
        public TocSection section(String title) {
            return toc.section(title);
        }

        /**
         * Prints the TOC.
         */
        public void print(PrintStream out) {
            toc.print(out);
        }

        /**
         * Prints to stdout.
         */
        public void print() {
            toc.print();
        }
    }
}
