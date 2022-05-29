package gui.choices;

import cards.NeutralCard;
import player.NeutralPlayer;

import javax.swing.*;

import static gui.guitools.Frame.frame;

public class ChallengeGui {
    private final int out;

    public ChallengeGui(NeutralPlayer target, NeutralCard cardPlayed) {
        this.out = JOptionPane.showConfirmDialog(frame, "Do you want to challenge " + target.getClass().getSimpleName() + " for using " + cardPlayed.getClass().getSimpleName() + "?"
                , "Challenge Option", JOptionPane.YES_NO_OPTION);
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
