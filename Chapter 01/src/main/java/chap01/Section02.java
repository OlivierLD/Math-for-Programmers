package chap01;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.BasicStroke;
import java.util.ArrayList;
import java.util.List;

public class Section02 {

    private static double[] mileages = new double[]{4.1429, 8.9173, 6.5, 6.0601, 12.3, 6.2, 2.5782, 0.9, 1.7, 13.1045, 24.7, 9.2699, 17.2, 10.0, 10.0, 2.8, 12.3773, 19.6, 7.3397, 2.1178, 12.9886, 10.9884, 16.9, 6.0, 12.9, 8.1936, 10.5, 8.0713, 1.7, 10.0, 15.6097, 17.0, 16.7, 5.6, 11.3, 19.9, 9.6, 21.6, 20.3};
    private static double[] prices = new double[]{16_980.0, 15_973.0, 9_900.0, 15_998.0, 3_900.0, 12_540.0, 21_688.0, 17_086.0, 23_000.0, 8_900.0, 3_875.0, 10_500.0, 3_500.0, 26_992.0, 17_249.0, 19_627.0, 9_450.0, 3_000.0, 14_999.0, 24_990.0, 7_967.0, 7_257.0, 4_799.0, 13_982.0, 5_299.0, 14_310.0, 7_800.0, 12_250.0, 23_000.0, 14_686.0, 7_495.0, 4_950.0, 3_500.0, 11_999.0, 9_600.0, 1_999.0, 4_300.0, 3_500.0, 4_200.0};

    private static double price(double mileage) {
        return 26_500 * Math.pow(0.905, mileage);
    }

    public static void main(String... args) {
        System.out.println(String.format("Running from folder %s", System.getProperty("user.dir")));
        System.out.println(String.format("Java version %s", System.getProperty("java.version")));

        List<Double> xList = new ArrayList<>();
        for (int x = 0; x <= 25; x++) {
            xList.add((double) x);
        }
        double[] xs = xList.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();

        List<Double> yList = new ArrayList<>();
        for (int x = 0; x < xs.length; x++) {
            yList.add(price(xs[x]));
        }
        double[] ys = yList.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();

        // Plot the data
        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Prices and Mileage")
                .xAxisTitle("Mileage (10,000 of miles)")
                .yAxisTitle("Price ($)")
                .build();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.CardinalPosition.InsideNE);

        XYSeries seriesLiability = chart.addSeries("Mileage (theory)", xs, ys);
        seriesLiability.setMarker(SeriesMarkers.NONE);
        seriesLiability.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Scattered data
        XYSeries scattered = chart.addSeries("Actual", mileages, prices);
        scattered.setMarker(SeriesMarkers.CIRCLE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setMarkerSize(16);

        // Render the chart
//      BitmapEncoder.getBufferedImage(chart);
        new SwingWrapper(chart).displayChart();

        double targetMileage = Math.log(10 / 26.5) / Math.log(0.905);
        double targetPrice = price(targetMileage);
        System.out.println(String.format("Target %f (10,000s of miles), price: %.02f$", targetMileage, targetPrice));

        double xTargetFrom = 0;
        double xTargetTo   = 25;
        XYSeries target1 = chart.addSeries("Target-Y",
                new double[] {xTargetFrom, xTargetTo},
                new double[] {targetPrice, targetPrice});
        target1.setMarker(SeriesMarkers.NONE);
        target1.setLineColor(XChartSeriesColors.RED);
        target1.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        BasicStroke dottedStroke = new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{5f, 5f}, 0f);
        target1.setLineStyle(dottedStroke);
        XYSeries target2 = chart.addSeries("Target-X",
                new double[] {targetMileage, targetMileage},
                new double[] {0, targetPrice});
        target2.setMarker(SeriesMarkers.NONE);
        target2.setLineColor(XChartSeriesColors.RED);
        target2.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        target2.setLineStyle(dottedStroke);

        new SwingWrapper(chart).displayChart();
    }
}
