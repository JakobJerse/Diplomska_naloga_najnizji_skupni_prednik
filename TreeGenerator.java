import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class TreeGenerator {

    // Tree generator
    private int currentLabel = 1;
    Random random = new Random();

    public TreeNode generateRandomTree(int depth, int maxChildren) {
        this.currentLabel = 1;
        return generateRandomTree(0, depth, maxChildren);
    }

    private TreeNode generateRandomTree(int currentDepth, int depth, int maxChildren) {
        if (currentDepth > depth) {
            return null;
        }

        TreeNode node = new TreeNode(currentLabel++, currentDepth);

        int numChildren = (currentDepth == depth) ? 0 : random.nextInt(maxChildren) + 1;

        for (int i = 0; i < numChildren; i++) {
            TreeNode child = generateRandomTree(currentDepth + 1, depth, maxChildren);
            if (child != null) {
                node.addChild(child);
            }
        }

        return node;
    }

    public TreeNode generateCompleteTree(int depth, int numChildren) {
        this.currentLabel = 1;
        return generateCompleteTree(0, depth, numChildren);
    }

    private TreeNode generateCompleteTree(int currentDepth, int depth, int numChildren) {
        if (currentDepth > depth) {
            return null;
        }

        TreeNode node = new TreeNode(currentLabel++, currentDepth);

        for (int i = 0; i < numChildren; i++) {
            TreeNode child = generateCompleteTree(currentDepth + 1, depth, numChildren);
            if (child != null) {
                node.addChild(child);
            }
        }

        return node;
    }

    public TreeNode generateSkewedTree(int depth) {
        this.currentLabel = 1;
        return generateSkewedTree(0, depth);
    }

    private TreeNode generateSkewedTree(int currentDepth, int depth) {
        if (currentDepth > depth) {
            return null;
        }

        TreeNode node = new TreeNode(currentLabel++, currentDepth);

        if (currentDepth < depth) {
            node.addChild(generateSkewedTree(currentDepth + 1, depth));
        }

        return node;
    }

    public void saveTreeToFile(TreeNode root, String filename) {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
                ObjectOutputStream outputStream = new ObjectOutputStream(fileOut)) {
            outputStream.writeObject(root);
            outputStream.close();
        } catch (IOException ioe) {
            System.err.println("IOException");
            ioe.printStackTrace();
        }
    }

    public TreeNode loadTreeFromFile(String filename) {
        TreeNode root = null;
        try (FileInputStream fileIn = new FileInputStream(filename);
                ObjectInputStream inputStream = new ObjectInputStream(fileIn)) {
            root = (TreeNode) inputStream.readObject();
            inputStream.close();
        } catch (IOException | ClassNotFoundException cnfe) {
            System.err.println("TreeNode class not found");
            cnfe.printStackTrace();
        }
        return root;
    }

    public void generateCompleteTreeTestSet(int maxDepth, int step, int numChildren) {
        for (int i = step; i <= maxDepth; i += step) {
            UtilMethods.deleteExistingFiles(new String[] { "test_trees/complete_trees/complete_tree_" + i + ".ser" });
            currentLabel = 1;
            TreeNode root = generateCompleteTree(i, numChildren);
            String filename = "test_trees/complete_trees/complete_tree_" + i + ".ser";
            saveTreeToFile(root, filename);
        }
    }

    public void generateRandomTreeTestSet(int maxDepth, int step, int maxChildren) {
        for (int i = 19; i <= 19; i += step) {
            UtilMethods.deleteExistingFiles(new String[] { "test_trees/random_trees/random_tree_" + i + ".ser" });
            currentLabel = 1;
            TreeNode root = null;
            if (i <= 10) {
                root = generateRandomTree(i, maxChildren + 1);
            } else {
                root = generateRandomTree(i, maxChildren);
            }
            System.out.println("Number of nodes: " + UtilMethods.getNumberOfNoodes(root));
            String filename = "test_trees/random_trees/random_tree_" + i + ".ser";
            saveTreeToFile(root, filename);
        }

    }

    public void generateSkewedTreeTestSet(int maxDepth, int step) {
        for (int i = step; i <= maxDepth; i += step) {
            UtilMethods.deleteExistingFiles(new String[] { "test_trees/skewed_trees/skewed_tree_" + i + ".ser" });
            currentLabel = 1;
            TreeNode root = generateSkewedTree(i);
            String filename = "test_trees/skewed_trees/skewed_tree_" + i + ".ser";
            saveTreeToFile(root, filename);
        }
    }

    public static void main(String[] args) {
        System.out.println("Would you like to generate new test sets? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        if (choice.equals("y")) {
            System.out.println("Generating new test sets...");
            TreeGenerator treeGenerator = new TreeGenerator();
            treeGenerator.generateCompleteTreeTestSet(19, 1, 2);
            treeGenerator.generateSkewedTreeTestSet(1000000, 100000);
            treeGenerator.generateRandomTreeTestSet(19, 1, 3);

        } else {
            System.out.println("Test sets were not genereated.");
            System.out.println();
        }
    }
}
