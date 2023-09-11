import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        TreeGenerator treeGenerator = new TreeGenerator();

        // TreeNode root = treeGenerator.generateRandomTree(4, 2);
        // treeGenerator.printTree(root);

        System.out.println("---------------------");

        for (int i = 1; i <= 10; i++) {
            String filename = "test_trees/tree_" + i + ".ser";
            TreeNode root = treeGenerator.loadTreeFromFile(filename);

            LCA_recursion lca = new LCA_recursion();
            LCA_sqrt lca2 = new LCA_sqrt(root);
            LCA_binary_lift lca3 = new LCA_binary_lift(root);

            System.out.println("Tree " + i + ": ");
            treeGenerator.printTree(root);
            System.out.println("Enter the values of the nodes for which you want to find the lowest common ancestor: ");
            int node1 = sc.nextInt();
            int node2 = sc.nextInt();

            System.out.println("LCA1 of nodes " + node1 + " and " + node2 + " is : " +
                    lca.getLCA(root, node1, node2));
            System.out.println("LCA2 of nodes " + node1 + " and " + node2 + " is : " +
                    lca2.getLCA(node1, node2));
            System.out.println("LCA3 of nodes " + node1 + " and " + node2 + " is : " +
                    lca3.getLCA(node1, node2));

            System.out.println("---------------------");
        }
        sc.close();
        System.out.println("Finished.");

    }
}
