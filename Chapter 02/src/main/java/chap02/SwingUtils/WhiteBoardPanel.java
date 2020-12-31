package chap02.SwingUtils;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * A Reusable WhiteBoard
 * Suitable for Swing application, as Jupyter Notebooks (with iJava)
 */
public class WhiteBoardPanel extends JPanel {

    private Consumer<Graphics2D> whiteBoardWriter = g2d -> {
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(30f));
        g2d.setColor(Color.RED);
        g2d.drawString("This is your white board!", 10, 40);
        g2d.setColor(Color.BLUE);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(16f));
        String message = "Override the overridden paintComponent method in JPanel";
        g2d.drawString(message, 40, 80);
    };

    public WhiteBoardPanel() {
    }

    public WhiteBoardPanel(Consumer<Graphics2D> whiteBoardWriter) {
        this();
        this.whiteBoardWriter = whiteBoardWriter;
    }

    public void setWhiteBoardWriter(Consumer<Graphics2D> whiteBoardWriter) {
        this.whiteBoardWriter = whiteBoardWriter;
    }

    @Override
    protected void paintComponent(Graphics g) {
//        System.out.println("paintComponent invoked on JPanel");
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        whiteBoardWriter.accept(g2d);
    }

    public void createImage(File f, String ext, int w, int h) {
        int width = w;
        int height = h;

        // Create a buffered image in which to draw
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final WhiteBoardPanel instance = this;
        Thread refreshThread = new Thread(() -> {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    // Create a graphics contents on the buffered image
                    Graphics2D g2d = bufferedImage.createGraphics();
                    instance.paintComponent((Graphics) g2d);
                    // Write generated image to a file
                    try {
                        OutputStream os = new FileOutputStream(f);
                        ImageIO.write(bufferedImage, ext, os);
                        os.flush();
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Graphics context no longer needed so dispose it
                    g2d.dispose();
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        refreshThread.start();
    }

    public static void drawArrow(Graphics2D g, Point from, Point to, Color c) {
        drawArrow(g, from, to, c, 30);
    }

    public static void drawArrow(Graphics2D g, Point from, Point to, Color c, int hl) {
        Color orig = null;
        if (g != null) {
            orig = g.getColor();
        }
        int headLength = hl;
        double headHalfAngle = 15D;

        double dir = getDir((float) (from.x - to.x), (float) (to.y - from.y));
//      System.out.println("Dir:" + dir);

        g.setColor(c);
        Point left = new Point((int) (to.x - (headLength * Math.cos(Math.toRadians(dir - 90 + headHalfAngle)))),
                (int) (to.y - (headLength * Math.sin(Math.toRadians(dir - 90 + headHalfAngle)))));
        Point right = new Point((int) (to.x - (headLength * Math.cos(Math.toRadians(dir - 90 - headHalfAngle)))),
                (int) (to.y - (headLength * Math.sin(Math.toRadians(dir - 90 - headHalfAngle)))));

        g.drawLine(from.x, from.y, to.x, to.y);
        Polygon head = new Polygon(new int[]{to.x, left.x, right.x}, new int[]{to.y, left.y, right.y}, 3);
        g.fillPolygon(head);

        if (g != null) {
            g.setColor(orig);
        }
    }

    // TODO Change that to atan2
    public static double getDir(float x, float y) {
        double dir = 0.0D;
        if (y != 0) {
            dir = Math.toDegrees(Math.atan((double) x / (double) y));
        }
        if (x <= 0 || y <= 0) {
            if (x > 0 && y < 0) {
                dir += 180D;
            } else if (x < 0 && y > 0) {
                dir += 360D;
            } else if (x < 0 && y < 0) {
                dir += 180D;
            } else if (x == 0) {
                if (y > 0) {
                    dir = 0.0D;
                } else {
                    dir = 180D;
                }
            } else if (y == 0) {
                if (x > 0) {
                    dir = 90D;
                } else {
                    dir = 270D;
                }
            }
        }
        dir += 180D;
        while (dir >= 360D) {
            dir -= 360D;
        }
        return dir;
    }

    /**
     * Call this from a Jupyter Notebook (iJava) !
     * @return The expected BufferedImage
     */
    public BufferedImage getImage() {
        Dimension size = this.getSize();
        final BufferedImage bufferedImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        // Create a graphics contents on the buffered image
        Graphics2D g2d = bufferedImage.createGraphics();
        this.paintComponent((Graphics) g2d);
        return bufferedImage;
    }
}
