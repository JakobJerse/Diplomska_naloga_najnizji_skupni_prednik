import java.util.*;

public class LCA_sqrt {
    TreeNode root;
    int numNodes;
    int blockSize;
    TreeNode[] jumpParents;
    UtilMethods utilMethods = new UtilMethods();

    public LCA_sqrt(TreeNode root) {
        this.root = root;
        this.numNodes = utilMethods.getNumberOfNoodes(root);
        this.blockSize = (int) Math.floor(Math.sqrt(numNodes));
        this.jumpParents = new TreeNode[numNodes + 1];
        // preprocess
        getJumpParents(root);
    }

    public TreeNode getLCA(int node1_value, int node2_value, Map<Integer, TreeNode> nodeMap) {
        TreeNode node1 = nodeMap.get(node1_value);
        TreeNode node2 = nodeMap.get(node2_value);

        if (node2.getDepth() > node1.getDepth()) {
            TreeNode[] swapedNodes = swapNodes(node1, node2);
            node1 = swapedNodes[0];
            node2 = swapedNodes[1];
        }

        while (jumpParents[node1.getValue()] != jumpParents[node2.getValue()]) {
            node1 = jumpParents[node1.getValue()];
        }

        while (node1.getDepth() > node2.getDepth()) {
            node1 = node1.parent;
        }

        while (node1 != node2) {
            node1 = node1.parent;
            node2 = node2.parent;
        }

        return node1;

    }

    private void getJumpParents(TreeNode node) {
        if (node == null) {
            return;
        }

        if (node.getDepth() % blockSize == 0) {
            jumpParents[node.getValue()] = node.parent;
        } else {
            jumpParents[node.getValue()] = jumpParents[node.parent.getValue()];
        }

        for (TreeNode child : node.children) {
            getJumpParents(child);
        }
    }

    private TreeNode[] swapNodes(TreeNode node1, TreeNode node2) {
        TreeNode temp = node1;
        node1 = node2;
        node2 = temp;

        return new TreeNode[] { node1, node2 };
    }
}
