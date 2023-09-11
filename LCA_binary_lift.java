import java.util.*;

public class LCA_binary_lift {
    TreeNode root;
    TreeNode[][] ancestors;
    int numNodes;
    int numAncesstors;
    Map<Integer, TreeNode> nodeMap;

    public LCA_binary_lift(TreeNode root) {
        this.root = root;
        this.numNodes = UtilMethods.getNumberOfNoodes(root);
        this.numAncesstors = (int) Math.floor(Math.log(UtilMethods.height(root)) / Math.log(2));
        this.ancestors = new TreeNode[numNodes + 1][numAncesstors + 1];
        this.nodeMap = new HashMap<>();
        preprocess(root);
    }

    public TreeNode getLCA(int node1_value, int node2_value) {

        if (!nodeMap.containsKey(node1_value)) {
            throw new IllegalArgumentException("Node1 not found");
        } else if (!nodeMap.containsKey(node2_value)) {
            throw new IllegalArgumentException("Node2 not found");
        } else if (!nodeMap.containsKey(node1_value) && !nodeMap.containsKey(node2_value)) {
            throw new IllegalArgumentException("Both nodes not found");
        }

        TreeNode node1 = nodeMap.get(node1_value);
        TreeNode node2 = nodeMap.get(node2_value);

        if (node2.getDepth() > node1.getDepth()) {
            TreeNode[] swapedNodes = UtilMethods.swapNodes(node1, node2);
            node1 = swapedNodes[0];
            node2 = swapedNodes[1];
        }

        int depthDiff = node1.getDepth() - node2.getDepth();

        while (depthDiff != 0) {
            int largestBit = Integer.highestOneBit(depthDiff);
            int largestJumpIndex = (int) Math.floor(Math.log(largestBit) / Math.log(2));
            ;
            node1 = ancestors[node1.getValue()][largestJumpIndex];
            depthDiff -= largestBit;
        }

        if (node1 == node2) {
            return node1;
        }

        for (int i = numAncesstors; i >= 0; i--) {
            if (ancestors[node1.getValue()][i] != ancestors[node2.getValue()][i]) {
                node1 = ancestors[node1.getValue()][i];
                node2 = ancestors[node2.getValue()][i];
            }
        }

        return ancestors[node1.getValue()][0];
    }

    private void preprocess(TreeNode root) {
        if (root == null) {
            return;
        }

        nodeMap.put(root.getValue(), root);

        ancestors[root.getValue()][0] = root.parent;
        for (int i = 1; i <= numAncesstors; i++) {
            TreeNode ancestor = ancestors[root.getValue()][i - 1];

            if (ancestor == null) {
                break;
            }

            ancestors[root.getValue()][i] = ancestors[ancestor.getValue()][i - 1];
        }

        for (TreeNode child : root.children) {
            preprocess(child);
        }
    }
}
