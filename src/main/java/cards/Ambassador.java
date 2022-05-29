package cards;

import board.GameBoard;
import database.Acts;
import gui.choices.EliminationGui;
import player.NeutralPlayer;
import player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static database.Logger.log;

public class Ambassador extends NeutralCard {

    public Ambassador(NeutralPlayer cardOperator) {
        super(cardOperator);
    }

    @Override
    public void act() throws InterruptedException {
        getCardOperator().setMoveCost(0);
        log(Acts.ambassadorAct, List.of(getCardOperator()));

        Collections.shuffle(GameBoard.getInstance().getCardsList());
        ArrayList<NeutralCard> temp = new ArrayList<>();
        ArrayList<NeutralCard> initialActiveHand = new ArrayList<>();

        for (NeutralCard card : getCardOperator().getCardHand()) {
            if (card.isActive()) {
                card.setCardOperator(null);
                temp.add(card);
                initialActiveHand.add(card);
            }
        }

        getCardOperator().getCardHand().removeIf(initialActiveHand::contains);
        temp.add(GameBoard.getInstance().getCardsList().get(0));
        GameBoard.getInstance().getCardsList().remove(0);
        temp.add(GameBoard.getInstance().getCardsList().get(0));
        GameBoard.getInstance().getCardsList().remove(0);

        if (!(getCardOperator() instanceof Player)) {

            Collections.shuffle(temp);
            for (NeutralCard card : temp) {
                if (card instanceof Assassin) {
                    Collections.swap(temp, 0, temp.lastIndexOf(card));
                    break;
                }
            }

            for (int i = 0; i < initialActiveHand.size(); i++) {
                temp.get(0).setCardOperator(getCardOperator());
                getCardOperator().addCardHand(temp.get(0));
                temp.remove(0);
            }
            GameBoard.getInstance().getCardsList().addAll(temp);
        } else {
            ArrayList<NeutralCard> toRemove = new EliminationGui().choice(2, temp, getCardOperator());
            GameBoard.getInstance().getCardsList().addAll(toRemove);
            temp.removeAll(toRemove);
            for (NeutralCard card : temp) {
                card.setCardOperator(getCardOperator());
                getCardOperator().addCardHand(card);
            }
        }
        Collections.shuffle(GameBoard.getInstance().getCardsList());
        super.act();
        getCardOperator().setMoveCost(0);
    }
}
