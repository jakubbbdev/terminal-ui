package dev.jakub.terminal.internal;

import dev.jakub.terminal.components.Tree;

import java.util.ArrayList;
import java.util.List;

/** Node for {@link Tree}. */
public final class TreeNode {
    public final String label;
    public final List<TreeNode> children = new ArrayList<>();

    public TreeNode(String label) {
        this.label = label != null ? label : "";
    }
}
