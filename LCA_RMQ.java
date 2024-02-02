
/*
 *  Implementation of LCA using reduction to RMQ technique
 *  Preprocesing complexity : O(N log N)
 *  Query complexity : O(1)
 */

import java.util.*;

public class LCA_RMQ {

    private long preprocessTime;

    private TreeNode root;
    private int numNodes;
    private int p;
    private ArrayList<TreeNode> eulerTourArray;
    private ArrayList<Integer> depthArray;
    private int[] firstAppearanceIndex;
    private int[] log2Array;
    private int[] pow2Array;
    private int size;
    private int[][] sparseTable;

    public LCA_RMQ(TreeNode root) {
        long preprocessStartTime = System.nanoTime();

        this.root = root;
        this.numNodes = UtilMethods.getNumberOfNoodes(root);
        this.size = 2 * numNodes - 1;
        this.p = (int) (Math.log(size) / Math.log(2));
        this.eulerTourArray = new ArrayList<>();
        this.depthArray = new ArrayList<>();
        this.firstAppearanceIndex = new int[numNodes + 1];
        this.log2Array = new int[size + 1];
        this.pow2Array = new int[size + 1];
        this.sparseTable = new int[p + 1][size];

        this.buildHelperArrays();
        this.dfs(this.root);
        this.buildSparseTable();

        long preprocessEndTime = System.nanoTime();
        this.preprocessTime = preprocessEndTime - preprocessStartTime;
    }

    // construct the log2 and pow2 arrays
    private void buildHelperArrays() {
        pow2Array[0] = 1;
        for (int i = 1; i <= this.size; i++) {
            log2Array[i] = (int) (Math.log(i) / Math.log(2));
            pow2Array[i] = 1 << i;
        }

    }

    // perform depth first search on the tree and fills the eulerTourArray and the
    // depthArray
    private void dfs(TreeNode root) {

        if (root == null) {
            return;
        }

        eulerTourArray.add(root);
        depthArray.add(root.getDepth());

        if (firstAppearanceIndex[root.getValue()] == 0) {
            firstAppearanceIndex[root.getValue()] = eulerTourArray.size() - 1;
        }

        for (TreeNode child : root.children) {
            dfs(child);
            eulerTourArray.add(root);
            depthArray.add(root.getDepth());
        }

    }

    // build the sparse table using dynamic programming
    private void buildSparseTable() {

        for (int i = 0; i < depthArray.size(); i++) {
            sparseTable[0][i] = i;
        }

        for (int i = 1; i <= p; i++) {
            for (int j = 0; j + pow2Array[i] <= eulerTourArray.size(); j++) {
                int leftIndex = sparseTable[i - 1][j];
                int rightIndex = sparseTable[i - 1][j + pow2Array[i - 1]];
                sparseTable[i][j] = depthArray.get(leftIndex) <= depthArray.get(rightIndex) ? leftIndex : rightIndex;
            }
        }
    }

    // query the sparse table
    private int query(int left, int right) {

        int length = right - left + 1;
        int k = log2Array[length];
        int leftIntervalMinValueIndex = sparseTable[k][left];
        int rightIntervalMinValueIndex = sparseTable[k][right - pow2Array[k] + 1];

        return depthArray.get(leftIntervalMinValueIndex) <= depthArray.get(rightIntervalMinValueIndex)
                ? leftIntervalMinValueIndex
                : rightIntervalMinValueIndex;

    }

    // LCA query
    public TreeNode getLCA(int node1_value, int node2_value) {
        // handle invalid inputs
        if (node1_value < 1 || node1_value > numNodes) {
            throw new IllegalArgumentException("Node1 not found");
        } else if (node2_value < 1 || node2_value > numNodes) {
            throw new IllegalArgumentException("Node2 not found");
        } else if (node1_value < 1 || node1_value > numNodes && node2_value < 1 || node2_value > numNodes) {
            throw new IllegalArgumentException("Both nodes not found");
        }

        int left = Math.min(firstAppearanceIndex[node1_value], firstAppearanceIndex[node2_value]);
        int right = Math.max(firstAppearanceIndex[node1_value], firstAppearanceIndex[node2_value]);
        int lcaIndex = query(left, right);

        return eulerTourArray.get(lcaIndex);

    }

    public long getPreprocessTime() {
        return preprocessTime;
    }
}
