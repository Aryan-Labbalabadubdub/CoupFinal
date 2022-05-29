package gui.guitools;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

import static database.Constants.*;

abstract public class Frame extends JFrame {
    public static Container glassPane;
    public static JFrame frame = new JFrame("COUP");
    public static Dimension rawScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static Dimension screenSize = new Dimension(rawScreenSize.height, rawScreenSize.height);
    private static Point location;

    public static void makeFrame() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        initializeGlassPane();
        JFrame.setDefaultLookAndFeelDecorated(true);
        UIManager.setLookAndFeel(theme);
        frame.setVisible(true);
        frame.setSize(screenSize);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUIFont(publicFont);
        location = frame.getLocation();
        lock();
    }

    public static void setUIFont(Font font) {
        javax.swing.plaf.FontUIResource uiFont = new javax.swing.plaf.FontUIResource(font);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, uiFont);
        }
    }

    public static void lock() {
        Thread lockThread = new Thread(() -> {
            while (true) {
                frame.setLocation(location);
                try {
                    Thread.sleep(frameRefreshRateMS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        lockThread.setDaemon(true);
        lockThread.start();
    }

    public static void initializeGlassPane() {
        frame.setGlassPane(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(darkenColor);
                g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
            }
        });
        glassPane = (Container) frame.getGlassPane();
    }

    @Override
    public void setContentPane(Container contentPane) {
        removeAll();
        super.setContentPane(contentPane);
        revalidate();
        repaint();
    }
}
