
/*
 *  Implementation of LCA using reduction to RMQ technique
 *  Preprocesing complexity : O(nlog(n))
 *  Query complexity : O(1)
 */

import java.util.*;

public class LCA_RMQ {
    TreeNode root;
    int numNodes;
    int p;
    ArrayList<TreeNode> eulerTourArray;
    ArrayList<Integer> depthArray;
    int[] firstAppearanceIndex;
    int[] log2Array;
    int[] pow2Array;
    int size;
    int[][] sparseTable;
    int[][] indexTable;

    public LCA_RMQ(TreeNode root) {
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
        this.indexTable = new int[p + 1][size];

        buildHelperArrays();
        dfs(this.root);
        buildSparseTable();
        System.out.println("test");
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
            sparseTable[0][i] = depthArray.get(i);
            indexTable[0][i] = i;
        }

        for (int i = 1; i <= p; i++) {
            for (int j = 0; j + pow2Array[i] <= eulerTourArray.size(); j++) {
                int left = sparseTable[i - 1][j];
                int right = sparseTable[i - 1][j + pow2Array[i - 1]];
                if (left <= right) {
                    sparseTable[i][j] = left;
                    indexTable[i][j] = indexTable[i - 1][j];
                } else {
                    sparseTable[i][j] = right;
                    indexTable[i][j] = indexTable[i - 1][j + pow2Array[i - 1]];
                }
            }
        }
    }

    // query the sparse table
    private int query(int left, int right) {

        int length = right - left + 1;
        int k = log2Array[length];
        int leftIntervalValue = sparseTable[k][left];
        int rightIntervalValue = sparseTable[k][right - pow2Array[k] + 1];

        if (leftIntervalValue <= rightIntervalValue) {
            return indexTable[k][left];
        } else {
            return indexTable[k][right - pow2Array[k] + 1];
        }

    }

    // LCA query
    public TreeNode getLCA(int node1_value, int node2_value) {
        int left = Math.min(firstAppearanceIndex[node1_value], firstAppearanceIndex[node2_value]);
        int right = Math.max(firstAppearanceIndex[node1_value], firstAppearanceIndex[node2_value]);
        int lcaIndex = query(left, right);
        return eulerTourArray.get(lcaIndex);

    }

}
