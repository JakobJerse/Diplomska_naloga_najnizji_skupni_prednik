import java.io.Serializable;
import java.util.*;

public class TreeNode implements Serializable {
    int value;
    int depth;
    TreeNode parent;
    List<TreeNode> children;

    public TreeNode(int value, int depth) {
        this.value = value;
        this.depth = depth;
        this.parent = null;
        this.children = new ArrayList<TreeNode>();
    }

    @Override
    public String toString() {
        return "Node=" + value;
    }

    public void addChild(TreeNode child) {
        this.children.add(child);
        child.parent = this;
    }

    public int getValue() {
        return this.value;
    }

    public int getDepth() {
        return this.depth;
    }
}