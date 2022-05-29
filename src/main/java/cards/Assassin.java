package cards;

import board.GameBoard;
import database.Acts;
import gui.choices.EliminationGui;
import player.NeutralPlayer;
import player.Player;

import java.util.List;
import java.util.Random;

import static database.Constants.assassinationPrice;
import static database.Logger.log;
import static player.NeutralPlayer.challenge;

public class
Assassin extends NeutralCard {
    public Assassin(NeutralPlayer cardOperator) {
        super(cardOperator);
    }

    @Override
    public void act() throws InterruptedException {
        if (getCardOperator().getCredit() >= assassinationPrice) {

            getCardOperator().setMoveCost(assassinationPrice);
            getCardOperator().setCredit(getCardOperator().getCredit() - assassinationPrice);
            GameBoard.getInstance().setBankCredit(GameBoard.getInstance().getBankCredit() + assassinationPrice);
            NeutralPlayer target = getCardOperator().target();

            log(Acts.assassinAct, List.of(getCardOperator(), target));

            if (!challenge(getCardOperator(), this)) {
                if (!target.block(this)) {

                    if (!(target instanceof Player)) {
                        target.getActiveCards().get(new Random().nextInt(target.getActiveCards().size())).setActive(false);
                    } else {
                        new EliminationGui().choice(1, target.getActiveCards(), target).get(0).setActive(false);
                    }

                    log(Acts.eliminate, List.of(target));
                    target.isActive();

                }
            }
        }
        super.act();
        getCardOperator().setMoveCost(0);
    }
}
