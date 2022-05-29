package cards;

import database.Constants;
import player.NeutralPlayer;

public class NeutralCard {
    private NeutralPlayer cardOperator;
    private boolean active = true;

    public NeutralCard(NeutralPlayer cardOperator) {
        this.cardOperator = cardOperator;
    }

    public String pathToPic() {
        if (this instanceof Ambassador) {
            return Constants.pathToAmbassadorCard;
        } else if (this instanceof Assassin) {
            return Constants.pathToAssassinCard;
        } else if (this instanceof Captain) {
            return Constants.pathToCaptainCard;
        } else if (this instanceof Condessa) {
            return Constants.pathToCondessaCard;
        } else if (this instanceof Duke) {
            return Constants.pathToDukeCard;
        }
        return null;
    }

    public void act() throws InterruptedException {
        getCardOperator().moves++;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public NeutralPlayer getCardOperator() {
        return cardOperator;
    }

    public void setCardOperator(NeutralPlayer cardOperator) {
        this.cardOperator = cardOperator;
    }
}
