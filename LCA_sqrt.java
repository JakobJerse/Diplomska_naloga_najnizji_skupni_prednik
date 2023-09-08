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
        this.blockSize = (int) Math.sqrt(utilMethods.height(root));
        this.jumpParents = new TreeNode[numNodes + 1];
        getJumpParents(root);
    }

    public TreeNode getLCA(int node1_value, int node2_value, Map<Integer, TreeNode> nodeMap) {

        if (!nodeMap.containsKey(node1_value)) {
            throw new IllegalArgumentException("Node1 not found");
        } else if (!nodeMap.containsKey(node2_value)) {
            throw new IllegalArgumentException("Node2 not found");
        } else if (!nodeMap.containsKey(node1_value) && !nodeMap.containsKey(node2_value)) {
            throw new IllegalArgumentException("Both nodes not found");
        }

        TreeNode node1 = nodeMap.get(node1_value);
        TreeNode node2 = nodeMap.get(node2_value);

        while (jumpParents[node1.getValue()] != jumpParents[node2.getValue()]) {
            if (node2.getDepth() > node1.getDepth()) {
                TreeNode[] swapedNodes = utilMethods.swapNodes(node1, node2);
                node1 = swapedNodes[0];
                node2 = swapedNodes[1];
            }
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

}
