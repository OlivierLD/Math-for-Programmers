package gsg.examples.override;

import gsg.SwingUtils.WhiteBoardPanel;
import gsg.VectorUtils;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * One polynomial curve
 */
public class SwingSample3 {

    private JFrame frame;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu();
    private JMenuItem menuFileExit = new JMenuItem();
    private JMenu menuHelp = new JMenu();
    private JMenuItem menuHelpAbout = new JMenuItem();

    private final static int WIDTH = 860;
    private final static int HEIGHT = 600;

    // The WhiteBoard
    private static WhiteBoardPanel whiteBoard = new WhiteBoardPanel();

    private void fileExit_ActionPerformed(ActionEvent ae) {
        System.out.println("Exit requested");
        System.exit(0);
    }
    private void helpAbout_ActionPerformed(ActionEvent ae) {
        System.out.println("Help requested");
        JOptionPane.showMessageDialog(whiteBoard, "Help would go here", "Help", JOptionPane.PLAIN_MESSAGE);
    }

    public SwingSample3() {
        // The JFrame
        frame = new JFrame("This is example #3");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        System.out.printf("Default frame width %d height %d %n", frameSize.width, frameSize.height);
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        if (frameSize.width == 0 || frameSize.height == 0) {
            frameSize = new Dimension(WIDTH, HEIGHT + 50); // 50: ... menu, title bar, etc.
            frame.setSize(frameSize);
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setJMenuBar(menuBar);
        frame.getContentPane().setLayout(new BorderLayout());
        menuFile.setText("File");
        menuFileExit.setText("Exit");
        menuFileExit.addActionListener(ae -> fileExit_ActionPerformed(ae));
        menuHelp.setText("Help");
        menuHelpAbout.setText("About");
        menuHelpAbout.addActionListener(ae -> helpAbout_ActionPerformed(ae));
        menuFile.add(menuFileExit);
        menuBar.add(menuFile);
        menuHelp.add(menuHelpAbout);
        menuBar.add(menuHelp);

        // Add the WitheBoard to the JFrame
        frame.getContentPane().add(whiteBoard, BorderLayout.CENTER);

        frame.setVisible(true); // Display
    }

    public static void main(String... args) {

        try {
            if (System.getProperty("swing.defaultlaf") == null) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        new SwingSample3();

        // Now, the Data
        java.util.List<Double> xOrig = new ArrayList<>();
        java.util.List<Double> yOrig = new ArrayList<>();
        // The function points
//        for (double i=-10; i<=10; i+=0.5) {
//            xOrig.add(i);
//            yOrig.add((0.2 * Math.pow(i, 2)) - 3);
//        }
        // The function points
        for (double i=-6; i<=15; i+=0.25) {
            xOrig.add(i);
            yOrig.add((0.01 * Math.pow(i, 3)) - (0.1 * Math.pow(i, 2)) - (0.2 * i) + 3);
        }
        double[] x = xOrig.stream().mapToDouble(Double::doubleValue).toArray();
        double[] y = yOrig.stream().mapToDouble(Double::doubleValue).toArray();

        // The points to spray x in [-6, 15], y in [-1.5, 11.25]
        xOrig.clear();
        xOrig.clear();
        for (int i=0; i<100; i++) {
            xOrig.add((Math.random() * 21) - 6);
            yOrig.add((Math.random() * 12.75) - 1.5);
        }
        double[] xPlot = xOrig.stream().mapToDouble(Double::doubleValue).toArray();
        double[] yPlot = yOrig.stream().mapToDouble(Double::doubleValue).toArray();

        VectorUtils.GraphicRange graphicRange = VectorUtils.findGraphicRange(x, y); // TODO Also integrate xPlot, yPlot
        double xAmplitude = graphicRange.getMaxX() - graphicRange.getMinX();
        double yAmplitude = graphicRange.getMaxY() - graphicRange.getMinY();

        int MARGINS = 20;

        double oneUnit = Math.min((WIDTH - (2 * MARGINS)) / xAmplitude, (HEIGHT - (2 * MARGINS)) / yAmplitude);
        System.out.println(String.format("One Unit: %f", oneUnit));

        // Transformers
        Function<Double, Integer> findCanvasXCoord = spaceXCoord -> (int)(MARGINS + (Math.round((spaceXCoord - graphicRange.getMinX()) * oneUnit)));
        Function<Double, Integer> findCanvasYCoord = spaceYCoord -> (int)(MARGINS + (Math.round((spaceYCoord - graphicRange.getMinY()) * oneUnit)));

        double x0 = findCanvasXCoord.apply(0d); // Math.round(0 - graphicRange.getMinX()) * oneUnit;
        double y0 = findCanvasYCoord.apply(0d); // Math.round(0 - graphicRange.getMinY()) * oneUnit;

//        System.out.println(String.format("y0: %f (minY: %f)", y0, graphicRange.getMinY()));

        int CIRCLE_DIAM = 2;
        Dimension dimension = new Dimension(WIDTH, HEIGHT);

        whiteBoard.setWhiteBoardWriter(g2d -> {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, dimension.width, dimension.height);

            // Actual working zone, from graphicRange
            g2d.setColor(Color.PINK); // GRAY);
            // graphicRange.getMaxX() graphicRange.getMinX() graphicRange.getMaxY() graphicRange.getMinY()
            int minX = findCanvasXCoord.apply(graphicRange.getMinX());
            int maxX = findCanvasXCoord.apply(graphicRange.getMaxX());
            int minY = findCanvasYCoord.apply(graphicRange.getMinY());
            int maxY = findCanvasYCoord.apply(graphicRange.getMaxY());
            System.out.println(String.format("Working Rectangle: x:%d, y:%d, w:%d, h:%d", minX, HEIGHT - maxY, (maxX - minX), (maxY - minY)));
            g2d.drawRect(minX, HEIGHT - maxY, (maxX - minX), (maxY - minY));

            // Label font
            int labelFontSize = 10;
            Font labelFont = new Font("Courier New", Font.PLAIN, labelFontSize);
            g2d.setFont(labelFont);

            // Vertical X (left) Arrow
            WhiteBoardPanel.drawArrow(g2d,
                    new Point((int)Math.round(x0), HEIGHT),
                    new Point((int)Math.round(x0), 0),
                    Color.BLACK);

            // X Notches, positive
            g2d.setColor(Color.BLACK);
            int xTick = 0;
            int canvasX = 0;
            while (canvasX <= WIDTH) {
                canvasX = findCanvasXCoord.apply((double)xTick);
                if (canvasX <= WIDTH) {
                    g2d.drawLine(canvasX, HEIGHT - (int) Math.round(y0 - 5),
                            canvasX, HEIGHT - (int) Math.round(y0 + 5));
                    String label = String.valueOf(xTick);
                    int strWidth = g2d.getFontMetrics(labelFont).stringWidth(label);
                    g2d.drawString(label, canvasX - (strWidth / 2),HEIGHT - (int) Math.round(y0 - 5 - (labelFont.getSize())));
                }
                xTick += 1;
            }
            // X Notches, negative
            xTick = 0;
            canvasX = WIDTH;
            while (canvasX >= 0) {
                canvasX = findCanvasXCoord.apply((double)xTick);
                if (canvasX >= 0) {
                    g2d.drawLine(canvasX, HEIGHT - (int) Math.round(y0 - 5),
                            canvasX, HEIGHT - (int) Math.round(y0 + 5));
                    String label = String.valueOf(xTick);
                    int strWidth = g2d.getFontMetrics(labelFont).stringWidth(label);
                    g2d.drawString(label, canvasX - (strWidth / 2),HEIGHT - (int) Math.round(y0 - 5 - (labelFont.getSize())));
                }
                xTick -= 1;
            }

            // Horizontal Y (bottom) Arrow
            WhiteBoardPanel.drawArrow(g2d,
                    new Point(0, HEIGHT - (int)Math.round(y0)),
                    new Point(WIDTH, HEIGHT - (int)Math.round(y0)),
                    Color.BLACK);

            // Y Notches, positive
            g2d.setColor(Color.BLACK);
            int yTick = 0;
            int canvasY = 0;
            while (canvasY <= HEIGHT) {
                canvasY = findCanvasYCoord.apply((double)yTick);
                if (canvasY <= HEIGHT) {
                    g2d.drawLine((int) Math.round(x0 - 5), HEIGHT - canvasY,
                            (int) Math.round(x0 + 5), HEIGHT - canvasY);
                    String label = String.valueOf(yTick);
                    int strWidth = g2d.getFontMetrics(labelFont).stringWidth(label);
                    g2d.drawString(label, (int) Math.round(x0 - 5) - strWidth - 2, HEIGHT - canvasY + (int)(labelFont.getSize() * 0.9 / 2));
                }
                yTick += 1;
            }
            // Y Notches, negative
            yTick = 0;
            canvasY = HEIGHT;
            while (canvasY >= 0) {
                canvasY = findCanvasYCoord.apply((double)yTick);
                if (canvasY >= 0) {
                    g2d.drawLine((int) Math.round(x0 - 5), HEIGHT - canvasY,
                            (int) Math.round(x0 + 5), HEIGHT - canvasY);
                    String label = String.valueOf(yTick);
                    int strWidth = g2d.getFontMetrics(labelFont).stringWidth(label);
                    g2d.drawString(label, (int) Math.round(x0 - 5) - strWidth - 2, HEIGHT - canvasY + (int)(labelFont.getSize() * 0.9 / 2));
                }
                yTick -= 1;
            }

            // For the text
            g2d.setColor(Color.GRAY);
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD | Font.ITALIC).deriveFont(24f));
            // (0.01 * Math.pow(i, 3)) - (0.1 * Math.pow(i, 2)) - (0.2 * i) + 3
            g2d.drawString("y = (0.01 * x^3) + (-0.1 * x^2) - (0.2 * x) + 3", 10, 60);

            /*
             *  THE DATA - The curve.
             */
            g2d.setColor(new Color(0, 0, 255, 255)); // Line Color
            g2d.setStroke(new BasicStroke(3));             // Line Thickness
            boolean withPoints = false;
            Point previous = null;
            for (int i=0; i<x.length; i++) {
                int pointX = findCanvasXCoord.apply(x[i]);
                int pointY = findCanvasYCoord.apply(y[i]);
//              System.out.println(String.format("x:%f, y:%f => X:%d, Y:%d", x[i], y[i], pointX, pointY));
                Point here = new Point(pointX, pointY);
                if (withPoints) {
                    g2d.fillOval(pointX - (CIRCLE_DIAM / 2),
                            HEIGHT - pointY - (CIRCLE_DIAM / 2),
                            CIRCLE_DIAM, CIRCLE_DIAM);
                }
                if (previous != null) {
                    g2d.drawLine(previous.x, HEIGHT - previous.y, here.x, HEIGHT - here.y);
                }
                previous = here;
            }

            // Plot data
            g2d.setColor(new Color(255, 0, 0, 125)); // Plot Color
            int circleDiam = 10;
            for (int i=0; i<xPlot.length; i++) {
                int pointX = findCanvasXCoord.apply(xPlot[i]);
                int pointY = findCanvasYCoord.apply(yPlot[i]);
//              System.out.println(String.format("x:%f, y:%f => X:%d, Y:%d", x[i], y[i], pointX, pointY));
                g2d.fillOval(pointX - (circleDiam / 2),
                        HEIGHT - pointY - (circleDiam / 2),
                        circleDiam, circleDiam);
            }
        });
//      whiteBoard.getImage(); // This is for the Notebook
        whiteBoard.repaint();  // This is for a pure Swing context
    }
}
