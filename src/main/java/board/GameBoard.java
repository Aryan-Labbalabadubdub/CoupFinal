package board;

import cards.NeutralCard;
import player.NeutralPlayer;

import java.util.ArrayList;
import java.util.Random;

import static database.Database.getInlinePlayers;

public class GameBoard {

    private static GameBoard instance;
    private final ArrayList<NeutralCard> cardsList = new ArrayList<>();
    private int bankCredit;
    private NeutralPlayer winner;

    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }

    public int getBankCredit() {
        return bankCredit;
    }

    public void setBankCredit(int bankCredit) {
        this.bankCredit = bankCredit;
    }

    public NeutralCard getRandomCard() {
        return cardsList.get(new Random().nextInt(cardsList.size()));
    }

    public ArrayList<NeutralCard> getCardsList() {
        return cardsList;
    }

    public ArrayList<NeutralPlayer> getActivePlayers() {
        ArrayList<NeutralPlayer> out = new ArrayList<>();
        for (NeutralPlayer player : getInlinePlayers()) {
            if (player.isActive()) {
                out.add(player);
            }
        }
        return out;
    }

    public NeutralPlayer getWinner() {
        return winner;
    }

    public void setWinner(NeutralPlayer winner) {
        this.winner = winner;
    }
}
