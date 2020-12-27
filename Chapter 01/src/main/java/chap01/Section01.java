package chap01;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.VectorGraphicsEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
//import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.CardinalPosition;
import org.knowm.xchart.style.Styler.TextAlignment;
import static org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.markers.SeriesMarkers;

import matrix.SquareMatrix;
import matrix.SystemUtil;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * For XChart:
 * Javadoc at https://knowm.org/javadocs/xchart/index.html
 * Good examples at https://knowm.org/open-source/xchart/xchart-example-code/
 */
public class Section01 {

    // Utility classes
    static class Tuple {
        double x, y;
        public Tuple(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class IndexedCoeff {
        int idx;
        double coef;
        public IndexedCoeff(int idx, double coef) {
            this.idx = idx;
            this.coef = coef;
        }
    }

    public static double mean(double[] dataset) {
        int size = dataset.length;
        double sum = Arrays.stream(dataset).sum();
        return (sum / size);
    }

    private static double deviationFromMean(double score, double mean) {
        return score - mean;
    }

    public static double variance(double[] dataset) {
        double mean = mean(dataset);
        double sqSum = Arrays.stream(dataset).map(x -> Math.pow(deviationFromMean(x, mean), 2)).sum();
        return sqSum / (dataset.length - 1);
    }

    public static double stdDev(double[] dataset) {
        return Math.sqrt(variance(dataset));
    }

    public static void main(String... args) throws Exception {
        System.out.println(String.format("Running from folder %s", System.getProperty("user.dir")));
        System.out.println(String.format("Java version %s", System.getProperty("java.version")));

        // Creating random data
        double previousY = 30d;  // Y starts here
        double yAmpl = 42d;      // Y amplitude
        List<Double> xs = new ArrayList<>();
        List<Double> ys = new ArrayList<>();
        for (double x=0; x<501; x++) {
            xs.add(x);
            double delta = Math.random() - 0.5;  // [-0.5, 0.5]
            double nextY = previousY + delta;
            if (nextY > yAmpl || nextY < 0) {
                nextY = previousY - delta;
            }
            ys.add(nextY);
            previousY = nextY;
        }

        double[] xData = xs.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
        double[] yData = ys.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();

        // Plot the data
        XYChart chart = QuickChart.getChart(
                "Stock Price",        // Chart title
                "Elapsed Time (min)", // x axis label
                "Stock Price ($)",    // y axis label
                "price",              // Legend
                xData,                // x axis data
                yData);               // y axis data

        final String FIGURES_FOLDER = "figures";
        File figuresDir = new File(FIGURES_FOLDER);
        if (!figuresDir.exists()) {
            System.out.println(String.format("Creating folder %s.", FIGURES_FOLDER));
            figuresDir.mkdirs();
        } else {
            System.out.println(String.format("%s folder already exists.", FIGURES_FOLDER));
        }

        // Save it
        BitmapEncoder.saveBitmap(chart, "./figures/chart_01", BitmapEncoder.BitmapFormat.PNG);
        VectorGraphicsEncoder.saveVectorGraphic(chart, "./figures/chart_01", VectorGraphicsEncoder.VectorGraphicsFormat.SVG);

        // Render the chart
//        BitmapEncoder.getBufferedImage(chart);

        // Alternate: Show it (in a swing frame)
        new SwingWrapper(chart).displayChart();

        // Regression
        final int REQUIRED_SMOOTHING_DEGREE = 1;
        int dimension = REQUIRED_SMOOTHING_DEGREE + 1;
        double[] sumXArray = new double[(REQUIRED_SMOOTHING_DEGREE * 2) + 1]; // Will fill the matrix
        double[] sumY      = new double[REQUIRED_SMOOTHING_DEGREE + 1];
        // init
        for (int i=0; i<((REQUIRED_SMOOTHING_DEGREE * 2) + 1); i++) {
            sumXArray[i] = 0.0;
        }
        for (int i=0; i<(REQUIRED_SMOOTHING_DEGREE + 1); i++) {
            sumY[i] = 0.0;
        }
        List<Tuple> data = new ArrayList<>();
        for (int x=0; x<xData.length; x++) {
            data.add(new Tuple(xData[x], yData[x]));
        }
        data.stream().forEach(tuple -> {
            for (int i=0; i<((REQUIRED_SMOOTHING_DEGREE * 2) + 1); i++) {
                sumXArray[i] += Math.pow(tuple.x, i);
            }
            for (int i=0; i<(REQUIRED_SMOOTHING_DEGREE + 1); i++) {
                sumY[i] += (tuple.y * Math.pow(tuple.x, i));
            }
        });

        // Resolution
        SquareMatrix squareMatrix = new SquareMatrix(dimension);
        for (int row=0; row<dimension; row++) {
            for (int col=0; col<dimension; col++) {
                int powerRnk = (REQUIRED_SMOOTHING_DEGREE - row) + (REQUIRED_SMOOTHING_DEGREE - col);
                System.out.println("[" + row + "," + col + ": deg " + (powerRnk) + "] = " + sumXArray[powerRnk]);
                squareMatrix.setElementAt(row, col, sumXArray[powerRnk]);
            }
        }
        double[] constants = new double[dimension];
        for (int i=0; i<dimension; i++) {
            constants[i] = sumY[REQUIRED_SMOOTHING_DEGREE - i];
            System.out.println("[" + (REQUIRED_SMOOTHING_DEGREE - i) + "] = " + constants[i]);
        }

        System.out.println("Resolving:");
        SystemUtil.printSystem(squareMatrix, constants);
        System.out.println();

        double[] result = SystemUtil.solveSystem(squareMatrix, constants);
        AtomicInteger integer = new AtomicInteger(0);
        // Final equation coefficients:
        Arrays.stream(result)
                .boxed()
                .map(coef -> new IndexedCoeff(integer.incrementAndGet(), coef))
                .forEach(ic -> System.out.println(String.format("Deg %d -> %f", (dimension - ic.idx), ic.coef)));
        // Calculate standard deviation
        double std = stdDev(yData);
        System.out.println(String.format("Standard Deviation: %f", std));

        List<Double> ySmoothed = new ArrayList<>();

        xs.forEach(x -> {
            double y = (x * result[0]) + result[1];
            ySmoothed.add(y);
        });

        double[] yDataSmoothed = ySmoothed.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
        double[] top = ySmoothed.stream()
                .mapToDouble(d -> d.doubleValue() + std)
                .toArray();
        double[] bottom = ySmoothed.stream()
                .mapToDouble(d -> d.doubleValue() - std)
                .toArray();

        chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("With Regression & Deviation")
                .xAxisTitle("Time")
                .yAxisTitle("Price")
                .build();

        // Customize Chart
        chart.getStyler().setLegendPosition(CardinalPosition.InsideNW);
//        chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
//        chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
        chart.getStyler().setYAxisLabelAlignment(TextAlignment.Right);
        chart.getStyler().setYAxisDecimalPattern("$ #,###.##");
        chart.getStyler().setPlotMargin(0);
        chart.getStyler().setPlotContentSize(.95);

        XYSeries seriesLiability = chart.addSeries("Raw", xData, yData);
        seriesLiability.setMarker(SeriesMarkers.NONE);  // remove the dots
        if (false) { // An option to try...
            seriesLiability.setXYSeriesRenderStyle(XYSeriesRenderStyle.Area);
//            seriesLiability.setMarker(SeriesMarkers.NONE);
        }

        XYSeries lined = chart.addSeries("Lined", xData, yDataSmoothed);
        lined.setXYSeriesRenderStyle(XYSeriesRenderStyle.Line);
        lined.setMarker(SeriesMarkers.NONE);  // remove the dots

        XYSeries top1 = chart.addSeries("Top", xData, top);
        top1.setMarker(SeriesMarkers.NONE);  // remove the dots

        XYSeries bottom1 = chart.addSeries("Bottom", xData, bottom);
        bottom1.setMarker(SeriesMarkers.NONE);  // remove the dots

        // Render the chart
//        BitmapEncoder.getBufferedImage(chart);
        new SwingWrapper(chart).displayChart();
    }
}
