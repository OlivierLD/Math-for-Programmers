package gsg.examples.override;

import examples.globe.CommandPanel;
import gsg.SwingUtils.WhiteBoardPanel;
import gsg.VectorUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static gsg.VectorUtils.Vector2D;

/**
 * Something completely different
 * Importing an existing external Component.
 */
public class SwingGlobeSample {

    private JFrame frame;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu();
    private JMenuItem menuFileExit = new JMenuItem();
    private JMenu menuHelp = new JMenu();
    private JMenuItem menuHelpAbout = new JMenuItem();

    private static CommandPanel commandPanel;

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
        JOptionPane.showMessageDialog(whiteBoard, "This is a Globe, 100% written in Swing", "Help", JOptionPane.PLAIN_MESSAGE);
    }

    public SwingGlobeSample() {
        // The JFrame
        frame = new JFrame("This is a Globe example");
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
        commandPanel = new CommandPanel(null);
        whiteBoard.setLayout(new BorderLayout());
        whiteBoard.add(commandPanel, BorderLayout.CENTER);

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

        new SwingGlobeSample();

        // Now, the Data
        whiteBoard.setWhiteBoardWriter(g2d -> {
//            g2d.setBackground(Color.WHITE);
            commandPanel.chartPanelPaintComponent(g2d);
            // commandPanel.repaint();
        });
//      whiteBoard.getImage(); // This is for the Notebook
        whiteBoard.repaint();  // This is for a pure Swing context
    }
}
