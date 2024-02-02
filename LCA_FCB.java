
/*
 *  Implementation of LCA  Farach-Colton and Bender algorithm
 *  Preprocesing complexity : O(N)
 *  Query complexity : O(1)
 */

import java.util.*;

public class LCA_FCB {

    private long preprocessTime;

    private TreeNode root;
    private int numNodes;
    private int p;
    private int blockSize;
    private int numBlocks;
    private int[] log2Array;
    private int[] pow2Array;
    private ArrayList<TreeNode> eulerTourArray;
    private ArrayList<Integer> depthArray;
    private int[] firstAppearanceIndex;
    private int eulerTourSize;
    private int[] minOfEachBlock;
    private int[] blockStartingIndex; // the starting index in the Euler tour for each block
    private int[] blockBitmasks;
    private int[][] sparseTable;
    private int[][][] precomputedBlocks;

    public LCA_FCB(TreeNode root) {

        long preprocessStartTime = System.nanoTime();

        this.root = root;
        this.numNodes = UtilMethods.getNumberOfNoodes(root);
        this.eulerTourSize = 2 * numNodes - 1;
        this.blockSize = Math.max(1, (int) (Math.log(eulerTourSize) / Math.log(2)) / 2);
        this.numBlocks = (int) Math.ceil((double) eulerTourSize / blockSize);
        this.log2Array = new int[eulerTourSize + 1];
        this.pow2Array = new int[eulerTourSize + 1];
        this.eulerTourArray = new ArrayList<>();
        this.depthArray = new ArrayList<>();
        this.firstAppearanceIndex = new int[numNodes + 1];
        this.blockBitmasks = new int[numBlocks];

        this.buildHelperArrays();
        this.dfs(this.root);
        this.getMinOfEachBlock(blockSize);
        this.buildSparseTable();
        this.precomputeBlockBitMasks();
        this.precomputeBlocks();

        long preprocessEndTime = System.nanoTime();
        this.preprocessTime = preprocessEndTime - preprocessStartTime;
    }

    // construct the log2 and pow2 arrays
    private void buildHelperArrays() {
        pow2Array[0] = 1;
        for (int i = 1; i <= this.eulerTourSize; i++) {
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

    // precompute the minimum of each block
    private void getMinOfEachBlock(int blockSize) {
        minOfEachBlock = new int[numBlocks];
        blockStartingIndex = new int[numBlocks + 1];

        for (int i = 0; i < eulerTourSize; i += blockSize) {
            int minIndex = i;
            for (int j = i + 1; j < i + blockSize && j < eulerTourSize; j++) {
                if (depthArray.get(j) < depthArray.get(minIndex)) {
                    minIndex = j;
                }
            }
            minOfEachBlock[i / blockSize] = minIndex;
            blockStartingIndex[i / blockSize] = i;
        }
    }

    // build the sparse table based on the minimum of each block using dynamic
    // programming
    private void buildSparseTable() {
        int p = (int) (Math.log(numBlocks) / Math.log(2));
        sparseTable = new int[p + 1][numBlocks];

        for (int i = 0; i < numBlocks; i++) {
            sparseTable[0][i] = minOfEachBlock[i];
        }

        for (int i = 1; i <= p; i++) {
            for (int j = 0; j + pow2Array[i] <= numBlocks; j++) {
                int leftIndex = sparseTable[i - 1][j];
                int rightIndex = sparseTable[i - 1][j + pow2Array[i - 1]];
                sparseTable[i][j] = depthArray.get(leftIndex) <= depthArray.get(rightIndex) ? leftIndex : rightIndex;
            }
        }
    }

    // precompute the bitmasks for each block
    private void precomputeBlockBitMasks() {
        for (int i = 0; i < numBlocks; i++) {
            int mask = 0;
            for (int j = 1; j < blockSize && blockStartingIndex[i] + j < eulerTourSize; j++) {
                int prevIndex = blockStartingIndex[i] + j - 1;
                int currentIndex = blockStartingIndex[i] + j;
                int prevDepth = depthArray.get(prevIndex);
                int currentDepth = depthArray.get(currentIndex);

                if (currentDepth > prevDepth) {
                    mask |= (1 << (j - 1));
                }
            }
            blockBitmasks[i] = mask;
        }
    }

    // precompute the index of the minimum element for each possible subsequence of
    // each block
    private void precomputeBlocks() {
        int max_num_sequences = (1 << (blockSize - 1)); // 2^(blockSize - 1)
        precomputedBlocks = new int[max_num_sequences][blockSize][blockSize];

        Set<Integer> bitMasksSet = new HashSet<>();

        for (int block = 0; block < numBlocks; block++) {
            int blockMask = blockBitmasks[block];
            if (!bitMasksSet.contains(blockMask)) {
                bitMasksSet.add(blockMask);
                for (int start = 0; start < blockSize && blockStartingIndex[block] + start < eulerTourSize; start++) {
                    for (int end = start; end < blockSize && blockStartingIndex[block] + end < eulerTourSize; end++) {
                        int minElementIndex = blockStartingIndex[block] + start;
                        for (int k = start + 1; k <= end; k++) {
                            int currIndex = blockStartingIndex[block] + k;
                            if (depthArray.get(currIndex) < depthArray.get(minElementIndex)) {
                                minElementIndex = currIndex;
                            }
                        }
                        precomputedBlocks[blockMask][start][end] = minElementIndex;
                    }
                }

            }

        }
    }

    // LCA query
    public TreeNode getLCA(int node1_value, int node2_value) {

        if (node1_value < 1 || node1_value > numNodes) {
            throw new IllegalArgumentException("Node1 not found");
        } else if (node2_value < 1 || node2_value > numNodes) {
            throw new IllegalArgumentException("Node2 not found");
        } else if (node1_value < 1 || node1_value > numNodes && node2_value < 1 || node2_value > numNodes) {
            throw new IllegalArgumentException("Both nodes not found");
        }

        int left = Math.min(firstAppearanceIndex[node1_value], firstAppearanceIndex[node2_value]);
        int right = Math.max(firstAppearanceIndex[node1_value], firstAppearanceIndex[node2_value]);

        int leftBlockIndex = left / blockSize;
        int rightBlockIndex = right / blockSize;

        int lcaIndex = -1;

        if (leftBlockIndex == rightBlockIndex) {
            int blockMask = blockBitmasks[leftBlockIndex];
            int inBlockIndex = (precomputedBlocks[blockMask][left % blockSize][right % blockSize]) % blockSize;
            lcaIndex = blockStartingIndex[leftBlockIndex] + inBlockIndex;
            return eulerTourArray.get(lcaIndex);

        } else {

            // callculate the minimum of suffix of the left block
            int leftBlockMask = blockBitmasks[leftBlockIndex];
            int leftInBlockIndex = (precomputedBlocks[leftBlockMask][left % blockSize][blockSize - 1]) % blockSize;
            int minSuffixLeftBlock = blockStartingIndex[leftBlockIndex] + leftInBlockIndex;

            // callculate the minimum of prefix of the right block
            int rightBlockMask = blockBitmasks[rightBlockIndex];
            int rightInBlockIndex = (precomputedBlocks[rightBlockMask][0][right % blockSize]) % blockSize;
            int minPrefixRightBlock = blockStartingIndex[rightBlockIndex] + rightInBlockIndex;

            int range = rightBlockIndex - leftBlockIndex - 1;
            if (range > 0) {
                int largestPowerOf2 = log2Array[range];

                // Start querying from the block after leftBlockIndex
                int minLeftSide = sparseTable[largestPowerOf2][leftBlockIndex + 1];

                // Query the range towards the right
                int minRightSide = sparseTable[largestPowerOf2][rightBlockIndex - pow2Array[largestPowerOf2]];

                if (depthArray.get(minLeftSide) <= depthArray.get(minRightSide)) {
                    lcaIndex = minLeftSide;
                } else {
                    lcaIndex = minRightSide;
                }
            } else {
                if (depthArray.get(minSuffixLeftBlock) <= depthArray.get(minPrefixRightBlock)) {
                    lcaIndex = minSuffixLeftBlock;
                } else {
                    lcaIndex = minPrefixRightBlock;
                }
            }

            // final minimum calculation
            int tempLcaIndex = 0;

            if (depthArray.get(minSuffixLeftBlock) < depthArray.get(minPrefixRightBlock)) {
                tempLcaIndex = minSuffixLeftBlock;

            } else {
                tempLcaIndex = minPrefixRightBlock;
            }

            int finalLcaIndex = 0;
            if (depthArray.get(lcaIndex) < depthArray.get(tempLcaIndex)) {

                finalLcaIndex = lcaIndex;
            } else {
                finalLcaIndex = tempLcaIndex;
            }

            return eulerTourArray.get(finalLcaIndex);

        }
    }

    public long getPreprocessTime() {
        return preprocessTime;
    }

}
