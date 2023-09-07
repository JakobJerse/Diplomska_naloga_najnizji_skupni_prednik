import java.util.*;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UtilMethods utilMethods = new UtilMethods();
        Map<Integer, TreeNode> nodeMap = new HashMap<>();

        TreeGenerator treeGenerator = new TreeGenerator();
        TreeNode root = treeGenerator.generateRandomTree(4, 3, nodeMap); // Example: depth 3, max 4 children
        LCA_recursion lca = new LCA_recursion();
        LCA_sqrt lca2 = new LCA_sqrt(root);

        System.out.println("Chosen tree: ");

        treeGenerator.printTree(root);
        System.out.println("Depth of the tree: " + utilMethods.height(root));

        System.out.println(
                "Enter the values \u200B\u200Bof the nodes for which you want to find the lowest common ancestor: ");
        int node1 = sc.nextInt();
        int node2 = sc.nextInt();
        sc.close();

        System.out.println("LCA1 of nodes " + node1 + " and " + node2 + " is : " + lca.getLCA(root, node1, node2));
        System.out.println("LCA2 of nodes " + node1 + " and " + node2 + " is : " + lca2.getLCA(node1, node2, nodeMap));
        System.out.println("Finished.");

    }
}
