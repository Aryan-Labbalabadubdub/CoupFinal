package gui.options;

import board.GameBoard;
import cards.*;
import database.Database;
import database.JsonOperator;
import gui.guitools.CoinTextField;
import gui.guitools.Frame;
import gui.guitools.PanelB;
import player.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static database.Constants.*;
import static database.Database.*;
import static gui.Start.*;
import static gui.guitools.ComponentPlace.fontAndPlace;
import static gui.guitools.Convert.dimensionConverter;
import static gui.options.Board.cardGenerator;

public class Settings {
    final Map<JList<String>, JList<String>> lists =
            Map.ofEntries(Map.entry(new JList<>(), new JList<>()), Map.entry(new JList<>(), new JList<>()), Map.entry(new JList<>(), new JList<>()), Map.entry(new JList<>(), new JList<>()));
    PanelB basePanel = new PanelB(new ImageIcon(pathToBackground).getImage(), new float[]{0, 0, 100, 100});
    ArrayList<NeutralPlayer> botTypes = new ArrayList<>(List.of(new Imposter(), new Paranoid(), new Grandmaster(), new Couper()));
    ArrayList<CoinTextField> coinEntryFields = new ArrayList<>(List.of(
            new CoinTextField(botTypes.get(0).getClass().getSimpleName(), 1, botTypes.get(0).getCredit())
            , new CoinTextField(botTypes.get(1).getClass().getSimpleName(), 1, botTypes.get(1).getCredit())
            , new CoinTextField(botTypes.get(2).getClass().getSimpleName(), 1, botTypes.get(2).getCredit())
            , new CoinTextField(botTypes.get(3).getClass().getSimpleName(), 1, botTypes.get(3).getCredit())));
    DefaultListModel<String> boardCardsModel = new DefaultListModel<>();
    GridBagConstraints constraints = new GridBagConstraints();

    {
        for (JList<String> list : lists.keySet()) {
            ((DefaultListCellRenderer) list.getCellRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        }
        for (JList<String> list : lists.values()) {
            ((DefaultListCellRenderer) list.getCellRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    {
        ArrayList<NeutralPlayer> temp = new ArrayList<>(botTypes);
        TypeCheck:
        for (NeutralPlayer player : botTypes) {
            for (NeutralPlayer player1 : getInlinePlayers()) {
                if (player.getClass().equals(player1.getClass())) {
                    temp.set(temp.lastIndexOf(player), player1);
                    continue TypeCheck;
                }
            }
            temp.remove(player);
        }
        temp.add(mainPlayer);
        botTypes = temp;
    }

    public Settings() {
        basePanel.setSize(Frame.frame.getSize());
        basePanel.fadeBackground(OPACITY);

        basePanel.setLayout(new GridBagLayout());
        constraints.insets = new Insets(5, 5, 5, 5);

        makeTitle();
        makeBotImages();
        makeCardHandList();
        makeCoinEntryBox();
        makeSubmit();
        Frame.frame.setContentPane(basePanel);
    }

    public static NeutralCard typeToCard(String cardType) {
        if ("Ambassador".equals(cardType)) {
            return new Ambassador(null);
        } else if ("Assassin".equals(cardType)) {
            return new Assassin(null);
        } else if ("Captain".equals(cardType)) {
            return new Captain(null);
        } else if ("Condessa".equals(cardType)) {
            return new Condessa(null);
        } else if ("Duke".equals(cardType)) {
            return new Duke(null);
        } else {
            return null;
        }
    }

    public void makeTitle() {
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = botTypes.size();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JLabel label = new JLabel("Choose your game settings");
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
        constraints.weightx = 1;
        List<JButton> botImages = new ArrayList<>(botTypes.size());
        for (NeutralPlayer type : botTypes) {
            JButton botImage = cardGenerator(type.pathToPic(), 2f);
            for (ActionListener actionListener : botImage.getActionListeners()) {
                botImage.removeActionListener(actionListener);
            }
            botImage.addActionListener(e -> {
                for (JButton button : botImages) {
                    button.setBorder(null);
                }
                if (!getKickStarter().getClass().equals(botTypes.get(botImages.lastIndexOf(botImage)).getClass())) {
                    for (JButton button : botImages) {
                        if (getKickStarter().getClass().equals(botTypes.get(botImages.lastIndexOf(botImage)).getClass())) {
                            button.setBorder(null);
                        }
                        break;
                    }
                    botImage.setBorder(BorderFactory.createLineBorder(bloodRed, 5));
                    for (NeutralPlayer player : getInlinePlayers()) {
                        if (player.getClass().equals(botTypes.get(botImages.lastIndexOf(botImage)).getClass())) {
                            setKickStarter(player);
                            break;
                        }
                    }
                } else {
                    setKickStarter(mainPlayer);
                }
            });
            botImage.setFocusable(false);
            botImages.add(botImage);
        }
        fontAndPlace(basePanel, constraints, false, botImages.toArray(new Component[0]), vivaldiHuge);
    }

    public void makeCardHandList() {
        for (JList<String> selectedList : lists.keySet()) {
            selectedList.setFocusable(false);
            selectedList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() >= 2) {
                        NeutralPlayer targetPlayer = botTypes.get(new ArrayList<>(lists.keySet()).lastIndexOf(selectedList));
                        putCardType(targetPlayer, targetPlayer.getCardHand().get(selectedList.getSelectedIndex()).getClass());
                        pickCardType(targetPlayer, GameBoard.getInstance().getCardsList().get(new Random().nextInt(GameBoard.getInstance().getCardsList().size())).getClass());
                        fillCardHandList();
                    } else if (e.getClickCount() == 1) {
                        selectedList.clearSelection();
                    }
                }
            });
        }
        for (JList<String> unselectedList : lists.values()) {
            unselectedList.setFocusable(false);
            JList<String> key = null;
            for (JList<String> selectedList : lists.keySet()) {
                if (lists.get(selectedList).equals(unselectedList)) {
                    key = selectedList;
                    break;
                }
            }
            JList<String> finalKey = key;
            unselectedList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() >= 2) {
                        NeutralPlayer targetPlayer = botTypes.get(new ArrayList<>(lists.keySet()).lastIndexOf(finalKey));
                        pickCardType(targetPlayer, GameBoard.getInstance().getCardsList().get(unselectedList.getSelectedIndex()).getClass());
                        putCardType(targetPlayer, targetPlayer.getCardHand().get(0).getClass());
                        fillCardHandList();
                    } else if (e.getClickCount() == 1) {
                        unselectedList.clearSelection();
                    }
                }
            });
        }
        fillCardHandList();

        ArrayList<JTabbedPane> tabbedPanes = new ArrayList<>(List.of(new JTabbedPane(), new JTabbedPane(), new JTabbedPane(), new JTabbedPane()));
        for (JTabbedPane tabbedPane : tabbedPanes) {
            JList<String> chosenList = new ArrayList<>(lists.keySet()).get(tabbedPanes.lastIndexOf(tabbedPane));
            JScrollPane chosen = new JScrollPane(chosenList);
            chosen.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            JScrollPane unchosen = new JScrollPane(lists.get(chosenList));
            unchosen.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            tabbedPane.addTab("Chosen", chosen);
            tabbedPane.addTab("Unchosen", unchosen);
        }
        constraints.gridx = 0;
        constraints.gridy++;
        fontAndPlace(basePanel, constraints, false, tabbedPanes.toArray(new Component[0]), vivaldiResized, dimensionConverter(20, 25));
    }

    public void fillCardHandList() {
        for (JList<String> selectedList : lists.keySet()) {
            DefaultListModel<String> selectedListModel = new DefaultListModel<>();
            for (NeutralCard card : botTypes.get(new ArrayList<>(lists.keySet()).lastIndexOf(selectedList)).getCardHand()) {
                selectedListModel.addElement(card.getClass().getSimpleName());
            }
            selectedList.setModel(selectedListModel);
        }

        boardCardsModel = new DefaultListModel<>();
        for (NeutralCard card : GameBoard.getInstance().getCardsList()) {
            boardCardsModel.addElement(card.getClass().getSimpleName());
        }
        for (JList<String> unselectedList : lists.values()) {
            unselectedList.setModel(boardCardsModel);
        }
        basePanel.revalidate();
        basePanel.repaint();
    }

    public void makeCoinEntryBox() {
        constraints.gridx = 0;
        constraints.gridy++;
        ArrayList<PanelB> titledFields = new ArrayList<>();
        for (CoinTextField coinTextField : coinEntryFields) {
            titledFields.add(coinTextField.packPanel());
        }
        fontAndPlace(basePanel, constraints, false, titledFields.toArray(new Component[0]), vivaldiResized, dimensionConverter(20, 5));
    }

    public void makeSubmit() {
        JButton submit = new JButton("Submit settings");
        submit.addActionListener(e -> {
            ArrayList<ArrayList<NeutralCard>> cardHands = new ArrayList<>();
            ArrayList<Integer> coins = new ArrayList<>();
            for (NeutralPlayer player : botTypes) {

                ArrayList<NeutralCard> currentCardHand = new ArrayList<>();
                JList<String> currentCardHandList = new ArrayList<>(lists.keySet()).get(botTypes.lastIndexOf(player));
                for (Object chosenCard : ((DefaultListModel<String>) currentCardHandList.getModel()).toArray()) {
                    NeutralCard currentChosenCard = typeToCard(String.valueOf(chosenCard));
                    assert currentChosenCard != null;
                    currentChosenCard.setCardOperator(player);
                    currentCardHand.add(currentChosenCard);
                }
                cardHands.add(currentCardHand);

                String currentValue = coinEntryFields.get(botTypes.lastIndexOf(player)).getText();
                if (currentValue.equals("")) {
                    currentValue = "0";
                }
                coins.add(Integer.parseInt(currentValue));
            }

            Database.setInlinePlayers(botTypes);
            Database.setCardHands(cardHands);
            Database.setCoins(coins);

            JsonOperator.saveState();
            try {
                JsonOperator.loadState();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Frame.frame.setContentPane(startMenu);
        });

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = botTypes.size();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        fontAndPlace(basePanel, constraints, false, new Component[]{submit}, vivaldiResized, dimensionConverter(95, 6));
    }
}
