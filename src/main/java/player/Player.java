package player;

import cards.NeutralCard;
import gui.choices.BlockGui;
import gui.choices.ChallengeGui;
import gui.choices.RefundGui;

public class Player extends NeutralPlayer {
    @Override
    public void play() throws InterruptedException {
        int initial = moves;
        Thread thread = new Thread(() -> {
            while (moves == initial) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        thread.join();
    }

    @Override
    public boolean challengeAct(NeutralPlayer target, NeutralCard cardPlayed) {
        if (isActive()) {
            return new ChallengeGui(target, cardPlayed).output();
        }
        return false;
    }

    @Override
    public boolean blockAct(NeutralCard card) {
        if (isActive()) {
            return new BlockGui(card).output();
        }
        return false;
    }

    @Override
    public boolean refundAct() {
        if (isActive()) {
            return new RefundGui().output();
        }
        return false;
    }
}
