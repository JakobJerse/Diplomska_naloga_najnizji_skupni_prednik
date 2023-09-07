import java.util.*;

public class TreeGenerator {

    // Tree generator
    private int currentLabel = 1;

    public TreeNode generateRandomTree(int maxDepth, int maxChildren, Map<Integer, TreeNode> nodeMap) {
        return generateRandomTree(0, maxDepth, maxChildren, nodeMap);
    }

    private TreeNode generateRandomTree(int currentDepth, int maxDepth, int maxChildren,
            Map<Integer, TreeNode> nodeMap) {
        if (currentDepth > maxDepth) {
            return null;
        }

        TreeNode node = new TreeNode(currentLabel++, currentDepth);
        nodeMap.put(node.getValue(), node);

        Random random = new Random();
        int numChildren = random.nextInt(maxChildren + 1);

        for (int i = 0; i < numChildren; i++) {
            TreeNode child = generateRandomTree(currentDepth + 1, maxDepth, maxChildren, nodeMap);
            if (child != null) {
                node.addChild(child);
            }
        }

        return node;
    }

    // Height of a tree
    public int height(TreeNode root) {
        if (root == null) {
            return -1;
        }

        int maxHeight = -1;

        for (TreeNode child : root.children) {
            int childHeight = height(child);
            maxHeight = Math.max(maxHeight, childHeight);
        }

        return maxHeight + 1;
    }

    // Print the tree
    public void printTree(TreeNode node) {

        int counter = 0;
        boolean newLine = false;

        if (node == null) {
            return;
        }

        int currentDepth = node.getDepth();

        Queue<Integer> numChildrenQueue = new ArrayDeque<>();
        numChildrenQueue.add(node.children.size());
        counter = numChildrenQueue.remove() + 1;

        Queue<TreeNode> nodeQueue = new ArrayDeque<>();
        nodeQueue.add(node);

        while (!nodeQueue.isEmpty()) {
            TreeNode current = nodeQueue.remove();
            if (current.getDepth() > currentDepth) {
                System.out.println();
                currentDepth = current.getDepth();
                newLine = true;
            }

            if (counter == 0 && current.getDepth() != 0) {
                if (newLine == false) {
                    System.out.print("| ");
                } else {
                    newLine = false;
                }
                counter = numChildrenQueue.remove();
                if (counter == 0) {
                    System.out.print(" NULL | ");
                    counter = numChildrenQueue.remove();
                }
            }

            System.out.print(current.getValue() + " ");
            counter--;

            for (TreeNode child : current.children) {
                nodeQueue.add(child);
                numChildrenQueue.add(child.children.size());
            }
        }
        System.out.println();
    }

}
