package player;

import cards.Duke;
import cards.NeutralCard;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

import static database.Constants.coupPrice;

public class Couper extends NeutralPlayer {
    @Override
    public boolean challengeAct(NeutralPlayer target, NeutralCard cardPlayed) {
        return super.challengeAct(target, cardPlayed);
    }

    @Override
    public boolean blockAct(NeutralCard card) {
        return (new Random().nextInt(100) > 75);
    }

    @Override
    public void play() throws InterruptedException, IOException, FontFormatException {
        if (getCredit() < coupPrice) {
            new Duke(this).act();
        } else {
            coup(target());
        }
        super.play();
    }

    @Override
    public boolean refundAct() {
        return true;
    }
}
