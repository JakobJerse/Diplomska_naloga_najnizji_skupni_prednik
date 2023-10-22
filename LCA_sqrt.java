
/*
 *  Implementation of LCA algorithm using square root decomposition technique
 *  Preprocesing complexity : O(n)
 *  Query complexity : O(sqrt(n))
 */

import java.util.*;

public class LCA_sqrt {

    private long preprocessTime;
    private long queryTime;

    TreeNode root;
    private int numNodes;
    private int blockSize;
    private TreeNode[] jumpParents;
    private Map<Integer, TreeNode> nodeMap;

    public LCA_sqrt(TreeNode root) {
        long preprocessStartTime = System.nanoTime();

        this.root = root;
        this.numNodes = UtilMethods.getNumberOfNoodes(root);
        this.blockSize = (int) Math.sqrt(UtilMethods.height(root));
        this.jumpParents = new TreeNode[numNodes + 1];
        this.nodeMap = new HashMap<>();
        getJumpParents(root);

        long preprocessEndTime = System.nanoTime();
        preprocessTime = preprocessEndTime - preprocessStartTime;
    }

    // LCA query
    public TreeNode getLCA(int node1_value, int node2_value) {

        long queryStartTime = System.nanoTime();

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
                TreeNode[] swapedNodes = UtilMethods.swapNodes(node1, node2);
                node1 = swapedNodes[0];
                node2 = swapedNodes[1];
            }
            node1 = jumpParents[node1.getValue()];
        }

        if (node2.getDepth() > node1.getDepth()) {
            TreeNode[] swapedNodes = UtilMethods.swapNodes(node1, node2);
            node1 = swapedNodes[0];
            node2 = swapedNodes[1];
        }

        while (node1.getDepth() > node2.getDepth()) {
            node1 = node1.parent;
        }

        while (node1 != node2) {
            node1 = node1.parent;
            node2 = node2.parent;
        }

        long queryEndTime = System.nanoTime();
        queryTime = queryEndTime - queryStartTime;

        return node1;

    }

    // precompute the jump parents for each node
    private void getJumpParents(TreeNode node) {
        if (node == null) {
            return;
        }
        nodeMap.put(node.getValue(), node);

        if (node.getDepth() % blockSize == 0) {
            jumpParents[node.getValue()] = node.parent;
        } else {
            jumpParents[node.getValue()] = jumpParents[node.parent.getValue()];
        }

        for (TreeNode child : node.children) {
            getJumpParents(child);
        }
    }

    public long getPreprocessTime() {
        return preprocessTime;
    }

    public long getQueryTime() {
        return queryTime;
    }

}
