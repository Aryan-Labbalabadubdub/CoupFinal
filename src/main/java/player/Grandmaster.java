package player;

import cards.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static database.Constants.*;

public class Grandmaster extends NeutralPlayer {
    @Override
    public void play() throws InterruptedException, IOException, FontFormatException {
        if (getCredit() >= coupPrice) {
            coup(target());
        } else {
            String action = wheelSpin();
            switch (action) {
                case "Ambassador" -> {
                    new Ambassador(this).act();
                    return;
                }
                case "Assassin" -> {
                    new Assassin(this).act();
                    return;
                }
                case "Captain" -> {
                    new Captain(this).act();
                    return;
                }
                case "Condessa" -> {
                    new Condessa(this).act();
                    return;
                }
                case "Duke" -> {
                    new Duke(this).act();
                    return;
                }
                case "Income" -> {
                    income();
                    return;
                }
                case "ForeignAid" -> {
                    foreignAid();
                    return;
                }
                case "Coup" -> {
                    coup(target());
                    return;
                }
                case "Exchange" -> {
                    exchange();
                    return;
                }
            }
        }
        super.play();
    }

    @Override
    public boolean challengeAct(NeutralPlayer target, NeutralCard cardPlayed) {
        return (new Random().nextInt(100) < 20);
    }

    @Override
    public boolean blockAct(NeutralCard card) {
        return (new Random().nextInt(100) < 30);
    }

    private String wheelSpin() {
        String action = "";
        ArrayList<String> actions = new ArrayList<>(actionCost.keySet());

        while (action.equals("")) {
            String tempAction = actions.get(new Random().nextInt(actions.size()));
            if (getCredit() >= actionCost.get(tempAction)) {
                if (cardRisk.containsKey(tempAction)) {

                    if (hasCard(toType(tempAction)) && new Random().nextInt(100) >= cardRisk.get(tempAction)) {
                        action = tempAction;
                    } else if (hasCard(toType(tempAction)) && new Random().nextInt(100) < cardRisk.get(tempAction)) {
                        action = tempAction;
                    }

                } else {
                    action = tempAction;
                }
            }
        }
        return action;
    }

    @Override
    public boolean refundAct() {
        return new Random().nextBoolean();
    }

}
