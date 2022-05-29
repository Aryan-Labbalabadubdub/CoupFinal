package gui.options;

import database.JsonOperator;
import gui.guitools.Frame;
import gui.guitools.PanelB;
import player.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static database.Constants.*;
import static database.Database.*;
import static gui.Start.startMenu;
import static gui.guitools.ComponentPlace.fontAndPlace;
import static gui.guitools.Convert.dimensionConverter;
import static gui.options.Board.cardGenerator;

public class OpponentBots {
    final List<NeutralPlayer> botTypes = List.of(new Imposter(), new Paranoid(), new Grandmaster(), new Couper());
    ArrayList<NeutralPlayer> chosenBots = getInlinePlayers();
    GridBagConstraints constraints = new GridBagConstraints();
    PanelB basePanel = new PanelB(new ImageIcon(pathToBackground).getImage(), new float[]{0, 0, 100, 100});

    public OpponentBots() {
        basePanel.setSize(Frame.frame.getSize());
        basePanel.fadeBackground(OPACITY);

        basePanel.setLayout(new GridBagLayout());
        constraints.insets = new Insets(5, 5, 5, 5);

        makeTitle();
        makeBotImages();
        makeBotNames();
        makeSubmitButton();
        Frame.frame.setContentPane(basePanel);
    }

    public void makeTitle() {
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = botTypes.size();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JLabel label = new JLabel("Choose your opponents");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBackground(acidGreen);
        label.setOpaque(true);
        fontAndPlace(basePanel, constraints, false, new Component[]{label}, vivaldiHuge, dimensionConverter(95, 10));
    }

    public void makeBotImages() {
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        List<JButton> botImages = new ArrayList<>(botTypes.size());
        for (NeutralPlayer type : botTypes) {
            JButton botImage = cardGenerator(type.pathToPic(), 2f);
            for (ActionListener actionListener : botImage.getActionListeners()) {
                botImage.removeActionListener(actionListener);
            }
            botImage.setFocusable(false);
            botImages.add(botImage);
        }
        fontAndPlace(basePanel, constraints, false, botImages.toArray(new Component[0]), vivaldiHuge);
    }

    public void makeBotNames() {
        constraints.gridx = 0;
        constraints.gridy++;
        List<JRadioButton> botNames = new ArrayList<>(botTypes.size());
        for (NeutralPlayer type : botTypes) {
            JRadioButton botName = new JRadioButton(type.getClass().getSimpleName());
            botName.setFocusable(false);
            botNames.add(botName);
        }
        for (JRadioButton radioButton : botNames) {
            for (NeutralPlayer player : inlinePlayers) {
                if (botTypes.get(botNames.lastIndexOf(radioButton)).getClass().equals(player.getClass())) {
                    radioButton.setSelected(true);
                    break;
                }
            }
        }
        for (JRadioButton radioButton : botNames) {
            radioButton.setOpaque(true);
            radioButton.addActionListener(e -> {
                if (radioButton.isSelected()) {
                    chosenBots.add(botTypes.get(botNames.lastIndexOf(radioButton)));

                    NeutralPlayer found = null;
                    for (NeutralPlayer bot : botTypes) {
                        if (bot.getClass().equals(chosenBots.get(0).getClass())) {
                            found = bot;
                            break;
                        }
                    }
                    botNames.get(botTypes.lastIndexOf(found)).setSelected(false);
                    chosenBots.remove(0);
                } else {
                    radioButton.setSelected(true);
                }
                Collections.swap(chosenBots, chosenBots.lastIndexOf(mainPlayer), 3);
            });
        }
        fontAndPlace(basePanel, constraints, false, botNames.toArray(new Component[0]), vivaldiHuge);
    }

    public void makeSubmitButton() {
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JButton submit = new JButton("Submit");
        submit.setFocusable(false);

        submit.addActionListener(e -> {
            JsonOperator.saveState();
            try {
                JsonOperator.loadState();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Frame.frame.setContentPane(startMenu);
        });

        fontAndPlace(basePanel, constraints, false, new Component[]{submit}, vivaldiHuge);
    }
}
