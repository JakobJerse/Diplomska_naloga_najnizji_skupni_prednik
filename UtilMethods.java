import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UtilMethods {

    public static int getNumberOfNoodes(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int counter = 1;

        for (TreeNode child : root.children) {
            counter += getNumberOfNoodes(child);
        }

        return counter;
    }

    public static int height(TreeNode root) {
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

    public static TreeNode[] swapNodes(TreeNode node1, TreeNode node2) {
        TreeNode temp = node1;
        node1 = node2;
        node2 = temp;

        return new TreeNode[] { node1, node2 };
    }

    public static void deleteExistingFiles(String[] fileNames) {
        for (String fileName : fileNames) {
            File file = new File(fileName);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Deleted existing file: " + fileName);
                } else {
                    System.out.println("Failed to delete file: " + fileName);
                }
            }
        }
    }

    public static void storeAverageTime(String fileName, float averageTime, int numberOfNodes) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName, true);
        fileWriter.write(averageTime + ", " + numberOfNodes + "\n");
        fileWriter.close();
    }

}
