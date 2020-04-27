package ac.ucl.statistics.text_processor;

import java.awt.Color;
import java.awt.BasicStroke;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import ac.ucl.statistics.model.Word;
import ac.ucl.utility.ConfigurationReader;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;


import static org.jfree.chart.ChartUtils.saveChartAsJPEG;

/**
 * This class helps to generate the Zipfs Law pattern in a chart
 * for both the passage collection and actual Zipfs Law values.
 */
public class ZipfsPatternChartBuilder {

    private JFreeChart xylineChart;

    public ZipfsPatternChartBuilder(List<Word> words) {
        String chartTitle = "Zipf's Law Pattern Comparision";
        XYPlot plot = getPlot(words);
        XYLineAndShapeRenderer renderer = getRender();
        plot.setRenderer(renderer);
        this.xylineChart = new JFreeChart(
                chartTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        //ChartPanel chartPanel = new ChartPanel(xylineChart);
        //chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
    }

    public void generateChartAndSaveToFile() {
        int width = 640;   /* Width of the image */
        int height = 480;  /* Height of the image */

        String chartFile = ConfigurationReader.getPropertyByName("Zipfs_Law_Pattern");
        File XYChart = new File(chartFile);
        try {
            ChartUtils.saveChartAsJPEG(XYChart, xylineChart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create XYPlot for the line chart
     *
     * @return An XYPlot with axises and data.
     */
    private XYPlot getPlot(List<Word> words) {
        XYDataset data = createDataset(words);
        NumberAxis xAxis = new NumberAxis("Rank (by decreasing frequency)");
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        xAxis.setRange(1, 100);
        LogAxis yAxis = new LogAxis("Probability (of occurrence)");
        yAxis.setBase(10);
        //yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        XYPlot plot = new XYPlot(data, xAxis, yAxis, new XYLineAndShapeRenderer(true, false));
        return plot;
    }

    /**
     * Create a XY Shape render
     *
     * @return XY shape render.
     */
    private XYLineAndShapeRenderer getRender() {
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        return renderer;
    }

    /**
     * Initialize the dataset containing to 100 both actual Zipf's Law values and words from the passage collection.
     *
     * @return XYDataset.
     */
    private XYDataset createDataset(List<Word> words) {
        final XYSeries actualZipfs = new XYSeries("Words(assuming c=0.10)");
        Map<Integer, Double> zipMap = getZipfsLawValue();
        zipMap.forEach((K, V) -> {
            actualZipfs.add(K, V);
        });
        final XYSeries passageCollectionZipfs = new XYSeries("Words (passage collection)");
        for (Word w : words) {
            passageCollectionZipfs.add(w.getRank(), w.getProbability());
        }
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(actualZipfs);
        dataset.addSeries(passageCollectionZipfs);
        return dataset;
    }

    /**
     * Generates actual Zipf's law values assuming the constant c=0.10.
     *
     * @return A map in the form of (rank->probability) pair.
     */
    private Map<Integer, Double> getZipfsLawValue() {
        Map<Integer, Double> zipsfRank = new HashMap<>();
        double constant_c = 0.1;
        for (int i = 1; i < 101; i++) {
            double probability = (constant_c / i);
            zipsfRank.put(i, probability);
        }
        return zipsfRank;
    }


}