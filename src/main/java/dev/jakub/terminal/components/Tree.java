package dev.jakub.terminal.components;

import dev.jakub.terminal.Terminal;
import dev.jakub.terminal.core.TerminalSupport;
import dev.jakub.terminal.internal.TreeNode;

import java.io.PrintStream;
import java.util.List;

/**
 * Tree view for directory-like structures. Use via {@link Terminal#tree()}.
 * Supports unlimited nesting. Use {@link #node(String)} for root, then
 * {@link TreeBuilder#child(String)} and {@link TreeBuilder#end()} to build.
 */
public final class Tree {

    private static final String UTF8_BRANCH = "├── ";
    private static final String UTF8_LAST   = "└── ";
    private static final String UTF8_PIPE   = "│   ";
    private static final String ASCII_BRANCH = "+-- ";
    private static final String ASCII_LAST   = "\\-- ";
    private static final String ASCII_PIPE   = "|   ";

    private final TerminalSupport support;
    private TreeNode root;

    public Tree(TerminalSupport support) {
        this.support = support;
    }

    /**
     * Sets the root node label and returns a builder for adding children.
     * Use {@link TreeBuilder#child(String)} to add children and {@link TreeBuilder#end()} to go back to parent.
     */
    public TreeBuilder node(String label) {
        this.root = new TreeNode(label);
        return new TreeBuilder(root, null, this);
    }

    /**
     * Prints the tree to the given stream.
     */
    public void print(PrintStream out) {
        if (root == null) return;
        boolean useAscii = !support.isUtf8Symbols();
        String branch = useAscii ? ASCII_BRANCH : UTF8_BRANCH;
        String last   = useAscii ? ASCII_LAST   : UTF8_LAST;
        String pipe   = useAscii ? ASCII_PIPE   : UTF8_PIPE;
        out.println(root.label);
        printChildren(out, root.children, "", branch, last, pipe);
    }

    /**
     * Prints the tree to stdout.
     */
    public void print() {
        print(System.out);
    }

    private void printChildren(PrintStream out, List<TreeNode> children, String prefix, String branch, String last, String pipe) {
        for (int i = 0; i < children.size(); i++) {
            TreeNode n = children.get(i);
            boolean isLast = (i == children.size() - 1);
            String conn = isLast ? last : branch;
            out.println(prefix + conn + n.label);
            String nextPrefix = prefix + (isLast ? "    " : pipe);
            printChildren(out, n.children, nextPrefix, branch, last, pipe);
        }
    }

    TerminalSupport getSupport() {
        return support;
    }

    TreeNode getRoot() {
        return root;
    }

    /**
     * Builder for adding children to a tree node. {@link #child(String)} adds a child and returns a builder for it;
     * {@link #end()} returns the parent builder (or the tree when at root).
     */
    public static final class TreeBuilder {
        private final TreeNode node;
        private final TreeBuilder parent;
        private final Tree tree;

        TreeBuilder(TreeNode node, TreeBuilder parent, Tree tree) {
            this.node = node;
            this.parent = parent;
            this.tree = tree;
        }

        /** Adds a child and returns a builder for that child (for nesting). */
        public TreeBuilder child(String label) {
            TreeNode child = new TreeNode(label);
            node.children.add(child);
            return new TreeBuilder(child, this, tree);
        }

        /** Returns the parent builder, or this when at root (so you can call {@link #child(String)} or {@link #print()}). */
        public TreeBuilder end() {
            return parent != null ? parent : this;
        }

        /** Prints the tree. */
        public void print(PrintStream out) {
            tree.print(out);
        }

        /** Prints the tree to stdout. */
        public void print() {
            tree.print();
        }
    }
}
