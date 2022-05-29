package database;

import board.GameBoard;
import cards.*;
import player.NeutralPlayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static database.Constants.BANK_CREDIT;
import static database.Constants.cardCopies;

public class Database {
    public static ArrayList<NeutralPlayer> inlinePlayers = new ArrayList<>();
    public static ArrayList<ArrayList<NeutralCard>> cardHands = new ArrayList<>();
    public static ArrayList<Integer> coins = new ArrayList<>();
    public static NeutralPlayer mainPlayer;

    public static void boardInitializer() {
        GameBoard.getInstance().getCardsList().clear();
        GameBoard.getInstance().setBankCredit(BANK_CREDIT);

        for (int i = 0; i < cardCopies; i++) {
            GameBoard.getInstance().getCardsList().add(new Ambassador(null));
            GameBoard.getInstance().getCardsList().add(new Duke(null));
            GameBoard.getInstance().getCardsList().add(new Assassin(null));
            GameBoard.getInstance().getCardsList().add(new Condessa(null));
            GameBoard.getInstance().getCardsList().add(new Captain(null));
        }
        inlinePlayers.clear();
        cardHands.clear();
    }

    public static Image pathToImage(String path) {
        BufferedImage out = null;
        try {
            out = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static void pickCardType(NeutralPlayer player, Class<?> cardType) {
        if (cardType.equals(Ambassador.class)) {
            player.addCardHand(new Ambassador(player));
        } else if (cardType.equals(Duke.class)) {
            player.addCardHand(new Duke(player));
        } else if (cardType.equals(Assassin.class)) {
            player.addCardHand(new Assassin(player));
        } else if (cardType.equals(Condessa.class)) {
            player.addCardHand(new Condessa(player));
        } else if (cardType.equals(Captain.class)) {
            player.addCardHand(new Captain(player));
        }
        for (NeutralCard card : GameBoard.getInstance().getCardsList()) {
            if (card.getClass().equals(cardType)) {
                GameBoard.getInstance().getCardsList().remove(card);
                break;
            }
        }
    }

    public static void putCardType(NeutralPlayer player, Class<?> cardType) {
        if (cardType.equals(Ambassador.class)) {
            GameBoard.getInstance().getCardsList().add(new Ambassador(null));
        } else if (cardType.equals(Duke.class)) {
            GameBoard.getInstance().getCardsList().add(new Duke(null));
        } else if (cardType.equals(Assassin.class)) {
            GameBoard.getInstance().getCardsList().add(new Assassin(null));
        } else if (cardType.equals(Condessa.class)) {
            GameBoard.getInstance().getCardsList().add(new Condessa(null));
        } else if (cardType.equals(Captain.class)) {
            GameBoard.getInstance().getCardsList().add(new Captain(null));
        }
        for (NeutralCard card : player.getCardHand()) {
            if (card.getClass().equals(cardType)) {
                player.getCardHand().remove(card);
                break;
            }
        }
    }

    public static ArrayList<NeutralPlayer> getInlinePlayers() {
        return inlinePlayers;
    }

    public static void setInlinePlayers(ArrayList<NeutralPlayer> inlinePlayers) {
        Database.inlinePlayers = inlinePlayers;
    }

    public static ArrayList<ArrayList<NeutralCard>> getCardHands() {
        return cardHands;
    }

    public static void setCardHands(ArrayList<ArrayList<NeutralCard>> cardHands) {
        Database.cardHands = cardHands;
    }

    public static ArrayList<Integer> getCoins() {
        return coins;
    }

    public static void setCoins(ArrayList<Integer> coins) {
        Database.coins = coins;
    }
}
