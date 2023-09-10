import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class TreeGenerator {

    // Tree generator
    private int currentLabel = 1;

    public TreeNode generateRandomTree(int maxDepth, int maxChildren) {
        return generateRandomTree(0, maxDepth, maxChildren);
    }

    private TreeNode generateRandomTree(int currentDepth, int maxDepth, int maxChildren) {
        if (currentDepth > maxDepth) {
            return null;
        }

        TreeNode node = new TreeNode(currentLabel++, currentDepth);

        Random random = new Random();
        int numChildren = random.nextInt(maxChildren + 1);

        if (currentDepth == 0 && numChildren == 0) {
            numChildren = 1;
        }

        for (int i = 0; i < numChildren; i++) {
            TreeNode child = generateRandomTree(currentDepth + 1, maxDepth, maxChildren);
            if (child != null) {
                node.addChild(child);
            }
        }

        return node;
    }

    public TreeNode generateCompleteTree(int maxDepth, int numChildren) {
        return generateCompleteTree(0, maxDepth, numChildren);
    }

    private TreeNode generateCompleteTree(int currentDepth, int maxDepth, int numChildren) {
        if (currentDepth > maxDepth) {
            return null;
        }

        TreeNode node = new TreeNode(currentLabel++, currentDepth);

        for (int i = 0; i < numChildren; i++) {
            TreeNode child = generateCompleteTree(currentDepth + 1, maxDepth, numChildren);
            if (child != null) {
                node.addChild(child);
            }
        }

        return node;
    }

    public TreeNode generateSkewedTree(int maxDepth) {
        return generateSkewedTree(0, maxDepth);
    }

    private TreeNode generateSkewedTree(int currentDepth, int maxDepth) {
        if (currentDepth > maxDepth) {
            return null;
        }

        TreeNode node = new TreeNode(currentLabel++, currentDepth);

        if (currentDepth < maxDepth) {
            node.addChild(generateSkewedTree(currentDepth + 1, maxDepth));
        }

        return node;
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
}
