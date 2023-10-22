import java.util.*;
import java.io.*;

public class Main {

        public static void main(String[] args) {

                Scanner sc = new Scanner(System.in);
                TreeGenerator treeGenerator = new TreeGenerator();

                System.out.println("---------------------");

                try {
                        FileWriter preprocessWriter = new FileWriter("preprocessTimeResults.txt", true); // Open in
                                                                                                         // append mode
                        FileWriter queWriter = new FileWriter("queryTimeResults.txt", true); // Open in append mode

                        for (int i = 1; i <= 10; i++) {
                                String filename = "test_trees/tree_" + i + ".ser";
                                TreeNode root = treeGenerator.loadTreeFromFile(filename);

                                LCA_recursion lca = new LCA_recursion();
                                LCA_sqrt lca2 = new LCA_sqrt(root);
                                LCA_binary_lift lca3 = new LCA_binary_lift(root);
                                LCA_RMQ lca4 = new LCA_RMQ(root);
                                LCA_FCB lca5 = new LCA_FCB(root);

                                System.out.println("Tree " + i + ": ");
                                System.out.println("Number of nodes: " + UtilMethods.getNumberOfNoodes(root));
                                treeGenerator.printTree(root);
                                System.out.println(
                                                "Enter the values of the nodes for which you want to find the lowest common ancestor: ");
                                int node1 = sc.nextInt();
                                int node2 = sc.nextInt();

                                System.out.println("LCA1 of nodes " + node1 + " and " + node2 + " is : " +
                                                lca.getLCA(root, node1, node2));
                                System.out.println("LCA2 of nodes " + node1 + " and " + node2 + " is : " +
                                                lca2.getLCA(node1, node2));
                                System.out.println("LCA3 of nodes " + node1 + " and " + node2 + " is : " +
                                                lca3.getLCA(node1, node2));
                                System.out.println("LCA4 of nodes " + node1 + " and " + node2 + " is : " +
                                                lca4.getLCA(node1, node2));
                                System.out.println("LCA5 of nodes " + node1 + " and " + node2 + " is : " +
                                                lca5.getLCA(node1, node2));

                                System.out.println("---------------------");

                                // Print the results to a file
                                preprocessWriter.write(i + ".tree with " + UtilMethods.getNumberOfNoodes(root)
                                                + " nodes results \n");
                                preprocessWriter.write(lca2.getPreprocessTime() + "\n");
                                preprocessWriter.write(lca3.getPreprocessTime() + "\n");
                                preprocessWriter.write(lca4.getPreprocessTime() + "\n");
                                preprocessWriter.write(lca5.getPreprocessTime() + "\n");
                                queWriter.write(i + ".tree with  " + UtilMethods.getNumberOfNoodes(root)
                                                + " nodes results \n");
                                queWriter.write(lca.getQueryTime() + "\n");
                                queWriter.write(lca2.getQueryTime() + "\n");
                                queWriter.write(lca3.getQueryTime() + "\n");
                                queWriter.write(lca4.getQueryTime() + "\n");
                                queWriter.write(lca5.getQueryTime() + "\n");

                                System.out.println("Successfully wrote to the files for tree " + i);
                        }

                        preprocessWriter.close();
                        queWriter.close();

                } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                }

                sc.close();
                System.out.println("Finished.");

        }

}
