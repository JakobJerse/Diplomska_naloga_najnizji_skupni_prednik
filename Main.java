import java.util.*;
import javax.swing.SwingUtilities;
import java.io.*;

public class Main {

        public static void main(String[] args) {

                Scanner scanner = new Scanner(System.in);

                System.out.println("Choose the type of tree: ");
                System.out.println("1. Complete Tree");
                System.out.println("2. Random Tree");
                System.out.println("3. Skewed Tree");

                int treeType = scanner.nextInt();
                TreeGenerator treeGenerator = new TreeGenerator();

                switch (treeType) {
                        case 1:
                                System.out.println("Generating complete trees...");
                                TreeNode[] complete_trees = new TreeNode[19];
                                for (int i = 0; i < 19; i++) {
                                        complete_trees[i] = treeGenerator.generateCompleteTree(i + 1, 2);
                                }
                                System.out.println("Done.");
                                testNaiveMethod("complete_trees", complete_trees);
                                testPreprocessingSqrtMethod("complete_trees", complete_trees);
                                testQueryAnsweringSqrtMethod("complete_trees", complete_trees);
                                testPreprocessingBinaryLiftingMethod("complete_trees", complete_trees);
                                testQueryAnsweringBinaryLiftingMethod("complete_trees", complete_trees);
                                testPreprocessingRMQMethod("complete_trees", complete_trees);
                                testQueryAnsweringRMQMethod("complete_trees", complete_trees);
                                testPreprocessingFCBMethod("complete_trees", complete_trees);
                                testQueryAnsweringFCBMethod("complete_trees", complete_trees);
                                testQueryAnswersMatch("complete_trees", complete_trees);
                                break;
                        case 2:
                                System.out.println("Fetching random trees from test_trees/random_trees...");
                                TreeNode[] random_trees = new TreeNode[19];
                                for (int i = 0; i < 19; i++) {
                                        random_trees[i] = treeGenerator.loadTreeFromFile(
                                                        "test_trees/random_trees/random_tree_" + (i + 1) + ".ser");
                                }
                                System.out.println("Done.");
                                testNaiveMethod("random_trees", random_trees);
                                testPreprocessingSqrtMethod("random_trees", random_trees);
                                testQueryAnsweringSqrtMethod("random_trees", random_trees);
                                testPreprocessingBinaryLiftingMethod("random_trees", random_trees);
                                testQueryAnsweringBinaryLiftingMethod("random_trees", random_trees);
                                testPreprocessingRMQMethod("random_trees", random_trees);
                                testQueryAnsweringRMQMethod("random_trees", random_trees);
                                testPreprocessingFCBMethod("random_trees", random_trees);
                                testQueryAnsweringFCBMethod("random_trees", random_trees);
                                testQueryAnswersMatch("random_trees", random_trees);
                                break;
                        case 3:
                                System.out.println("Generating skewed trees...");
                                TreeNode[] skewed_trees = new TreeNode[11];
                                int treeDepth = 1;
                                for (int i = 0; i <= 10; i++) {
                                        skewed_trees[i] = treeGenerator.generateSkewedTree(treeDepth);
                                        if (i == 0) {
                                                treeDepth += 99999;
                                        } else {
                                                treeDepth += 100000;
                                        }
                                }
                                System.out.println("Done.");
                                testNaiveMethod("skewed_trees", skewed_trees);
                                testPreprocessingSqrtMethod("skewed_trees", skewed_trees);
                                testQueryAnsweringSqrtMethod("skewed_trees", skewed_trees);
                                testPreprocessingBinaryLiftingMethod("skewed_trees", skewed_trees);
                                testQueryAnsweringBinaryLiftingMethod("skewed_trees", skewed_trees);
                                testPreprocessingRMQMethod("skewed_trees", skewed_trees);
                                testQueryAnsweringRMQMethod("skewed_trees", skewed_trees);
                                testPreprocessingFCBMethod("skewed_trees", skewed_trees);
                                testQueryAnsweringFCBMethod("skewed_trees", skewed_trees);
                                testQueryAnswersMatch("skewed_trees", skewed_trees);
                                break;
                        default:
                                System.out.println("Invalid choice. Exiting.");
                                System.exit(1);
                }

                System.out.println("Finished.");
                switch (treeType) {
                        case 1:
                                SwingUtilities.invokeLater(
                                                () -> new LCAChart("LCA Performance Chart", 1));
                                break;

                        case 2:
                                SwingUtilities.invokeLater(() -> new LCAChart("LCA Performance Chart", 2));
                                break;
                        case 3:
                                SwingUtilities.invokeLater(() -> new LCAChart("LCA Performance Chart", 3));
                                break;
                }
        }

        private static void testNaiveMethod(String treeTypeString,
                        TreeNode[] trees) {
                try {
                        System.out.println("Would you like to test naive method? (y/n)");
                        Scanner scanner = new Scanner(System.in);
                        String choice = scanner.nextLine();
                        if (choice.equals("y")) {
                                // delete existing files
                                UtilMethods.deleteExistingFiles(new String[] {
                                                "test_results/" + treeTypeString + "/average_query_times_naive.txt" });

                                System.out.println("Testing Naive Method...");
                                for (int i = 0; i < trees.length; i++) {

                                        TreeNode root = trees[i];
                                        int numberOfNodes = UtilMethods.getNumberOfNoodes(root);
                                        int height = UtilMethods.height(root);

                                        List<Integer> query1 = new ArrayList<>();
                                        List<Integer> query2 = new ArrayList<>();
                                        Random random = new Random();
                                        for (int p = 0; p < 10100; p++) {
                                                // get 2 random numbers between 1 and num of nodes
                                                query1.add(random.nextInt(numberOfNodes) + 1);
                                                query2.add(random.nextInt(numberOfNodes) + 1);
                                        }

                                        // test query answering time
                                        LCA_recursion lca_recursion = new LCA_recursion();
                                        long queryTimeNaiveStart = 0;
                                        for (int k = 0; k < 10100; k++) {
                                                if (k == 100) {
                                                        queryTimeNaiveStart = System.nanoTime();
                                                }
                                                int node1 = query1.get(k);
                                                int node2 = query2.get(k);

                                                int res_naive = lca_recursion.getLCA(root, node1, node2).value;
                                        }
                                        long queryTimeNaiveEnd = System.nanoTime();
                                        long avgQueryTimeNaive = (queryTimeNaiveEnd - queryTimeNaiveStart) / 10000;

                                        // Store average query time
                                        UtilMethods.storeAverageTime(
                                                        "test_results/" + treeTypeString
                                                                        + "/average_query_times_naive.txt",
                                                        avgQueryTimeNaive,
                                                        avgQueryTimeNaive == 0 ? 0 : numberOfNodes);

                                        System.out.println("Successfully tested tree with height "
                                                        + height
                                                        + " and the number of nodes: " + numberOfNodes + ".");
                                }
                        } else {
                                System.out.println("Skipping testing naive method.");
                                System.out.println();

                                return;
                        }
                } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                }
        }

        private static void testPreprocessingSqrtMethod(String treeTypeString,
                        TreeNode[] trees) {
                try {
                        System.out.println(
                                        "Would you like to test preprocessing for Square root decomposition method? (y/n)");
                        Scanner scanner = new Scanner(System.in);
                        String choice = scanner.nextLine();
                        if (choice.equals("y")) {
                                // delete existing files
                                UtilMethods.deleteExistingFiles(
                                                new String[] { "test_results/" + treeTypeString
                                                                + "/average_preprocess_times_sqrt.txt" });

                                System.out.println("Testing preprocessing for Square root decomposition method...");
                                for (int i = 0; i < trees.length; i++) {
                                        long avgPreprocessTimeSqrt = 0;

                                        List<Long> preprocessingTimesSqrt = new ArrayList<>();

                                        TreeNode root = trees[i];
                                        int numberOfNodes = UtilMethods.getNumberOfNoodes(root);
                                        int height = UtilMethods.height(root);

                                        // test preprocessing time
                                        for (int j = 0; j < 100; j++) {

                                                LCA_sqrt lca2 = new LCA_sqrt(root);

                                                if (j >= 20) {
                                                        preprocessingTimesSqrt.add(lca2.getPreprocessTime());
                                                }
                                        }

                                        // handle outliers
                                        preprocessingTimesSqrt = removeOutliers(preprocessingTimesSqrt, 0.9);

                                        // get average preprocessing time
                                        avgPreprocessTimeSqrt = calculateAverage(preprocessingTimesSqrt);

                                        // Store average preprocessing time
                                        UtilMethods.storeAverageTime(
                                                        "test_results/" + treeTypeString
                                                                        + "/average_preprocess_times_sqrt.txt",
                                                        avgPreprocessTimeSqrt,
                                                        avgPreprocessTimeSqrt == 0 ? 0 : numberOfNodes);

                                        System.out.println("Successfully tested tree with height "
                                                        + height
                                                        + " and the number of nodes: " + numberOfNodes + ".");
                                }
                        } else {
                                System.out.println(
                                                "Skipping preprocessing testing for square root decomposition method.");
                                System.out.println();

                                return;
                        }
                } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                }
        }

        private static void testQueryAnsweringSqrtMethod(String treeTypeString,
                        TreeNode[] trees) {
                try {
                        System.out.println(
                                        "Would you like to test query answering for Square root decomposition method? (y/n)");
                        Scanner scanner = new Scanner(System.in);
                        String choice = scanner.nextLine();
                        if (choice.equals("y")) {
                                // delete existing files
                                UtilMethods.deleteExistingFiles(
                                                new String[] { "test_results/" + treeTypeString
                                                                + "/average_query_times_sqrt.txt" });

                                System.out.println("Testing query answering for Square root decomposition method...");
                                for (int i = 0; i < trees.length; i++) {

                                        TreeNode root = trees[i];
                                        int numberOfNodes = UtilMethods.getNumberOfNoodes(root);
                                        int height = UtilMethods.height(root);

                                        List<Integer> query1 = new ArrayList<>();
                                        List<Integer> query2 = new ArrayList<>();
                                        Random random = new Random();
                                        for (int p = 0; p < 2001000; p++) {
                                                // get 2 random numbers between 1 and num of nodes
                                                query1.add(random.nextInt(numberOfNodes) + 1);
                                                query2.add(random.nextInt(numberOfNodes) + 1);
                                        }

                                        // test query answering time
                                        LCA_sqrt lca2 = new LCA_sqrt(root);
                                        long queryTimeSqrtStart = 0;
                                        for (int k = 0; k < 2001000; k++) {
                                                if (k == 1000) {
                                                        queryTimeSqrtStart = System.nanoTime();
                                                }
                                                int node1 = query1.get(k);
                                                int node2 = query2.get(k);

                                                int res_sqrt = lca2.getLCA(node1, node2).value;
                                        }
                                        long queryTimeSqrtEnd = System.nanoTime();
                                        long avgQueryTimeSqrt = (queryTimeSqrtEnd - queryTimeSqrtStart) / 2000000;

                                        // Store average query time
                                        UtilMethods.storeAverageTime(
                                                        "test_results/" + treeTypeString
                                                                        + "/average_query_times_sqrt.txt",
                                                        avgQueryTimeSqrt,
                                                        avgQueryTimeSqrt == 0 ? 0 : numberOfNodes);

                                        System.out.println("Successfully tested tree with height "
                                                        + height
                                                        + " and the number of nodes: " + numberOfNodes + ".");
                                }
                        } else {
                                System.out.println(
                                                "Skipping query answering testing for square root decomposition method.");
                                System.out.println();

                                return;
                        }
                } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                }
        }

        private static void testPreprocessingBinaryLiftingMethod(String treeTypeString, TreeNode[] trees) {
                try {
                        System.out.println("Would you like to test preprocesssing for Binary lifting method? (y/n)");
                        Scanner scanner = new Scanner(System.in);
                        String choice = scanner.nextLine();
                        if (choice.equals("y")) {
                                // delete existing files
                                UtilMethods.deleteExistingFiles(
                                                new String[] { "test_results/" + treeTypeString
                                                                + "/average_preprocess_times_binary_lift.txt" });

                                System.out.println("Testing preprocessing for Binary lifting method...");
                                for (int i = 0; i < trees.length; i++) {
                                        long avgPreprocessBinaryLiftingTime = 0;

                                        List<Long> preprocessingTimesBinaryLifting = new ArrayList<>();

                                        TreeNode root = trees[i];
                                        int numberOfNodes = UtilMethods.getNumberOfNoodes(root);
                                        int height = UtilMethods.height(root);

                                        // test preprocessing time
                                        for (int j = 0; j < 40; j++) {

                                                LCA_binary_lift lca_binary_lifting = new LCA_binary_lift(root);

                                                if (j >= 10) {
                                                        preprocessingTimesBinaryLifting
                                                                        .add(lca_binary_lifting.getPreprocessTime());
                                                }
                                        }

                                        // handle outliers
                                        preprocessingTimesBinaryLifting = removeOutliers(
                                                        preprocessingTimesBinaryLifting, 0.9);

                                        // get average preprocessing time
                                        avgPreprocessBinaryLiftingTime = calculateAverage(
                                                        preprocessingTimesBinaryLifting);

                                        // Store average preprocessing time
                                        UtilMethods.storeAverageTime(
                                                        "test_results/" + treeTypeString
                                                                        + "/average_preprocess_times_binary_lift.txt",
                                                        avgPreprocessBinaryLiftingTime,
                                                        avgPreprocessBinaryLiftingTime == 0 ? 0 : numberOfNodes);

                                        System.out.println("Successfully tested tree with height "
                                                        + height
                                                        + " and the number of nodes: " + numberOfNodes + ".");
                                }
                        } else {
                                System.out.println("Skipping testing preprocessing for Binary lifting method.");
                                System.out.println();

                                return;
                        }
                } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                }
        }

        private static void testQueryAnsweringBinaryLiftingMethod(String treeTypeString, TreeNode[] trees) {
                try {
                        System.out.println("Would you like to test query answering for Binary lifting method? (y/n)");
                        Scanner scanner = new Scanner(System.in);
                        String choice = scanner.nextLine();
                        if (choice.equals("y")) {
                                // delete existing files
                                UtilMethods.deleteExistingFiles(
                                                new String[] { "test_results/" + treeTypeString
                                                                + "/average_query_times_binary_lift.txt" });

                                System.out.println("Testing query answering for Binary lifting method...");
                                for (int i = 0; i < trees.length; i++) {

                                        TreeNode root = trees[i];
                                        int numberOfNodes = UtilMethods.getNumberOfNoodes(root);
                                        int height = UtilMethods.height(root);

                                        List<Integer> query1 = new ArrayList<>();
                                        List<Integer> query2 = new ArrayList<>();
                                        Random random = new Random();
                                        for (int p = 0; p < 2001000; p++) {
                                                // get 2 random numbers between 1 and num of nodes
                                                query1.add(random.nextInt(numberOfNodes) + 1);
                                                query2.add(random.nextInt(numberOfNodes) + 1);
                                        }

                                        // test query answering time
                                        LCA_binary_lift lca_binary_lifting = new LCA_binary_lift(root);
                                        long queryTimeBinaryLiftingStart = 0;
                                        for (int k = 0; k < 2001000; k++) {
                                                if (k == 1000) {
                                                        queryTimeBinaryLiftingStart = System.nanoTime();
                                                }
                                                int node1 = query1.get(k);
                                                int node2 = query2.get(k);

                                                int res_binary_lift = lca_binary_lifting.getLCA(node1, node2).value;
                                        }
                                        long queryTimeBinaryLiftingEnd = System.nanoTime();
                                        long avgQueryTimeBinaryLifting = (queryTimeBinaryLiftingEnd
                                                        - queryTimeBinaryLiftingStart) / 2000000;

                                        // Store average query time
                                        UtilMethods.storeAverageTime(
                                                        "test_results/" + treeTypeString
                                                                        + "/average_query_times_binary_lift.txt",
                                                        avgQueryTimeBinaryLifting,
                                                        avgQueryTimeBinaryLifting == 0 ? 0 : numberOfNodes);

                                        System.out.println("Successfully tested tree with height "
                                                        + height
                                                        + " and the number of nodes: " + numberOfNodes + ".");
                                }
                        } else {
                                System.out.println("Skipping query answering testing for Binary lifting method.");
                                System.out.println();
                                return;
                        }
                } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                }
        }

        private static void testPreprocessingRMQMethod(String treeTypeString,
                        TreeNode[] trees) {
                try {
                        System.out.println("Would you like to test preprocessing for RMQ method? (y/n)");
                        Scanner scanner = new Scanner(System.in);
                        String choice = scanner.nextLine();
                        if (choice.equals("y")) {
                                // delete existing files
                                UtilMethods.deleteExistingFiles(
                                                new String[] { "test_results/" + treeTypeString
                                                                + "/average_preprocess_times_rmq.txt" });

                                System.out.println("Testing preprocessing for RMQ Method...");
                                for (int i = 0; i < trees.length; i++) {
                                        long avgPreprocessRMQTime = 0;

                                        List<Long> preprocessingTimesRMQ = new ArrayList<>();

                                        TreeNode root = trees[i];
                                        int numberOfNodes = UtilMethods.getNumberOfNoodes(root);
                                        int height = UtilMethods.height(root);

                                        // test preprocessing time
                                        for (int j = 0; j < 60; j++) {

                                                LCA_RMQ lca_rmq = new LCA_RMQ(root);

                                                if (j >= 20) {
                                                        preprocessingTimesRMQ.add(lca_rmq.getPreprocessTime());
                                                }
                                        }

                                        // handle outliers
                                        preprocessingTimesRMQ = removeOutliers(preprocessingTimesRMQ, 0.9);

                                        // get average preprocessing time
                                        avgPreprocessRMQTime = calculateAverage(preprocessingTimesRMQ);

                                        // Store average preprocessing time
                                        UtilMethods.storeAverageTime(
                                                        "test_results/" + treeTypeString
                                                                        + "/average_preprocess_times_rmq.txt",
                                                        avgPreprocessRMQTime,
                                                        avgPreprocessRMQTime == 0 ? 0 : numberOfNodes);

                                        System.out.println("Successfully tested tree with height "
                                                        + height
                                                        + " and the number of nodes: " + numberOfNodes + ".");
                                }
                        } else {
                                System.out.println("Skipping preprocessing testing for RMQ method.");
                                System.out.println();
                                return;
                        }
                } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                }
        }

        private static void testQueryAnsweringRMQMethod(String treeTypeString,
                        TreeNode[] trees) {
                try {
                        System.out.println("Would you like to test query answering for RMQ method? (y/n)");
                        Scanner scanner = new Scanner(System.in);
                        String choice = scanner.nextLine();
                        if (choice.equals("y")) {
                                // delete existing files
                                UtilMethods.deleteExistingFiles(
                                                new String[] { "test_results/" + treeTypeString
                                                                + "/average_query_times_rmq.txt" });

                                System.out.println("Testing query answering for RMQ method...");
                                for (int i = 0; i < trees.length; i++) {

                                        TreeNode root = trees[i];
                                        int numberOfNodes = UtilMethods.getNumberOfNoodes(root);
                                        int height = UtilMethods.height(root);

                                        List<Integer> query1 = new ArrayList<>();
                                        List<Integer> query2 = new ArrayList<>();
                                        Random random = new Random();
                                        for (int p = 0; p < 2010000; p++) {
                                                // get 2 random numbers between 1 and num of nodes
                                                query1.add(random.nextInt(numberOfNodes) + 1);
                                                query2.add(random.nextInt(numberOfNodes) + 1);
                                        }

                                        // test query answering time
                                        LCA_RMQ lca_rmq = new LCA_RMQ(root);
                                        long queryTimeRMQStart = 0;
                                        for (int k = 0; k < 1010000; k++) {
                                                if (k == 10000) {
                                                        queryTimeRMQStart = System.nanoTime();
                                                }
                                                int node1 = query1.get(k);
                                                int node2 = query2.get(k);

                                                int res_rmq = lca_rmq.getLCA(node1, node2).value;
                                        }
                                        long queryTimeRMQEnd = System.nanoTime();
                                        long avgQueryTimeRMQ = (queryTimeRMQEnd - queryTimeRMQStart) / 1000000;

                                        // Store average query time
                                        UtilMethods.storeAverageTime(
                                                        "test_results/" + treeTypeString
                                                                        + "/average_query_times_rmq.txt",
                                                        avgQueryTimeRMQ,
                                                        avgQueryTimeRMQ == 0 ? 0 : numberOfNodes);

                                        System.out.println("Successfully tested tree with height "
                                                        + height
                                                        + " and the number of nodes: " + numberOfNodes + ".");
                                }
                        } else {
                                System.out.println("Skipping query answering testing for RMQ method.");
                                System.out.println();
                                return;
                        }
                } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                }
        }

        private static void testPreprocessingFCBMethod(String treeTypeString, TreeNode[] trees) {
                try {
                        System.out.println("Would you like to test preprocessing for FCB method? (y/n)");
                        Scanner scanner = new Scanner(System.in);
                        String choice = scanner.nextLine();
                        if (choice.equals("y")) {
                                // delete existing files
                                UtilMethods.deleteExistingFiles(
                                                new String[] { "test_results/" + treeTypeString
                                                                + "/average_preprocess_times_fcb.txt" });

                                System.out.println("Testing preprocessing for FCB Method...");
                                for (int i = 0; i < trees.length; i++) {
                                        long avgPreprocessFCBTime = 0;

                                        List<Long> preprocessingTimesFCB = new ArrayList<>();

                                        TreeNode root = trees[i];
                                        int numberOfNodes = UtilMethods.getNumberOfNoodes(root);
                                        int height = UtilMethods.height(root);

                                        // test preprocessing time
                                        for (int j = 0; j < 100; j++) {

                                                LCA_FCB lca_fcb = new LCA_FCB(root);

                                                if (j >= 20) {
                                                        preprocessingTimesFCB.add(lca_fcb.getPreprocessTime());
                                                }
                                        }

                                        // handle outliers
                                        preprocessingTimesFCB = removeOutliers(preprocessingTimesFCB, 0.9);

                                        // get average preprocessing time
                                        avgPreprocessFCBTime = calculateAverage(preprocessingTimesFCB);

                                        // Store average preprocessing time
                                        UtilMethods.storeAverageTime(
                                                        "test_results/" + treeTypeString
                                                                        + "/average_preprocess_times_fcb.txt",
                                                        avgPreprocessFCBTime,
                                                        avgPreprocessFCBTime == 0 ? 0 : numberOfNodes);

                                        System.out.println("Successfully tested tree with height "
                                                        + height
                                                        + " and the number of nodes: " + numberOfNodes + ".");
                                }
                        } else {
                                System.out.println("Skipping preprocessing testing FCB method.");
                                System.out.println();
                                return;
                        }
                } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                }
        }

        private static void testQueryAnsweringFCBMethod(String treeTypeString, TreeNode[] trees) {
                try {
                        System.out.println("Would you like to test query answering for FCB method? (y/n)");
                        Scanner scanner = new Scanner(System.in);
                        String choice = scanner.nextLine();
                        if (choice.equals("y")) {
                                // delete existing files
                                UtilMethods.deleteExistingFiles(
                                                new String[] { "test_results/" + treeTypeString
                                                                + "/average_query_times_fcb.txt" });

                                System.out.println("Testing query answering for FCB Method...");
                                for (int i = 0; i < trees.length; i++) {

                                        TreeNode root = trees[i];
                                        int numberOfNodes = UtilMethods.getNumberOfNoodes(root);
                                        int height = UtilMethods.height(root);

                                        List<Integer> query1 = new ArrayList<>();
                                        List<Integer> query2 = new ArrayList<>();
                                        Random random = new Random();
                                        for (int p = 0; p < 1001000; p++) {
                                                // get 2 random numbers between 1 and num of nodes
                                                query1.add(random.nextInt(numberOfNodes) + 1);
                                                query2.add(random.nextInt(numberOfNodes) + 1);
                                        }

                                        // test query answering time
                                        LCA_FCB lca_fcb = new LCA_FCB(root);
                                        long queryTimeFCBStart = 0;
                                        for (int k = 0; k < 1001000; k++) {
                                                if (k == 1000) {
                                                        queryTimeFCBStart = System.nanoTime();
                                                }
                                                int node1 = query1.get(k);
                                                int node2 = query2.get(k);

                                                int res_fcb = lca_fcb.getLCA(node1, node2).value;
                                        }
                                        long queryTimeFCBEnd = System.nanoTime();
                                        long avgQueryTimeFCB = (queryTimeFCBEnd - queryTimeFCBStart) / 1000000;

                                        // Store average query time
                                        UtilMethods.storeAverageTime(
                                                        "test_results/" + treeTypeString
                                                                        + "/average_query_times_fcb.txt",
                                                        avgQueryTimeFCB,
                                                        avgQueryTimeFCB == 0 ? 0 : numberOfNodes);

                                        System.out.println("Successfully tested tree with height "
                                                        + height
                                                        + " and the number of nodes: " + numberOfNodes + ".");
                                }
                        } else {
                                System.out.println("Skipping query answering testing for FCB method.");
                                System.out.println();
                                return;
                        }
                } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                }
        }

        private static void testQueryAnswersMatch(String treeTypeString, TreeNode[] trees) {
                UtilMethods.deleteExistingFiles(
                                new String[] { "test_results/" + treeTypeString + "/QueryResults.txt" });
                System.out.println("Testing query answers matching for all methods...");
                try {
                        FileWriter fileWriter = new FileWriter("test_results/" + treeTypeString + "/QueryResults.txt");
                        for (int i = 0; i < trees.length; i++) {

                                TreeNode root = trees[i];
                                int numberOfNodes = UtilMethods.getNumberOfNoodes(root);
                                int height = UtilMethods.height(root);
                                System.out.println("Testing tree with height " + height
                                                + " and the number of nodes: " + numberOfNodes + ".");

                                LCA_recursion lca_recursion = new LCA_recursion();
                                LCA_sqrt lca_sqrt = new LCA_sqrt(root);
                                LCA_binary_lift lca_binary_lift = new LCA_binary_lift(root);
                                LCA_RMQ lca_rmq = new LCA_RMQ(root);
                                LCA_FCB lca_fcb = new LCA_FCB(root);

                                for (int k = 0; k < 500; k++) {
                                        // get 2 random numbers between 1 and num of nodes
                                        Random random = new Random();
                                        int node1 = random.nextInt(numberOfNodes)
                                                        + 1;
                                        int node2 = random.nextInt(numberOfNodes)
                                                        + 1;

                                        int res_naive = lca_recursion.getLCA(root, node1, node2).value;
                                        int res_sqrt = lca_sqrt.getLCA(node1, node2).value;
                                        int res_binary_lift = lca_binary_lift.getLCA(node1, node2).value;
                                        int res_rmq = lca_rmq.getLCA(node1, node2).value;
                                        int res_fcb = lca_fcb.getLCA(node1, node2).value;

                                        // save resultst to a file
                                        fileWriter.write("Node1: " + node1 + " Node2: " + node2 + "\n");
                                        fileWriter.write(res_naive + ", ");
                                        fileWriter.write(res_sqrt + ", ");
                                        fileWriter.write(res_binary_lift + ", ");
                                        fileWriter.write(res_rmq + ", ");
                                        fileWriter.write(res_fcb + "\n");
                                        fileWriter.write("\n");

                                        if (res_naive == res_sqrt && res_naive == res_binary_lift
                                                        && res_naive == res_rmq && res_naive == res_fcb) {

                                                continue;
                                        } else {
                                                throw new RuntimeException("Results do not match.");
                                        }
                                }

                        }
                        fileWriter.close();
                        System.out.println("All results match!");

                } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                }
        }

        private static List<Long> removeOutliers(List<Long> times, double deviation) {
                if (times == null || times.isEmpty()) {
                        throw new IllegalArgumentException("The times list should not be null or empty.");
                }

                double average = calculateAverage(times);
                List<Long> filteredTimes = new ArrayList<>();

                for (long time : times) {
                        if (!isOutlier(time, average, deviation)) {
                                filteredTimes.add(time);
                        }
                }

                return filteredTimes;
        }

        private static long calculateAverage(List<Long> times) {
                long sum = 0;
                for (long time : times) {
                        sum += time;
                }
                return times.size() > 0 ? sum / times.size() : 0;
        }

        private static boolean isOutlier(long currentValue, double average, double deviation) {
                double upperThreshold = average * (1 + deviation);
                double lowerThreshold = average * (1 - deviation);
                return currentValue > upperThreshold || currentValue < lowerThreshold;
        }
}
