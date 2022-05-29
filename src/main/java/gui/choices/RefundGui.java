package gui.choices;

import javax.swing.*;

import static database.Database.mainPlayer;
import static gui.guitools.Frame.frame;

public class RefundGui {
    private final int out;

    public RefundGui() {
        this.out = JOptionPane.showConfirmDialog(frame, "Do you want to be refunded for " + mainPlayer.getMoveCost() + " coins?", "Refund Option", JOptionPane.YES_NO_OPTION);
    }

    public boolean output() {
        switch (out) {
            case 0 -> {
                return true;
            }
            case 1 -> {
                return false;
            }
        }
        return false;
    }
}
