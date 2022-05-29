package player;

import board.GameBoard;
import cards.*;
import database.Acts;
import gui.choices.EliminationGui;
import gui.choices.TargetGui;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static database.Constants.*;
import static database.Logger.log;

public class NeutralPlayer {
    private final ArrayList<NeutralCard> cardHand = new ArrayList<>(2);
    public volatile int moves = 0;
    private int credit = 2;
    private boolean active = true;
    private int moveCost = 0;

    public static void refund(NeutralPlayer target) {
        int refundValue = Math.min(GameBoard.getInstance().getBankCredit(), target.getMoveCost());
        target.setCredit(target.getCredit() + refundValue);
        GameBoard.getInstance().setBankCredit(GameBoard.getInstance().getBankCredit() - refundValue);

        log(Acts.refund, List.of(target));
    }

    public static boolean challenge(NeutralPlayer target, NeutralCard cardPlayed) {
        NeutralPlayer operator = null;
        Collections.shuffle(GameBoard.getInstance().getActivePlayers());
        for (NeutralPlayer player : GameBoard.getInstance().getActivePlayers()) {
            if (!player.equals(target)) {
                if (player.challengeAct(target, cardPlayed)) {
                    operator = player;
                    break;
                }
            }
        }

        if (operator == null) {
            return false;
        }
        log(Acts.challenge, List.of(operator, target));

        Challenge challenge = new Challenge(operator, target, cardPlayed);
        if (challenge.isSucceed()) {
            if (!(target instanceof Player)) {
                target.getActiveCards().get(new Random().nextInt(target.getActiveCards().size())).setActive(false);
            } else {
                new EliminationGui().choice(1, target.getActiveCards(), target).get(0).setActive(false);
            }
            log(Acts.eliminate, List.of(target));

            target.isActive();

            if (target.refundAct()) {
                refund(target);
            }
            return true;
        } else {
            if (!(operator instanceof Player)) {
                operator.getActiveCards().get(new Random().nextInt(operator.getActiveCards().size())).setActive(false);
            } else {
                new EliminationGui().choice(1, operator.getActiveCards(), operator).get(0).setActive(false);
            }
            log(Acts.eliminate, List.of(operator));

            operator.isActive();

            NeutralCard randomCard = GameBoard.getInstance().getRandomCard();

            NeutralCard found = null;
            for (NeutralCard card : target.getCardHand()) {
                if (card.getClass().equals(cardPlayed.getClass())) {
                    found = card;
                }
            }

            target.getCardHand().set(target.getCardHand().lastIndexOf(found), randomCard);
            GameBoard.getInstance().getCardsList().set(GameBoard.getInstance().getCardsList().lastIndexOf(randomCard), cardPlayed);
            Collections.shuffle(GameBoard.getInstance().getCardsList());
            return false;
        }
    }

    static Class<?> toType(String name) {
        Class<?> type = null;
        switch (name) {
            case "Ambassador":
                type = Ambassador.class;
            case "Assassin":
                type = Assassin.class;
            case "Captain":
                type = Captain.class;
            case "Condessa":
                type = Condessa.class;
            case "Duke":
                type = Duke.class;
        }
        return type;
    }

    public String pathToPic() {
        if (this instanceof Imposter) {
            return pathToImposterCard;
        } else if (this instanceof Paranoid) {
            return pathToParanoidCard;
        } else if (this instanceof Grandmaster) {
            return pathToGrandMasterCard;
        } else if (this instanceof Couper) {
            return pathToCouperCard;
        } else if (this instanceof Player) {
            return pathToPlayerCard;
        }
        return null;
    }

    public void play() throws InterruptedException, IOException, FontFormatException {
    }

    public void income() {
        setMoveCost(0);
        int change = Math.min(incomeValue, GameBoard.getInstance().getBankCredit());
        setCredit(getCredit() + change);
        GameBoard.getInstance().setBankCredit(GameBoard.getInstance().getBankCredit() - change);
        log(Acts.income, List.of(this));
        moves++;
        setMoveCost(0);
    }

    public void foreignAid() {
        setMoveCost(0);
        boolean blocked = false;

        ArrayList<NeutralPlayer> temp = new ArrayList<>(GameBoard.getInstance().getActivePlayers());
        for (NeutralPlayer player : temp) {
            if (!player.equals(this) && player.block(new NeutralCard(this))) {
                blocked = true;
                break;
            }
        }

        if (!blocked) {
            int change = Math.min(foreignAidValue, GameBoard.getInstance().getBankCredit());
            setCredit(getCredit() + change);
            GameBoard.getInstance().setBankCredit(GameBoard.getInstance().getBankCredit() - change);
            log(Acts.foreignAid, List.of(this));
        }

        moves++;
        setMoveCost(0);
    }

    public void exchange() {
        setMoveCost(exchangePrice);
        if (isActive()) {
            if (getCredit() >= exchangePrice) {
                setMoveCost(exchangePrice);
                setCredit(getCredit() - exchangePrice);
                GameBoard.getInstance().setBankCredit(GameBoard.getInstance().getBankCredit() + exchangePrice);

                NeutralCard chosen;
                if (!(this instanceof Player)) {
                    ArrayList<NeutralCard> activeCards = new ArrayList<>();
                    for (NeutralCard card : getCardHand()) {
                        if (card.isActive()) {
                            activeCards.add(card);
                        }
                    }
                    chosen = activeCards.get(new Random().nextInt(activeCards.size()));
                } else {
                    chosen = new EliminationGui().choice(1, getActiveCards(), this).get(0);
                }

                NeutralCard randomCard = GameBoard.getInstance().getRandomCard();

                getCardHand().set(getCardHand().lastIndexOf(chosen), randomCard);
                GameBoard.getInstance().getCardsList().set(GameBoard.getInstance().getCardsList().lastIndexOf(randomCard), chosen);
                Collections.shuffle(GameBoard.getInstance().getCardsList());

                log(Acts.exchange, List.of(this));
            }
        }
        moves++;
        setMoveCost(0);
    }

    public void coup(NeutralPlayer target) throws IOException, FontFormatException {
        if (getCredit() >= coupPrice) {
            setMoveCost(coupPrice);
            setCredit(getCredit() - coupPrice);
            GameBoard.getInstance().setBankCredit(GameBoard.getInstance().getBankCredit() + coupPrice);

            if (target.isActive()) {
                if (!(target instanceof Player)) {
                    target.getActiveCards().get(new Random().nextInt(target.getActiveCards().size())).setActive(false);
                } else {
                    new EliminationGui().choice(1, target.getActiveCards(), target).get(0).setActive(false);
                }
                target.isActive();

                log(Acts.coup, List.of(this, target));
            }
        }
        moves++;
        setMoveCost(0);
    }

    public boolean block(NeutralCard cardPlayed) {
        if (!blockAct(cardPlayed)) {
            return false;
        }
        log(Acts.block, List.of(this));

        NeutralCard defender = null;
        if (cardPlayed.getClass().equals(NeutralCard.class)) {
            defender = new Duke(this);
        } else if (cardPlayed.getClass().equals(Assassin.class)) {
            defender = new Condessa(this);
        } else if (cardPlayed.getClass().equals(Captain.class)) {
            boolean rand = false;

            if (hasCard(Captain.class)) {
                rand = true;
                defender = new Captain(this);
            } else if (hasCard(Ambassador.class)) {
                rand = true;
                defender = new Ambassador(this);
            }

            if (!rand) {
                int choice = new Random().nextInt(2);
                if (choice == 0) {
                    defender = new Captain(this);
                } else {
                    defender = new Ambassador(this);
                }
            }
        }
        return !challenge(this, defender);
    }

    public NeutralPlayer target() throws InterruptedException {
        if (!(this instanceof Player)) {
            ArrayList<NeutralPlayer> targets = new ArrayList<>();
            for (NeutralPlayer player : GameBoard.getInstance().getActivePlayers()) {
                if (!player.equals(this)) {
                    targets.add(player);
                }
            }
            return targets.get(new Random().nextInt(targets.size()));
        } else {
            return new TargetGui().choose();
        }
    }

    public boolean hasCard(Class<?> type) {
        for (NeutralCard card : getCardHand()) {
            if (card.isActive() && card.getClass().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean challengeAct(NeutralPlayer target, NeutralCard cardPlayed) {
        return false;
    }

    public boolean blockAct(NeutralCard card) {
        return false;
    }

    public boolean refundAct() {
        return false;
    }

    public ArrayList<NeutralCard> getCardHand() {
        return cardHand;
    }

    public void addCardHand(NeutralCard card) {
        this.cardHand.add(card);
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public boolean isActive() {
        if (getActiveCards().size() != 0) {
            setActive(true);
            return true;
        }
        setActive(false);
        return false;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<NeutralCard> getActiveCards() {
        ArrayList<NeutralCard> activeCards = new ArrayList<>();
        for (NeutralCard card : getCardHand()) {
            if (card.isActive()) {
                activeCards.add(card);
            }
        }
        return activeCards;
    }

    public int getMoveCost() {
        return moveCost;
    }

    public void setMoveCost(int moveCost) {
        this.moveCost = moveCost;
    }
}
