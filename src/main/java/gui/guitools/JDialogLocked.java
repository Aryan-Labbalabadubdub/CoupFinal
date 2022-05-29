package gui.guitools;

import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;

import static database.Constants.frameRefreshRateMS;
import static gui.guitools.Frame.frame;
import static gui.guitools.Frame.glassPane;

public class JDialogLocked extends JDialog {
    private boolean open = true;
    private Point location;

    public JDialogLocked() {
        super();
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                safeDisposalAction();
            }
        });
    }

    public JDialogLocked(java.awt.Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                safeDisposalAction();
            }
        });
    }

    public void JDialogLockedAction() throws NoSuchAlgorithmException, InterruptedException {
    }

    public void lock() {
        location = this.getLocation();
        glassPane.setVisible(true);
        frame.setEnabled(false);
        Thread lockThread = new Thread(() -> {
            while (open) {
                this.setLocation(location);
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

    public void safeDisposalAction() {
        open = false;
        glassPane.setVisible(false);
        frame.setEnabled(true);
        try {
            JDialogLockedAction();
        } catch (NoSuchAlgorithmException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Component add(Component comp) {
        Component component = super.add(comp);
        this.revalidate();
        this.repaint();
        return component;
    }

    public void dispatch() {
        for (Component component : this.getContentPane().getComponents()) {
            if (component instanceof Container) {
                for (Component component1 : ((Container) component).getComponents()) {
                    component1.setEnabled(false);
                }
            } else {
                component.setEnabled(false);
            }
        }
    }
}
