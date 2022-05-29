package player;

import cards.Ambassador;
import cards.Assassin;
import cards.NeutralCard;

import java.awt.*;
import java.io.IOException;

import static database.Constants.assassinationPrice;
import static database.Constants.coupPrice;

public class Paranoid extends NeutralPlayer {
    static int paranoidTurn = 0;

    @Override
    public void play() throws InterruptedException, IOException, FontFormatException {
        if (getCredit() >= coupPrice) {
            coup(target());
        } else {
            switch (paranoidTurn % 3) {
                case 0 -> {
                    foreignAid();
                    return;
                }
                case 1 -> {
                    if (paranoidTurn % 2 == 1 && getCredit() >= assassinationPrice) {
                        new Assassin(this).act();
                    } else if (paranoidTurn % 2 == 0) {
                        new Ambassador(this).act();
                    } else {
                        income();
                    }
                    return;
                }
                case 2 -> {
                    if (getCredit() >= 1) {
                        exchange();
                    } else {
                        income();
                    }
                    return;
                }
            }
            paranoidTurn++;
            return;
        }
        super.play();
    }

    @Override
    public boolean challengeAct(NeutralPlayer target, NeutralCard cardPlayed) {
        return Challenge.cnt % 2 == 0;
    }

    @Override
    public boolean blockAct(NeutralCard card) {
        return super.blockAct(card);
    }

    @Override
    public boolean refundAct() {
        return super.refundAct();
    }
}
