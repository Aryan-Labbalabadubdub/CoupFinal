package database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static database.Constants.cardCopies;

public class InitializationLists {
    private static InitializationLists instance;
    public ArrayList<Integer> encodedPlayers = new ArrayList<>();
    public ArrayList<Integer> encodedCardHands = new ArrayList<>();
    public ArrayList<Integer> encodedCoins = new ArrayList<>();

    public InitializationLists() {
        instance = this;
    }

    public static InitializationLists getInstance() {
        if (instance == null) {
            instance = new InitializationLists();
            instance.randomize();
        }
        return instance;
    }

    public static void setInstance(InitializationLists instance) {
        InitializationLists.instance = instance;
    }

    public void randomize() {
        ArrayList<Integer> tempPlayer = new ArrayList<>(List.of(0, 1, 2, 3));
        Collections.shuffle(tempPlayer);
        for (int i = 0; i < 3; i++) {
            instance.encodedPlayers.add(tempPlayer.get(0));
            tempPlayer.remove(0);
        }

        ArrayList<Integer> cardChoices = new ArrayList<>();
        for (int i = 0; i < 5 * cardCopies; i++) {
            cardChoices.add(i);
        }

        ArrayList<Integer> tempCard = new ArrayList<>(cardChoices);
        Collections.shuffle(tempCard);
        for (int i = 0; i < 8; i++) {
            instance.encodedCardHands.add(tempCard.get(0));
            tempCard.remove(0);
        }
        instance.encodedCoins = new ArrayList<>(List.of(2, 2, 2, 2));
    }

}
