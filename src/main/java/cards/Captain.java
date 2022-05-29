package cards;

import database.Acts;
import player.NeutralPlayer;

import java.util.List;

import static database.Constants.stealValue;
import static database.Logger.log;
import static player.NeutralPlayer.challenge;

public class Captain extends NeutralCard {
    public Captain(NeutralPlayer cardOperator) {
        super(cardOperator);
    }

    @Override
    public void act() throws InterruptedException {
        getCardOperator().setMoveCost(0);

        NeutralPlayer target = getCardOperator().target();
        log(Acts.captainAct, List.of(getCardOperator(), target));

        if (!challenge(getCardOperator(), this)) {
            if (!target.block(this)) {
                int steal = Math.min(stealValue, target.getCredit());
                getCardOperator().setCredit(getCardOperator().getCredit() + steal);
                target.setCredit(target.getCredit() - steal);
            }
        }
        super.act();
        getCardOperator().setMoveCost(0);
    }
}
