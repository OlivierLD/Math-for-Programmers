package chap02;

import chap02.SwingUtils.WhiteBoardPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;

public class SwingSample2 {

    private JFrame frame;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu();
//    private JMenuItem menuFileCustomAction = new JMenuItem();
    private JMenuItem menuFileExit = new JMenuItem();
    private JMenu menuHelp = new JMenu();
    private JMenuItem menuHelpAbout = new JMenuItem();

    private final static int WIDTH = 860;
    private final static int HEIGHT = 600;

    private static WhiteBoardPanel whiteBoard = new WhiteBoardPanel(g2d -> {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, WIDTH, HEIGHT); // Hard coded dimensions for that one.
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(30f));
        g2d.setColor(Color.ORANGE);
        g2d.drawString("This is your white board!", 10, 40);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(16f));
        String message = "Use the WhiteBoardPanel.setWhiteBoardWriter method.";
        g2d.drawString(message, 40, 80);
    });

//    private void customAction_ActionPerformed(ActionEvent ae) {
//        System.out.println("Custom Action requested - Change repaint, take a snapshot");
//        File snap = new File("snap.jpg");
//        Dimension dimension = whiteBoard.getSize();
//        whiteBoard.setWhiteBoardWriter(g2d -> {
//            g2d.setColor(Color.RED);
//            g2d.fillRect(0, 0, dimension.width, dimension.height);
//            g2d.setFont(new Font("courier", Font.BOLD | Font.ITALIC, 32));
//            g2d.setColor(Color.PINK);
//            g2d.drawString("This is your white board!", 10, 40);
//            g2d.setColor(Color.LIGHT_GRAY);
//            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(16f));
//            String message = "Use the WhiteBoardPanel.setWhiteBoardWriter method.";
//            g2d.drawString(message, 40, 80);
//            g2d.setColor(Color.BLACK);
//            g2d.drawOval(50, 100, dimension.width - 100, dimension.height - 200);
//            // Arrow
//            WhiteBoardPanel.drawArrow(g2d,
//                    new Point(400, 300),
//                    new Point(300, 200),
//                    Color.WHITE);
//        });
//        whiteBoard.repaint();
//        whiteBoard.createImage(snap, "jpg", dimension.width, dimension.height);
//    }
    private void fileExit_ActionPerformed(ActionEvent ae) {
        System.out.println("Exit requested");
    }
    private void helpAbout_ActionPerformed(ActionEvent ae) {
        System.out.println("Help requested");
    }

    private void jbInit() throws Exception {
        frame.setJMenuBar(menuBar);
        frame.getContentPane().setLayout(new BorderLayout());
        menuFile.setText("File");
//        menuFileCustomAction.setText("Custom Action");
//        menuFileCustomAction.addActionListener(ae -> customAction_ActionPerformed(ae));
//        menuFile.add(menuFileCustomAction);
        menuFileExit.setText("Exit");
        menuFileExit.addActionListener(ae -> fileExit_ActionPerformed(ae));
        menuHelp.setText("Help");
        menuHelpAbout.setText("About");
        menuHelpAbout.addActionListener(ae -> helpAbout_ActionPerformed(ae));
        menuFile.add(menuFileExit);
        menuBar.add(menuFile);
        menuHelp.add(menuHelpAbout);
        menuBar.add(menuHelp);

        frame.getContentPane().add(whiteBoard, BorderLayout.CENTER);
    }

    public SwingSample2() {
        frame = new JFrame("This is an example");
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
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        frame.setVisible(true);

    }

    public static void main(String... args) {

        try {
            if (System.getProperty("swing.defaultlaf") == null) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

     /* SwingSample swingSample = */ new SwingSample2();

        // Get the range here
        VectorUtils.Vector2D one = new VectorUtils.Vector2D(-1, -2);
        VectorUtils.Vector2D two = new VectorUtils.Vector2D(3, 4);
        VectorUtils.Vector2D three = VectorUtils.toPolar(one);
        VectorUtils.Vector2D four = new VectorUtils.Vector2D(5, 6);

        VectorUtils.GraphicRange graphicRange = VectorUtils.findGraphicRange(one, two, three, four);
        double xAmplitude = graphicRange.getMaxX() - graphicRange.getMinX();
        double yAmplitude = graphicRange.getMaxY() - graphicRange.getMinY();

        double oneUnit = Math.min(WIDTH / xAmplitude, HEIGHT / yAmplitude);
        System.out.println(String.format("One Unit: %f", oneUnit));

        double x0 = Math.round(0 - graphicRange.getMinX()) * oneUnit;
        double y0 = Math.round(0 - graphicRange.getMinY()) * oneUnit;

        System.out.println(String.format("y0: %f (minY: %f)", y0, graphicRange.getMinY()));

        int CIRCLE_DIAM = 30;
        Dimension dimension = new Dimension(WIDTH, HEIGHT);

        whiteBoard.setWhiteBoardWriter(g2d -> {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, dimension.width, dimension.height);
            // Vertical (left) Arrow
            WhiteBoardPanel.drawArrow(g2d,
                    new Point((int)Math.round(x0), HEIGHT),
                    new Point((int)Math.round(x0), 0),
                    Color.BLACK);

            // X Notches
            g2d.setColor(Color.BLACK);
            int xTick = 0;
            while (xTick < WIDTH) {
                // TODO Use X values, translated
                g2d.drawLine(xTick, HEIGHT - (int)Math.round(y0 - 5),
                        xTick, HEIGHT - (int)Math.round(y0 + 5));
                xTick = (int)Math.round(xTick + oneUnit);
            }

            // Horizontal (bottom) Arrow
            WhiteBoardPanel.drawArrow(g2d,
                    new Point(0, HEIGHT - (int)Math.round(y0)),
                    new Point(WIDTH, HEIGHT - (int)Math.round(y0)),
                    Color.BLACK);

            // Y Notches
            g2d.setColor(Color.BLACK);
            int yTick = 0;
            while (yTick < HEIGHT) {
                // TODO Use Y values, translated
                g2d.drawLine((int)Math.round(x0 - 5), HEIGHT - yTick,
                        (int)Math.round(x0 + 5), HEIGHT - yTick);
                yTick = (int)Math.round(yTick + oneUnit);
            }

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD | Font.ITALIC).deriveFont(30f));

            // Points, Vectors
            g2d.setColor(new Color(0, 0, 255, 125)); // blue
            int pointX = (int)Math.round((one.getX() - graphicRange.getMinX()) * oneUnit);
            int pointY = (int)Math.round((one.getY() - graphicRange.getMinY()) * oneUnit);
            g2d.fillOval(pointX - (CIRCLE_DIAM / 2),
                    HEIGHT - pointY - (CIRCLE_DIAM / 2),
                    CIRCLE_DIAM, CIRCLE_DIAM);
            WhiteBoardPanel.drawArrow(g2d,
                    new Point((int)Math.round(x0), HEIGHT - (int)Math.round(y0)),
                    new Point(pointX, HEIGHT - pointY),
                    new Color(0, 0, 255, 125));

            g2d.setColor(new Color(255, 0, 0, 125)); // red
            pointX = (int)Math.round((two.getX() - graphicRange.getMinX()) * oneUnit);
            pointY = (int)Math.round((two.getY() - graphicRange.getMinY()) * oneUnit);
            g2d.fillOval(pointX - (CIRCLE_DIAM / 2),
                    HEIGHT - pointY - (CIRCLE_DIAM / 2),
                    CIRCLE_DIAM, CIRCLE_DIAM);
            WhiteBoardPanel.drawArrow(g2d,
                    new Point((int)Math.round(x0), HEIGHT - (int)Math.round(y0)),
                    new Point(pointX, HEIGHT - pointY),
                    new Color(255, 0, 0, 125));

            g2d.setColor(new Color(0, 255, 0, 125)); // green
            pointX = (int)Math.round((three.getX() - graphicRange.getMinX()) * oneUnit);
            pointY = (int)Math.round((three.getY() - graphicRange.getMinY()) * oneUnit);
            g2d.fillOval(pointX - (CIRCLE_DIAM / 2),
                    HEIGHT - pointY - (CIRCLE_DIAM / 2),
                    CIRCLE_DIAM, CIRCLE_DIAM);
            WhiteBoardPanel.drawArrow(g2d,
                    new Point((int)Math.round(x0), HEIGHT - (int)Math.round(y0)),
                    new Point(pointX, HEIGHT - pointY),
                    new Color(0, 255, 0, 125));

            g2d.setColor(new Color(0, 255, 255, 125)); // cyan
            pointX = (int)Math.round((four.getX() - graphicRange.getMinX()) * oneUnit);
            pointY = (int)Math.round((four.getY() - graphicRange.getMinY()) * oneUnit);
            g2d.fillOval(pointX - (CIRCLE_DIAM / 2),
                    HEIGHT - pointY - (CIRCLE_DIAM / 2),
                    CIRCLE_DIAM, CIRCLE_DIAM);
            WhiteBoardPanel.drawArrow(g2d,
                    new Point((int)Math.round(x0), HEIGHT - (int)Math.round(y0)),
                    new Point(pointX, HEIGHT - pointY),
                    new Color(0, 255, 255, 125));
        });
//        whiteBoard.getImage(); // This is for the Notebook
        whiteBoard.repaint(); /// This is for a pure Swing context

//        double perimeter = VectorUtils.perimeter(Arrays.asList(one, two, three, four));

    }
}
