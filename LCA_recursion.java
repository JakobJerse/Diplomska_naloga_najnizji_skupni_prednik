
/*
 * Implementation of LCA (naive) algorithm using simple recursion
 * Preprocesing complexity : O(n)
 * Query complexity : O(n)
 */

import java.util.ArrayList;

public class LCA_recursion {

    // LCA query
    public TreeNode getLCA(TreeNode root, int node1_value, int node2_value) {

        ArrayList<TreeNode> path1 = new ArrayList<>();
        ArrayList<TreeNode> path2 = new ArrayList<>();

        boolean node1Found = findPath(root, node1_value, path1);
        boolean node2Found = findPath(root, node2_value, path2);

        if (!node1Found) {
            throw new IllegalArgumentException("Node1 not found");
        } else if (!node2Found) {
            throw new IllegalArgumentException("Node2 not found");
        } else if (!node1Found && !node2Found) {
            throw new IllegalArgumentException("Both nodes not found");
        }

        int LCA_index = 0;
        for (int i = 0; i < Math.min(path1.size(), path2.size()); i++) {
            if (path1.get(i) != path2.get(i)) {
                break;
            } else {
                LCA_index = i;
            }
        }

        return path1.get(LCA_index);
    }

    // find path from root to target node with recursion
    private boolean findPath(TreeNode current, int target, ArrayList<TreeNode> path) {
        if (current == null) {
            return false;
        }

        path.add(current);

        if (current.value == target) {
            return true;
        }

        for (TreeNode child : current.children) {
            if (findPath(child, target, path)) {
                return true;
            }
        }

        path.remove(path.size() - 1);
        return false;
    }
}
