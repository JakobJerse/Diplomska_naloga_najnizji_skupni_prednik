import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.VerticalAlignment;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class LCAChart extends JFrame {

        private int treeType;

        private Font legendFont = new Font("Arial", Font.PLAIN, 16);
        private Font axisFont = new Font("Arial", Font.PLAIN, 16);

        Map<String, String> dictionary = Map.of(
                        "naive", "Naivna metoda",
                        "sqrt", "Korenska dekompozicija",
                        "binary_lift", "Binarni dvig",
                        "rmq", "Prevedba na iskanje najmanjše vrednosti na intervalu",
                        "fcb", "Farach-Colton-Benderjev algoritem");

        public LCAChart(String title, int treeType) {
                super(title);
                this.treeType = treeType;

                String treeTypeString = "";

                switch (treeType) {
                        case 1:
                                treeTypeString = "complete_trees";
                                break;
                        case 2:
                                treeTypeString = "random_trees";
                                break;
                        case 3:
                                treeTypeString = "skewed_trees";
                                break;
                }

                // Create datasets for preprocessing and query times
                XYSeriesCollection preprocessDataset = createDatasetForAllMethods(
                                "test_results/" + treeTypeString + "/average_preprocess_times");
                XYSeriesCollection queryDatasetNaive = createDatasetForOneMethod(
                                "test_results/" + treeTypeString + "/average_query_times_naive.txt",
                                "Naivna metoda");
                XYSeriesCollection queryDatasetSqrt = createDatasetForOneMethod(
                                "test_results/" + treeTypeString + "/average_query_times_sqrt.txt",
                                "Korenska dekompozicija");
                XYSeriesCollection queryDatasetBinaryLift = createDatasetForOneMethod(
                                "test_results/" + treeTypeString + "/average_query_times_binary_lift.txt",
                                "Binarni dvig");
                XYSeriesCollection queryDatasetRMQ = createDatasetForOneMethod(
                                "test_results/" + treeTypeString + "/average_query_times_rmq.txt",
                                "Prevedba na iskanje najmanjše vrednosti na intervalu");
                XYSeriesCollection queryDatasetFCB = createDatasetForOneMethod(
                                "test_results/" + treeTypeString + "/average_query_times_fcb.txt",
                                "Farach-Colton-Benderjev algoritem");
                XYSeriesCollection queryDatasetAllMethods = createDatasetForAllMethods(
                                "test_results/" + treeTypeString + "/average_query_times");

                // Create charts
                JFreeChart preprocessChart = createMultipleLineChart("Čas Predprocesiranja", preprocessDataset,
                                new Color[] { Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA }, 0);
                JFreeChart queryChart1 = null;
                JFreeChart queryChart2 = null;
                JFreeChart queryChart3 = null;
                JFreeChart queryChart4 = null;
                JFreeChart queryChart5 = null;
                JFreeChart queryChart6 = null;

                switch (treeType) {
                        case 1:
                                queryChart1 = createChart("Čas odgovora na poizvedbe - Naivna metoda",
                                                queryDatasetNaive,
                                                65000000,
                                                Color.RED);
                                queryChart2 = createChart("Čas odgovora na poizvedbe - Korenska dekompozicija",
                                                queryDatasetSqrt,
                                                1500, Color.BLUE);
                                queryChart3 = createChart("Čas odgovora na poizvedbe - Binarni dvig",
                                                queryDatasetBinaryLift,
                                                1500, Color.GREEN);
                                queryChart4 = createChart(
                                                "Čas odgovora na poizvedbe - Prevedba na iskanje najmanjše vrednosti na intervalu",
                                                queryDatasetRMQ,
                                                1500, Color.ORANGE);
                                queryChart5 = createChart(
                                                "Čas odgovora na poizvedbe - Farach-Colton-Benderjev algoritem",
                                                queryDatasetFCB, 1500, Color.MAGENTA);
                                queryChart6 = createMultipleLineChart("Čas odgovora na poizvedbe - vsi algoritmi",
                                                queryDatasetAllMethods,
                                                new Color[] { Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
                                                                Color.MAGENTA },
                                                1500);

                                break;
                        case 2:
                                queryChart1 = createChart("Čas odgovora na poizvedbe - Naivna metoda",
                                                queryDatasetNaive,
                                                35000000,
                                                Color.RED);
                                queryChart2 = createChart("Čas odgovora na poizvedbe - Korenska dekompozicija",
                                                queryDatasetSqrt,
                                                1500, Color.BLUE);
                                queryChart3 = createChart("Čas odgovora na poizvedbe - Binarni dvig",
                                                queryDatasetBinaryLift,
                                                1500, Color.GREEN);
                                queryChart4 = createChart(
                                                "Čas odgovora na poizvedbe - Prevedba na iskanje najmanjše vrednosti na intervalu",
                                                queryDatasetRMQ,
                                                1500, Color.ORANGE);
                                queryChart5 = createChart(
                                                "Čas odgovora na poizvedbe - Farach-Colton-Benderjev algoritem",
                                                queryDatasetFCB, 1500, Color.MAGENTA);
                                queryChart6 = createMultipleLineChart("Čas Predprocesiranja - vsi algoritmi",
                                                queryDatasetAllMethods,
                                                new Color[] { Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
                                                                Color.MAGENTA },
                                                1500);
                                break;
                        case 3:
                                queryChart1 = createChart("Čas odgovora na poizvedbe - Naivna metoda",
                                                queryDatasetNaive,
                                                35000000,
                                                Color.RED);
                                queryChart2 = createChart("Čas odgovora na poizvedbe - Korenska dekompozicija",
                                                queryDatasetSqrt,
                                                20000, Color.BLUE);
                                queryChart3 = createChart("Čas odgovora na poizvedbe - Binarni dvig",
                                                queryDatasetBinaryLift,
                                                5000, Color.GREEN);
                                queryChart4 = createChart(
                                                "Čas odgovora na poizvedbe - Prevedba na iskanje najmanjše vrednosti na intervalu",
                                                queryDatasetRMQ,
                                                2000, Color.ORANGE);
                                queryChart5 = createChart(
                                                "Čas odgovora na poizvedbe - Farach-Colton-Benderjev algoritem",
                                                queryDatasetFCB, 2000, Color.MAGENTA);

                                queryChart6 = createMultipleLineChart("Čas Predprocesiranja - vsi algoritmi",
                                                queryDatasetAllMethods,
                                                new Color[] { Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
                                                                Color.MAGENTA },
                                                20000);
                                break;
                }

                // Create panels for the charts
                ChartPanel preprocessPanel = new ChartPanel(preprocessChart);
                ChartPanel queryPanel1 = new ChartPanel(queryChart1);
                ChartPanel queryPanel2 = new ChartPanel(queryChart2);
                ChartPanel queryPanel3 = new ChartPanel(queryChart3);
                ChartPanel queryPanel4 = new ChartPanel(queryChart4);
                ChartPanel queryPanel5 = new ChartPanel(queryChart5);
                ChartPanel queryPanel6 = new ChartPanel(queryChart6);

                // Add panels to separate frames and display them
                displayMultipleChartsInSeparateFrame(
                                new ChartPanel[] { queryPanel1, queryPanel2, queryPanel3, queryPanel4, queryPanel5,
                                                queryPanel6 },
                                "Čas odgovora na poizvedbe");
                displayChartInSeparateFrame(preprocessPanel, "Čas Predprocesiranja");
        }

        private XYSeriesCollection createDatasetForAllMethods(String prefix) {
                XYSeriesCollection dataset = new XYSeriesCollection();

                for (String method : new String[] { "naive", "sqrt", "binary_lift", "rmq", "fcb" }) {

                        XYSeries series = new XYSeries(dictionary.get(method));
                        try (BufferedReader br = new BufferedReader(new FileReader(prefix + "_" + method + ".txt"))) {
                                String line;
                                while ((line = br.readLine()) != null) {
                                        String[] parts = line.split(", ");
                                        float time = Float.parseFloat(parts[0]);
                                        int numberOfNodes = Integer.parseInt(parts[1]);
                                        series.add(numberOfNodes, time);
                                }
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                        dataset.addSeries(series);
                }

                return dataset;
        }

        private XYSeriesCollection createDatasetForOneMethod(String filename, String key) {
                XYSeriesCollection dataset = new XYSeriesCollection();
                XYSeries series = new XYSeries(key);

                try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                                String[] parts = line.split(", ");
                                float time = Float.parseFloat(parts[0]);
                                int numberOfNodes = Integer.parseInt(parts[1]);
                                series.add(numberOfNodes, time);
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }

                dataset.addSeries(series);
                return dataset;
        }

        private JFreeChart createMultipleLineChart(String title, XYSeriesCollection dataset, Color[] lineColors,
                        int maxY) {
                JFreeChart chart = ChartFactory.createXYLineChart(
                                title,
                                "Število vozlišč",
                                "Čas (ns)",
                                dataset);

                chart.getTitle().setPadding(10, 0, 0, 0);
                chart.getTitle().setVerticalAlignment(VerticalAlignment.CENTER);
                chart.getTitle().setHorizontalAlignment((HorizontalAlignment.CENTER));

                XYPlot plot = chart.getXYPlot();
                plot.setBackgroundPaint(Color.WHITE);
                plot.setDomainGridlinesVisible(true);
                plot.setRangeGridlinesVisible(true);
                plot.setDomainGridlinePaint(Color.GRAY);
                plot.setRangeGridlinePaint(Color.GRAY);
                plot.setDomainPannable(true);
                plot.setRangePannable(true);

                // Set custom y axis range
                if (maxY > 0) {
                        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
                        yAxis.setRange(0, maxY);
                }

                // Set the curve colors
                XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                for (int i = 0; i < lineColors.length; i++) {
                        renderer.setSeriesPaint(i, lineColors[i]);
                }

                chart.getLegend().setItemFont(this.legendFont);

                // Adjust legend position
                chart.getLegend().setPosition(RectangleEdge.BOTTOM);
                chart.getLegend().setHorizontalAlignment(HorizontalAlignment.CENTER);
                chart.getLegend().setMargin(new RectangleInsets(0, 0, 10, 80));

                // X-axis title
                chart.getXYPlot().getDomainAxis().setLabelFont(this.axisFont);
                // X-axis numbers
                chart.getXYPlot().getDomainAxis().setTickLabelFont(this.axisFont);

                // Y-axis title
                chart.getXYPlot().getRangeAxis().setLabelFont(this.axisFont);

                // Y-axis numbers
                chart.getXYPlot().getRangeAxis().setTickLabelFont(this.axisFont);

                return chart;
        }

        private JFreeChart createChart(String title, XYSeriesCollection dataset, int maxY,
                        Color curveColor) {
                JFreeChart chart = ChartFactory.createXYLineChart(
                                title,
                                "Število vozlišč",
                                "Čas (ns)",
                                dataset);

                chart.getTitle().setPadding(5, 0, 0, 0);
                chart.getTitle().setVerticalAlignment(VerticalAlignment.CENTER);
                chart.getTitle().setHorizontalAlignment((HorizontalAlignment.CENTER));

                XYPlot plot = chart.getXYPlot();
                plot.setBackgroundPaint(Color.WHITE);
                plot.setDomainGridlinesVisible(true);
                plot.setRangeGridlinesVisible(true);
                plot.setDomainGridlinePaint(Color.GRAY);
                plot.setRangeGridlinePaint(Color.GRAY);
                plot.setDomainPannable(true);
                plot.setRangePannable(true);

                // Set custom y axis range
                NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
                yAxis.setRange(0, maxY);

                // Set the curve color
                XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                renderer.setSeriesPaint(0, curveColor);

                // Customize legend font
                chart.getLegend().setItemFont(this.legendFont);

                // Adjust legend position and padding
                chart.getLegend().setPosition(RectangleEdge.BOTTOM);
                chart.getLegend().setHorizontalAlignment(HorizontalAlignment.CENTER);
                chart.getLegend().setMargin(new RectangleInsets(0, 0, 20, 0));

                // X-axis title
                chart.getXYPlot().getDomainAxis().setLabelFont(this.axisFont);

                // X-axis numbers
                chart.getXYPlot().getDomainAxis().setTickLabelFont(this.axisFont);

                // Y-axis title
                chart.getXYPlot().getRangeAxis().setLabelFont(this.axisFont);
                
                // Y-axis numbers
                chart.getXYPlot().getRangeAxis().setTickLabelFont(this.axisFont);

                return chart;
        }

        private void displayChartInSeparateFrame(ChartPanel chartPanel, String frameTitle) {
                JFrame frame = new JFrame(frameTitle);
                frame.setLayout(new GridLayout(1, 1));
                frame.add(chartPanel);
                frame.setSize(800, 800);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
        }

        private void displayMultipleChartsInSeparateFrame(ChartPanel[] charts, String frameTitle) {
                JFrame frame = new JFrame(frameTitle);
                frame.setLayout(new GridLayout(3, 2));
                for (ChartPanel chartPanel : charts) {
                        frame.add(chartPanel);
                }
                frame.setSize(1000, 1000);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
        }

        public static void main(String[] args) {
                System.out.println("Choose the type of tree you would like to display perfromance charts for: ");
                System.out.println("1. Complete Tree");
                System.out.println("2. Random Tree");
                System.out.println("3. Skewed Tree");

                Scanner scanner = new Scanner(System.in);
                int treeType = scanner.nextInt();

                switch (treeType) {
                        case 1:
                                SwingUtilities.invokeLater(() -> new LCAChart("LCA Performance Chart", 1));
                                break;
                        case 2:
                                SwingUtilities.invokeLater(() -> new LCAChart("LCA Performance Chart", 2));
                                break;
                        case 3:
                                SwingUtilities.invokeLater(() -> new LCAChart("LCA Performance Chart", 3));
                                break;
                        default:
                                System.out.println("Invalid choice. Exiting.");
                                System.exit(1);
                }

        }
}
