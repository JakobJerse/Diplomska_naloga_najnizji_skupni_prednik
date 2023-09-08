public class UtilMethods {

    public int getNumberOfNoodes(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int counter = 1;

        for (TreeNode child : root.children) {
            counter += getNumberOfNoodes(child);
        }

        return counter;
    }

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

    public TreeNode[] swapNodes(TreeNode node1, TreeNode node2) {
        TreeNode temp = node1;
        node1 = node2;
        node2 = temp;

        return new TreeNode[] { node1, node2 };
    }
}
