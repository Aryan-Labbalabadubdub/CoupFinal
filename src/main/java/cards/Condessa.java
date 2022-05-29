package cards;

import player.NeutralPlayer;

public class Condessa extends NeutralCard {
    public Condessa(NeutralPlayer cardOperator) {
        super(cardOperator);
    }

    @Override
    public void act() throws InterruptedException {
        super.act();
        getCardOperator().setMoveCost(0);
    }
}
