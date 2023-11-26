import java.util.*;
import java.io.*;

public class Main {

        public static void main(String[] args) {

                try {
                        FileWriter fileWriter = new FileWriter("results_skewed_trees.txt", true);

                        TreeGenerator treeGenerator = new TreeGenerator();
                        // treeGenerator.generateCompleteTreeTestSet(10, 3);
                        // treeGenerator.generateRandomTreeTestSet(10, 4);
                        // treeGenerator.generateSkewedTreeTestSet(10);

                        for (int i = 1; i <= 10; i++) {
                                TreeNode root = treeGenerator.loadTreeFromFile(
                                                "test_trees/skewed_trees/skewed_tree_with_height_" + i + ".ser");
                                int height = UtilMethods.height(root);
                                LCA_recursion lca = new LCA_recursion();
                                LCA_sqrt lca2 = new LCA_sqrt(root);
                                LCA_binary_lift lca3 = new LCA_binary_lift(root);
                                LCA_RMQ lca4 = new LCA_RMQ(root);
                                LCA_FCB lca5 = new LCA_FCB(root);

                                System.out.println("Tree " + i + " with height " + height + ": ");
                                System.out.println("Number of nodes: " + UtilMethods.getNumberOfNoodes(root));

                                for (int j = 0; j < 100; j++) {
                                        // get 2 random numbers between 1 and num of nodes
                                        Random random = new Random();
                                        int node1 = random.nextInt(UtilMethods.getNumberOfNoodes(root)) + 1;
                                        int node2 = random.nextInt(UtilMethods.getNumberOfNoodes(root)) + 1;

                                        int result1 = lca.getLCA(root, node1, node2).value;
                                        int result2 = lca2.getLCA(node1, node2).value;
                                        int result3 = lca3.getLCA(node1, node2).value;
                                        int result4 = lca4.getLCA(node1, node2).value;
                                        int result5 = lca5.getLCA(node1, node2).value;

                                        if (result1 == result2 && result1 == result3 && result1 == result4
                                                        && result1 == result5) {
                                                // Print the results to a file for each query
                                                fileWriter.write("Tree " + i + ", Height " + height + ", Query ("
                                                                + node1 + ", " + node2 + ")\n");
                                                fileWriter.write("LCA: " + result1 + "\n");
                                        } else {
                                                System.out.println("Results do not match for query (" + node1 + ", "
                                                                + node2 + ")");
                                                System.out.println("LCA1: " + result1);
                                                System.out.println("LCA2: " + result2);
                                                System.out.println("LCA3: " + result3);
                                                System.out.println("LCA4: " + result4);
                                                System.out.println("LCA5: " + result5);
                                                throw new RuntimeException("Results do not match.");
                                        }
                                }

                                System.out.println("Successfully tested tree " + i + " with height " + height);
                        }

                        fileWriter.close();

                } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                }

                System.out.println("Finished.");
        }
}
