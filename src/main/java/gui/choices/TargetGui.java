package gui.choices;

import board.GameBoard;
import gui.guitools.JDialogLocked;
import gui.guitools.PanelB;
import player.NeutralPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static database.Constants.*;
import static database.Database.mainPlayer;
import static gui.guitools.ComponentPlace.fontAndPlace;
import static gui.guitools.Convert.dimensionConverter;
import static gui.options.Board.cardGenerator;

public class TargetGui {
    volatile NeutralPlayer chosen = null;

    public NeutralPlayer choose() {
        JDialogLocked cardDialog = new JDialogLocked(null, null, true);
        cardDialog.setUndecorated(true);

        PanelB targets = new PanelB(null, new float[]{0, 0, 100, 100});
        targets.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);

        ArrayList<JButton> buttons = new ArrayList<>();
        for (NeutralPlayer player : GameBoard.getInstance().getActivePlayers()) {
            if (!player.equals(mainPlayer)) {
                JButton button = cardGenerator(player.pathToPic(), cardMaxW / cardW);
                for (ActionListener actionListener : button.getActionListeners()) {
                    button.removeActionListener(actionListener);
                }
                button.addActionListener(e -> {
                    chosen = player;
                    cardDialog.safeDisposalAction();
                    cardDialog.dispose();
                });
                buttons.add(button);
            }
        }
        JButton[] buttonArray = buttons.toArray(new JButton[0]);
        fontAndPlace(targets, constraints, false, buttonArray, publicFont);

        cardDialog.setSize(dimensionConverter((float) (1.05 * buttons.size() * cardMaxW), (float) (1.05 * cardMaxH)));
        cardDialog.setResizable(false);
        cardDialog.setLocationRelativeTo(null);
        cardDialog.add(targets);
        cardDialog.lock();
        cardDialog.setVisible(true);
        //Waits here until the dialog is closed
        return chosen;
    }
}
