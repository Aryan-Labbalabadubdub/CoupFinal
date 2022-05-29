package gui.options;

import board.GameBoard;
import cards.Ambassador;
import cards.Assassin;
import cards.Captain;
import cards.Duke;
import database.Acts;
import database.Constants;
import database.Logger;
import gui.guitools.Frame;
import gui.guitools.JDialogLocked;
import gui.guitools.PanelB;
import player.NeutralPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static database.Constants.*;
import static database.Database.*;
import static gui.guitools.BorderSetup.totalBorder;
import static gui.guitools.ComponentPlace.fontAndPlace;
import static gui.guitools.Convert.*;

public class Board {
    private static final ExecutorService pool = Executors.newFixedThreadPool(25);
    private final Map<Acts, JButton> actions = Map.ofEntries(
            Map.entry(Acts.income, cardGenerator(pathToIncomeCard, actionMultiplier)), Map.entry(Acts.foreignAid, cardGenerator(pathToForeignAidCard, actionMultiplier)),
            Map.entry(Acts.coup, cardGenerator(pathToCoupCard, actionMultiplier)), Map.entry(Acts.exchange, cardGenerator(pathToExchangeCard, actionMultiplier)),
            Map.entry(Acts.ambassadorAct, cardGenerator(pathToAmbassadorCard, actionMultiplier)), Map.entry(Acts.assassinAct, cardGenerator(pathToAssassinCard, actionMultiplier)),
            Map.entry(Acts.captainAct, cardGenerator(pathToCaptainCard, actionMultiplier)), Map.entry(Acts.dukeAct, cardGenerator(pathToDukeCard, actionMultiplier))
    );
    PanelB mainPanel = new PanelB(new ImageIcon(pathToBackground).getImage(), new float[]{0, 0, 100, 100});
    private NeutralPlayer player;
    private PanelB boardState;
    private PanelB player1State;
    private PanelB player2State;
    private PanelB player3State;
    private PanelB player4State;

    public static JButton cardGenerator(String path, float multiplier) {
        JButton card = new JButton();
        card.setPreferredSize(dimensionConverter(cardW * multiplier, cardH * multiplier));
        card.setFocusable(false);

        Runnable resize = () -> card.setIcon(new ImageIcon(iconResize(path, new float[]{cardW * multiplier, cardH * multiplier}, false)));
        pool.execute(resize);

        card.setContentAreaFilled(false);
        card.setOpaque(false);
        card.setBorder(null);
        card.addActionListener(e -> {
            JLabel cardImage = new JLabel();

            Runnable resize1 = () -> cardImage.setIcon(new ImageIcon(iconResize(path, new float[]{(float) (cardMaxW * 0.96), (float) (cardMaxH * 0.96)}, false)));
            pool.execute(resize1);

            JDialogLocked cardDialog = new JDialogLocked();
            cardDialog.setSize(dimensionConverter(cardMaxW, cardMaxH));
            cardDialog.setResizable(false);
            cardDialog.setVisible(true);
            cardDialog.setLocationRelativeTo(null);

            cardDialog.add(cardImage);
            cardDialog.lock();
        });
        return card;
    }

    public void generate() throws IOException, FontFormatException {
        mainPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    JOptionPane.showMessageDialog(null, Logger.getTextScrollable());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        mainPanel.setFocusable(true);
        mainPanel.requestFocusInWindow();

        mainPanel.setSize(Frame.frame.getSize());
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.fadeBackground(75);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(8, 8, 8, 8);

        fontAndPlace(mainPanel, constraints, true, new Component[]{statePanel(), actionPanel(mainPlayer)}, Constants.vivaldiResized);
        Frame.frame.setContentPane(mainPanel);
    }

    private PanelB statePanel() throws IOException, FontFormatException {
        PanelB states = new PanelB(null, new float[]{0, 0, 100, 100});
        states.setSize(dimensionConverter(98.5f, 75));
        states.setBorder(totalBorder("Game State", new int[]{5, 5, 5, 5}, vivaldiResized));
        states.setOpaque(false);

        states.setLayout(new GridBagLayout());
        GridBagConstraints stateConstraints = new GridBagConstraints();
        stateConstraints.gridx = 0;
        stateConstraints.gridy = 0;

        boardState = new PanelB(null, new float[]{0, 0, 100, 100});
        boardState.setBorder(totalBorder("Board State", new int[]{5, 5, 5, 5}, vivaldiResized));

        player1State = new PanelB(null, new float[]{0, 0, 100, 100});
        player1State.setBorder(totalBorder(inlinePlayers.get(0).getClass().getSimpleName() + " State", new int[]{5, 5, 5, 5}, vivaldiResized));
        player2State = new PanelB(null, new float[]{0, 0, 100, 100});
        player2State.setBorder(totalBorder(inlinePlayers.get(1).getClass().getSimpleName() + " State", new int[]{5, 5, 5, 5}, vivaldiResized));
        player3State = new PanelB(null, new float[]{0, 0, 100, 100});
        player3State.setBorder(totalBorder(inlinePlayers.get(2).getClass().getSimpleName() + " State", new int[]{5, 5, 5, 5}, vivaldiResized));
        player4State = new PanelB(null, new float[]{0, 0, 100, 100});
        player4State.setBorder(totalBorder(inlinePlayers.get(3).getClass().getSimpleName() + " State", new int[]{5, 5, 5, 5}, vivaldiResized));

        fillSubPanel(boardState, GameBoard.getInstance());
        fillSubPanel(player1State, inlinePlayers.get(0));
        fillSubPanel(player2State, inlinePlayers.get(1));
        fillSubPanel(player3State, inlinePlayers.get(2));
        fillSubPanel(player4State, inlinePlayers.get(3));

        Component[] components = new Component[]{boardState, player1State, player2State, player3State, player4State};
        fontAndPlace(states, stateConstraints, false, components, Constants.vivaldiResized, dimensionConverter(18.5f, 70));
        return states;
    }

    public void deactivateActionPanel() {
        for (JButton button : actions.values()) {
            for (ActionListener actionListener : button.getActionListeners()) {
                button.removeActionListener(actionListener);
            }
        }
    }

    private PanelB actionPanel(NeutralPlayer player) {
        this.player = player;

        PanelB action = new PanelB(null, new float[]{0, 0, 100, 100});
        action.setSize(dimensionConverter(98.5f, 22.5f));
        action.setBorder(totalBorder("Actions", new int[]{5, 5, 5, 5}, vivaldiResized));
        action.setOpaque(false);

        action.setLayout(new GridBagLayout());
        GridBagConstraints actionConstraints = new GridBagConstraints();
        actionConstraints.gridx = 0;
        actionConstraints.gridy = 0;
        actionConstraints.insets = new Insets(5, 5, 5, 5);

        for (JButton button : actions.values()) {
            for (ActionListener actionListener : button.getActionListeners()) {
                button.removeActionListener(actionListener);
            }
        }

        setActions();
        setButtonState();

        fontAndPlace(action, actionConstraints, false, actions.values().toArray(new JButton[0]), Constants.vivaldiResized);
        return action;
    }

    private void setActions() {
        actions.get(Acts.income).addActionListener(e -> player.income());
        actions.get(Acts.foreignAid).addActionListener(e -> player.foreignAid());
        actions.get(Acts.exchange).addActionListener(e -> player.exchange());
        actions.get(Acts.ambassadorAct).addActionListener(e -> {
            try {
                new Ambassador(player).act();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        actions.get(Acts.assassinAct).addActionListener(e -> {
            try {
                new Assassin(player).act();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        actions.get(Acts.captainAct).addActionListener(e -> {
            try {
                new Captain(player).act();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        actions.get(Acts.dukeAct).addActionListener(e -> {
            try {
                new Duke(player).act();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        actions.get(Acts.coup).addActionListener(e -> {
            try {
                player.coup(player.target());
            } catch (IOException | FontFormatException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void setButtonState() {
        for (JButton button : actions.values()) {
            button.setEnabled(true);
            button.setBorder(null);
        }
        if (!player.isActive()) {
            for (JButton button : actions.values()) {
                button.setEnabled(false);
            }
        } else if (player.getCredit() >= coupPrice) {
            for (JButton button : actions.values()) {
                button.setEnabled(false);
            }
            actions.get(Acts.coup).setEnabled(true);
        } else {
            if (player.getCredit() < coupPrice) {
                actions.get(Acts.coup).setEnabled(false);
            }
            if (player.getCredit() < exchangePrice) {
                actions.get(Acts.exchange).setEnabled(false);
            }
            if (player.getCredit() < assassinationPrice) {
                actions.get(Acts.assassinAct).setEnabled(false);
            }
            if (!player.hasCard(Ambassador.class)) {
                actions.get(Acts.ambassadorAct).setBorder(BorderFactory.createLineBorder(bloodRed, 3));
            }
            if (!player.hasCard(Duke.class)) {
                actions.get(Acts.dukeAct).setBorder(BorderFactory.createLineBorder(bloodRed, 3));
            }
            if (!player.hasCard(Captain.class)) {
                actions.get(Acts.captainAct).setBorder(BorderFactory.createLineBorder(bloodRed, 3));
            }
            if (!player.hasCard(Assassin.class)) {
                actions.get(Acts.assassinAct).setBorder(BorderFactory.createLineBorder(bloodRed, 3));
            }
        }
    }

    private void fillSubPanel(PanelB statePanel, Object type) throws IOException, FontFormatException {
        statePanel.setLayout(new GridBagLayout());
        GridBagConstraints subConstraints = new GridBagConstraints();
        subConstraints.gridx = 0;
        subConstraints.gridy = 0;
        subConstraints.insets = new Insets(4, 4, 4, 4);

        if (type instanceof GameBoard) {
            JButton info = cardGenerator(pathToInfoCard, stateMultiplier);
            JButton coin = coinCardGenerator(GameBoard.getInstance().getBankCredit());
            JButton cards = cardGenerator(pathToBackCard, stateMultiplier);

            fontAndPlace(statePanel, subConstraints, true, new Component[]{coin, cards, info}, Constants.vivaldiResized);
        } else if (type instanceof NeutralPlayer) {
            JButton coin = coinCardGenerator(((NeutralPlayer) type).getCredit());
            JButton card1;
            JButton card2;

            if (type.equals(mainPlayer)) {
                statePanel.setBorder(null);
                statePanel.setBorder(totalBorder(mainPlayer.getClass().getSimpleName() + " State", new int[]{5, 5, 5, 5}, vivaldiResized, bloodRed));

                card1 = cardGenerator(((NeutralPlayer) type).getCardHand().get(0).pathToPic(), stateMultiplier);
                card2 = cardGenerator(((NeutralPlayer) type).getCardHand().get(1).pathToPic(), stateMultiplier);
                if (!(((NeutralPlayer) type).getCardHand().get(0).isActive())) {
                    card1.setEnabled(false);
                }
                if (!(((NeutralPlayer) type).getCardHand().get(1).isActive())) {
                    card2.setEnabled(false);
                }
            } else {
                if (!(((NeutralPlayer) type).getCardHand().get(0).isActive())) {
                    card1 = cardGenerator(((NeutralPlayer) type).getCardHand().get(0).pathToPic(), stateMultiplier);
                } else {
                    card1 = cardGenerator(pathToBackCard, stateMultiplier);
                }
                if (!(((NeutralPlayer) type).getCardHand().get(1).isActive())) {
                    card2 = cardGenerator(((NeutralPlayer) type).getCardHand().get(1).pathToPic(), stateMultiplier);
                } else {
                    card2 = cardGenerator(pathToBackCard, stateMultiplier);
                }
            }
            fontAndPlace(statePanel, subConstraints, true, new Component[]{coin, card1, card2}, Constants.vivaldiResized);
        }
    }

    public void refresh() throws IOException, FontFormatException {
        for (KeyListener keyListener : mainPanel.getKeyListeners()) {
            mainPanel.removeKeyListener(keyListener);
        }

        mainPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    JOptionPane.showMessageDialog(null, Logger.getTextScrollable());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        mainPanel.setFocusable(true);
        mainPanel.requestFocusInWindow();

        boardState.removeAll();
        player1State.removeAll();
        player2State.removeAll();
        player3State.removeAll();
        player4State.removeAll();

        fillSubPanel(boardState, GameBoard.getInstance());
        fillSubPanel(player1State, inlinePlayers.get(0));
        fillSubPanel(player2State, inlinePlayers.get(1));
        fillSubPanel(player3State, inlinePlayers.get(2));
        fillSubPanel(player4State, inlinePlayers.get(3));

        setButtonState();


        Frame.frame.revalidate();
        Frame.frame.repaint();
    }

    private JButton coinCardGenerator(int value) throws IOException, FontFormatException {
        JButton coinButton = cardGenerator(pathToCoinCard, stateMultiplier);
        String text = value + "\n Coins";
        BufferedImage coin = (BufferedImage) pathToImage(pathToCoinCard);
        coin = (BufferedImage) coinImageGenerator(coin, text);

        BufferedImage finalCoin1 = coin;
        Runnable resize = () -> coinButton.setIcon(new ImageIcon(iconResize(new ImageIcon(finalCoin1).getImage(), new float[]{cardW * stateMultiplier, cardH * stateMultiplier}, false)));
        pool.execute(resize);

        for (ActionListener actionListener : coinButton.getActionListeners()) {
            coinButton.removeActionListener(actionListener);
        }

        BufferedImage finalCoin = coin;
        coinButton.addActionListener(e -> {
            JLabel cardImage = new JLabel();

            Runnable resize1 = () -> cardImage.setIcon(new ImageIcon(iconResize(finalCoin, new float[]{(float) (0.96 * cardMaxW), (float) (0.96 * cardMaxH)}, false)));
            pool.execute(resize1);

            JDialogLocked cardDialog = new JDialogLocked();
            cardDialog.setSize(dimensionConverter(cardMaxW, cardMaxH));
            cardDialog.setResizable(false);
            cardDialog.setVisible(true);
            cardDialog.setLocationRelativeTo(null);

            cardDialog.add(cardImage);
            cardDialog.lock();
        });
        return coinButton;
    }

    private Image coinImageGenerator(Image image, String text) throws IOException, FontFormatException {
        Image coin = new ImageIcon(fadeImage(image, 70)).getImage();

        Font coinFont = vivaldi.deriveFont(Font.PLAIN, 400);

        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        int width = (int) (coinFont.getStringBounds(text, frc).getWidth());

        Graphics g = coin.getGraphics();
        g.setFont(coinFont);
        g.setColor(Color.BLACK);
        g.drawString(text, (image.getWidth(null) - width) / 2, (int) (0.4 * image.getHeight(null)));
        g.dispose();

        return coin;
    }
}
