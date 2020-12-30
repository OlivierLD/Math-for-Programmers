package chap01;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.image.BufferedImage;
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
        if (true) {
            XYChart chart1 = new XYChartBuilder()
                    .width(800)
                    .height(600)
                    .title("Prices and Mileage")
                    .xAxisTitle("Mileage (1,000 of miles)")
                    .yAxisTitle("Price ($)")
                    .build();

            // Customize Chart
            chart1.getStyler().setLegendPosition(Styler.CardinalPosition.InsideNE);

            XYSeries seriesLiability = chart1.addSeries("Mileage (theory)", xs, ys);
            seriesLiability.setMarker(SeriesMarkers.NONE);
            seriesLiability.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

            // Scattered data
            XYSeries scattered = chart1.addSeries("Actual", mileages, prices);
            scattered.setMarker(SeriesMarkers.CIRCLE);
            chart1.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
            chart1.getStyler().setMarkerSize(16);

            // Render the chart
//      BitmapEncoder.getBufferedImage(chart);
            new SwingWrapper<>(chart1).displayChart();
        }

        // Plot the data, updated
        XYChart chart2 = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Prices and Mileage, plus target")
                .xAxisTitle("Mileage (1,000 of miles)")
                .yAxisTitle("Price ($)")
                .build();

        // Customize Chart
        chart2.getStyler().setLegendPosition(Styler.CardinalPosition.InsideNE);

        XYSeries seriesLiability2 = chart2.addSeries("Mileage (theory)", xs, ys);
        seriesLiability2.setMarker(SeriesMarkers.NONE);
        seriesLiability2.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Scattered data
        XYSeries scattered2 = chart2.addSeries("Actual", mileages, prices);
        scattered2.setMarker(SeriesMarkers.CIRCLE);
        chart2.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart2.getStyler().setMarkerSize(16);

        // New data
        double kBudget = 10d; // 10 000 $
        double targetMileage = Math.log(kBudget / 26.5) / Math.log(0.905); // find X
        double targetPrice = price(targetMileage);
        System.out.println(String.format("Target %f (10,000s of miles), price: %.02f$", targetMileage, targetPrice));

        double xTargetFrom = 0;
        double xTargetTo = 25;
        XYSeries target1 = chart2.addSeries("Target-Y (Budget)",
                new double[]{xTargetFrom, xTargetTo},
                new double[]{targetPrice, targetPrice});
        target1.setMarker(SeriesMarkers.NONE);
        target1.setLineColor(XChartSeriesColors.RED);
        target1.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        BasicStroke dottedStroke = new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{5f, 5f}, 0f);
        target1.setLineStyle(dottedStroke);
        XYSeries target2 = chart2.addSeries("Target-X (the deal)",
                new double[]{targetMileage, targetMileage},
                new double[]{0, targetPrice});
        target2.setMarker(SeriesMarkers.NONE);
        target2.setLineColor(XChartSeriesColors.PURPLE);
        target2.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        target2.setLineStyle(dottedStroke);
        target2.setLabel("Here is your deal!");

//        BufferedImage bufferedImage = BitmapEncoder.getBufferedImage(chart2);
        SwingWrapper<XYChart> xyChartSwingWrapper = new SwingWrapper<>(chart2);
        JFrame jFrame = xyChartSwingWrapper.displayChart();
        
        // Below: no impact...
        Container contentPane = jFrame.getContentPane();
        System.out.println("Content Pane is a " + contentPane.getClass().getName());

        Component[] components = contentPane.getComponents();
        System.out.println(">> There are " + components.length + " components under the contentPane");

        Component component = components[0];
        Graphics graphics = component.getGraphics();
        Graphics2D g2d = (Graphics2D) graphics;
        System.out.println(String.format("Content Pane dim: %d x %d",
                component.getWidth(),
                component.getHeight()));
        g2d.setColor(Color.MAGENTA);
        System.out.println("Font is " + graphics.getFont());
        g2d.setFont(graphics.getFont().deriveFont(30).deriveFont(Font.BOLD));
        g2d.drawString("Final Result", 200, 300);
        component.repaint();

        jFrame.setTitle("Final Result!");
        jFrame.repaint();
    }
}
