package database;

import board.GameBoard;
import cards.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import player.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static database.Constants.BANK_CREDIT;
import static database.Database.*;

public class JsonOperator {

    public JsonOperator() throws IOException {
        if (!(new File("JSON.json").exists())) {
            writeToJson();
        } else {
            readFromJson();
        }
    }

    public static void saveState() {
        encodePlayers(Database.getInlinePlayers());
        encodeCardHands(Database.getCardHands());
        encodeCoins(Database.getCoins());
        writeToJson();
    }

    public static void loadState() throws IOException {
        boardInitializer();
        readFromJson();
        decodePlayers(InitializationLists.getInstance().encodedPlayers);
        decodeCardsHands(InitializationLists.getInstance().encodedCardHands);
        decodeCoins(InitializationLists.getInstance().encodedCoins);
    }

    public static void writeToJson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String jsonWrite = gson.toJson(InitializationLists.getInstance(), InitializationLists.class);
        try {
            FileWriter writer = new FileWriter("JSON.json");
            writer.write(jsonWrite);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InitializationLists.setInstance(mapper.readValue(new File("JSON.json"), InitializationLists.class));
    }

    public static void decodeCardsHands(ArrayList<Integer> encodedCardHands) {

        for (int i = 0; i < encodedCardHands.size(); i++) {
            Class<?> cardType = null;
            switch (encodedCardHands.get(i) % 5) {
                case 0 -> cardType = Ambassador.class;
                case 1 -> cardType = Assassin.class;
                case 2 -> cardType = Captain.class;
                case 3 -> cardType = Condessa.class;
                case 4 -> cardType = Duke.class;
            }
            assert cardType != null;
            pickCardType(Database.inlinePlayers.get(i / 2), cardType);
        }
        for (NeutralPlayer player : Database.inlinePlayers) {
            Database.cardHands.add(new ArrayList<>(player.getCardHand()));
        }
    }

    public static void decodePlayers(ArrayList<Integer> encodedPlayers) {
        Database.inlinePlayers = new ArrayList<>();
        for (int i : encodedPlayers) {
            if (i == 0) {
                Database.inlinePlayers.add(new Couper());
            } else if (i == 1) {
                Database.inlinePlayers.add(new Grandmaster());
            } else if (i == 2) {
                Database.inlinePlayers.add(new Imposter());
            } else if (i == 3) {
                Database.inlinePlayers.add(new Paranoid());
            }
        }
        mainPlayer = new Player();
        Database.inlinePlayers.add(mainPlayer);
    }

    public static void decodeCoins(ArrayList<Integer> encodedCoins) {
        Database.coins = new ArrayList<>(encodedCoins);
        for (int i = 0; i < Database.inlinePlayers.size(); i++) {
            Database.inlinePlayers.get(i).setCredit(encodedCoins.get(i));
        }
        int sumOfCoins = 0;
        for (NeutralPlayer player : inlinePlayers) {
            sumOfCoins = sumOfCoins + player.getCredit();
        }
        GameBoard.getInstance().setBankCredit(BANK_CREDIT - sumOfCoins);
    }

    public static void encodePlayers(ArrayList<NeutralPlayer> decodedPlayers) {
        InitializationLists.getInstance().encodedPlayers.clear();
        for (NeutralPlayer player : decodedPlayers) {
            if (player instanceof Couper) {
                InitializationLists.getInstance().encodedPlayers.add(0);
            } else if (player instanceof Grandmaster) {
                InitializationLists.getInstance().encodedPlayers.add(1);
            } else if (player instanceof Imposter) {
                InitializationLists.getInstance().encodedPlayers.add(2);
            } else if (player instanceof Paranoid) {
                InitializationLists.getInstance().encodedPlayers.add(3);
            }
        }
    }

    public static void encodeCardHands(ArrayList<ArrayList<NeutralCard>> decodedCardHands) {
        InitializationLists.getInstance().encodedCardHands.clear();
        for (ArrayList<NeutralCard> playerHand : decodedCardHands) {
            for (NeutralCard card : playerHand) {
                int target = -1;
                if (Ambassador.class.equals(card.getClass())) {
                    target = 0;
                } else if (Assassin.class.equals(card.getClass())) {
                    target = 1;
                } else if (Captain.class.equals(card.getClass())) {
                    target = 2;
                } else if (Condessa.class.equals(card.getClass())) {
                    target = 3;
                } else if (Duke.class.equals(card.getClass())) {
                    target = 4;
                }
                while (InitializationLists.getInstance().encodedCardHands.contains(target)) {
                    target = target + 5;
                }
                InitializationLists.getInstance().encodedCardHands.add(target);
            }
        }
    }

    public static void encodeCoins(ArrayList<Integer> decodedCoins) {
        InitializationLists.getInstance().encodedCoins = new ArrayList<>(decodedCoins);
    }
}
