package cards;

import board.GameBoard;
import database.Acts;
import player.NeutralPlayer;

import java.util.List;

import static database.Constants.taxValue;
import static database.Logger.log;
import static player.NeutralPlayer.challenge;

public class Duke extends NeutralCard {
    public Duke(NeutralPlayer cardOperator) {
        super(cardOperator);
    }

    @Override
    public void act() throws InterruptedException {
        getCardOperator().setMoveCost(0);
        log(Acts.dukeAct, List.of(getCardOperator()));

        if (!challenge(getCardOperator(), this)) {
            int tax = Math.min(taxValue, GameBoard.getInstance().getBankCredit());
            getCardOperator().setCredit(getCardOperator().getCredit() + tax);
            GameBoard.getInstance().setBankCredit(GameBoard.getInstance().getBankCredit() - tax);
        }
        super.act();
        getCardOperator().setMoveCost(0);
    }
}
