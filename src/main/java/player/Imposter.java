package player;

import cards.Ambassador;
import cards.Assassin;
import cards.NeutralCard;

import java.awt.*;
import java.io.IOException;

import static database.Constants.coupPrice;

public class Imposter extends NeutralPlayer {
    @Override
    public void play() throws InterruptedException, IOException, FontFormatException {
        if (getCredit() >= coupPrice) {
            coup(target());
        } else {
            if (hasCard(Assassin.class)) {
                new Assassin(this).act();
            } else {
                if (hasCard(Ambassador.class)) {
                    new Ambassador(this).act();
                } else {
                    if (getCredit() >= 1) {
                        exchange();
                    } else {
                        foreignAid();
                    }
                }
            }
        }
        super.play();
    }

    @Override
    public boolean challengeAct(NeutralPlayer target, NeutralCard cardPlayed) {
        return super.challengeAct(target, cardPlayed);
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
