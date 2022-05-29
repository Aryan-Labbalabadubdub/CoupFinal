package gui.choices;

import cards.NeutralCard;
import database.Constants;
import gui.guitools.JDialogLocked;
import gui.guitools.PanelB;
import player.NeutralPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static database.Constants.*;
import static gui.guitools.ComponentPlace.fontAndPlace;
import static gui.guitools.Convert.dimensionConverter;
import static gui.options.Board.cardGenerator;

public class EliminationGui {
    private ArrayList<NeutralCard> chosen;

    public ArrayList<NeutralCard> choice(int num, ArrayList<NeutralCard> availableChoices, NeutralPlayer player) {
        int toChooseNum = availableChoices.size() - num;

        chosen = new ArrayList<>(toChooseNum);
        for (int i = 0; i < toChooseNum; i++) {
            chosen.add(availableChoices.get(i));
        }

        JDialogLocked cardDialog = new JDialogLocked(null, null, true);
        cardDialog.setUndecorated(true);

        PanelB targets = new PanelB(null, new float[]{0, 0, 100, 100});
        targets.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);

        ArrayList<JButton> buttons = new ArrayList<>();
        for (NeutralCard card : availableChoices) {
            JButton button = cardGenerator(card.pathToPic(), cardMaxW / cardW);
            button.setFocusable(false);
            for (ActionListener actionListener : button.getActionListeners()) {
                button.removeActionListener(actionListener);
            }
            buttons.add(button);
        }
        JButton[] buttonArray = buttons.toArray(new JButton[0]);
        fontAndPlace(targets, constraints, false, buttonArray, Constants.vivaldiResized);

        ArrayList<JRadioButton> radioButtons = new ArrayList<>();
        for (NeutralCard card : availableChoices) {
            JRadioButton button = new JRadioButton(card.getClass().getSimpleName());
            button.setFocusable(false);
            button.setEnabled(true);
            button.setSelected(false);
            radioButtons.add(button);
        }
        JRadioButton[] radioButtonArray = radioButtons.toArray(new JRadioButton[0]);
        constraints.gridx = 0;
        constraints.gridy++;
        fontAndPlace(targets, constraints, false, radioButtonArray, vivaldiResized);

        for (int i = 0; i < toChooseNum; i++) {
            radioButtons.get(i).setSelected(true);
        }
        for (JRadioButton radioButton : radioButtons) {
            radioButton.addActionListener(e -> {
                if (radioButton.isSelected()) {
                    chosen.add(availableChoices.get(radioButtons.lastIndexOf(radioButton)));
                    radioButtons.get(availableChoices.lastIndexOf(chosen.get(0))).setSelected(false);
                    chosen.remove(0);
                } else {
                    radioButton.setSelected(true);
                }
            });
        }

        JButton submit = new JButton("Submit");
        submit.setFocusable(false);
        submit.addActionListener(e -> {
            cardDialog.safeDisposalAction();
            cardDialog.dispose();
        });
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = availableChoices.size();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        fontAndPlace(targets, constraints, false, new Component[]{submit}, vivaldiResized);

        cardDialog.setSize(dimensionConverter((float) (1.05 * buttons.size() * cardMaxW), (float) (1.2 * cardMaxH)));
        cardDialog.setResizable(false);
        cardDialog.setLocationRelativeTo(null);
        cardDialog.add(targets);
        cardDialog.lock();
        cardDialog.setVisible(true);
        //Waits here until the dialog is closed

        ArrayList<NeutralCard> out = new ArrayList<>();
        for (NeutralCard card : availableChoices) {
            if (!(chosen.contains(card))) {
                out.add(card);
            }
        }
        return out;
    }
}
