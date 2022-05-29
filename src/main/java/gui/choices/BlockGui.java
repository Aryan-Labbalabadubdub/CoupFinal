package gui.choices;

import cards.NeutralCard;

import javax.swing.*;

import static gui.guitools.Frame.frame;

public class BlockGui {
    private final int out;

    public BlockGui(NeutralCard card) {
        if (!card.getClass().equals(NeutralCard.class)) {
            this.out = JOptionPane.showConfirmDialog(frame, "Do you want to block " + card.getCardOperator().getClass().getSimpleName()
                    + " on incoming act of " + card.getClass().getSimpleName() + " at all due costs?", "Block Option", JOptionPane.YES_NO_OPTION);
        } else {
            this.out = JOptionPane.showConfirmDialog(frame, "Do you want to block " + card.getCardOperator().getClass().getSimpleName() + " " +
                    "upon receiving foreign aid at all due costs?", "Block Option", JOptionPane.YES_NO_OPTION);
        }
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
