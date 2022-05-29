package gui;

import board.GameBoard;
import database.Constants;
import database.JsonOperator;
import gui.guitools.PanelB;
import gui.options.Board;
import gui.options.OpponentBots;
import gui.options.Settings;
import gui.ost.SoundPlayer;
import player.NeutralPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

import static database.Constants.ostPath;
import static database.Constants.vivaldiHuge;
import static database.Database.inlinePlayers;
import static database.Database.mainPlayer;
import static gui.guitools.ComponentPlace.fontAndPlace;
import static gui.guitools.Frame.frame;
import static gui.guitools.Frame.makeFrame;

public class Start {
    public static Board currentBoard;
    public static Thread turnArrangement;
    public static PanelB startMenu;
    public static NeutralPlayer kickStarter;

    public Start() throws IOException {
        new Thread(() -> {
            while (true) {
                SoundPlayer.playeSound(ostPath + "/Percival_Schuttenbach_The_Musty_Scent_Of_Fresh_Pâté.wav");
            }
        }).start();

        JsonOperator.loadState();
        kickStarter = inlinePlayers.get(new Random().nextInt(inlinePlayers.size()));

        turnArrangement = new Thread(() -> {
            int turn = -1;
            for (NeutralPlayer player : inlinePlayers) {
                if (player.getClass().equals(kickStarter.getClass())) {
                    turn = inlinePlayers.lastIndexOf(player);
                    break;
                }
            }

            while (GameBoard.getInstance().getActivePlayers().size() > 1) {
                NeutralPlayer currentPlayer = inlinePlayers.get(turn % 4);
                if (currentPlayer.equals(mainPlayer) && currentBoard != null) {
                    try {
                        currentBoard.refresh();
                    } catch (IOException | FontFormatException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (GameBoard.getInstance().getActivePlayers().contains(currentPlayer)) {
                    try {
                        currentPlayer.play();
                    } catch (InterruptedException | IOException | FontFormatException e) {
                        throw new RuntimeException(e);
                    }
                }
                turn++;
            }
            try {
                currentBoard.refresh();
            } catch (IOException | FontFormatException e) {
                throw new RuntimeException(e);
            }
            currentBoard.deactivateActionPanel();
            JOptionPane.showMessageDialog(frame, GameBoard.getInstance().getActivePlayers().get(0).getClass().getSimpleName() + " wins!");
        });
        turnArrangement.setDaemon(true);

        SwingUtilities.invokeLater(() -> {
            try {
                makeFrame();
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            setupStartMenu();
        });

    }

    public static void setupStartMenu() {
        startMenu = new PanelB(new ImageIcon(Constants.pathToBackground).getImage(), new float[]{0, 0, 100, 100});
        startMenu.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;

        JButton opponents = new JButton("Choose Opponent Bots");
        opponents.addActionListener(e -> new OpponentBots());

        JButton settings = new JButton("Settings");
        settings.addActionListener(e -> new Settings());

        JButton start = new JButton("Start");
        start.addActionListener(e -> {
            try {
                currentBoard = new Board();
                currentBoard.generate();
                turnArrangement.start();
            } catch (IOException | FontFormatException ex) {
                throw new RuntimeException(ex);
            }
        });

        for (JButton button : new JButton[]{opponents, settings, start}) {
            button.setFocusable(false);
        }

        fontAndPlace(startMenu, constraints, true, new Component[]{opponents, settings, start}, vivaldiHuge);
        frame.setContentPane(startMenu);
    }

    public static NeutralPlayer getKickStarter() {
        return kickStarter;
    }

    public static void setKickStarter(NeutralPlayer player) {
        kickStarter = player;
    }
}
