package chap02.SwingUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

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

    public BufferedImage getImage() {
        Dimension size = this.getSize();
        final BufferedImage bufferedImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        // Create a graphics contents on the buffered image
        Graphics2D g2d = bufferedImage.createGraphics();
        this.paintComponent((Graphics) g2d);
        return bufferedImage;
    }
}
