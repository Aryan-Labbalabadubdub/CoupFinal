package player;

import cards.NeutralCard;

public class Challenge {
    public static int cnt = 0;
    private NeutralPlayer challenger;
    private NeutralPlayer operator;
    private NeutralCard cardPlayed;
    private boolean succeed;

    public Challenge(NeutralPlayer triggerPlayer, NeutralPlayer targetPlayer, NeutralCard objectedCard) {
        this.challenger = triggerPlayer;
        this.operator = targetPlayer;
        this.cardPlayed = objectedCard;
        cnt++;
    }

    public void checkChallenge() {
        if (getOperator().hasCard(getCardPlayed().getClass())) {
            setSucceed(false);
            return;
        }
        setSucceed(true);
    }

    public NeutralPlayer getOperator() {
        return operator;
    }

    public void setOperator(NeutralPlayer operator) {
        this.operator = operator;
    }

    public NeutralCard getCardPlayed() {
        return cardPlayed;
    }

    public void setCardPlayed(NeutralCard cardPlayed) {
        this.cardPlayed = cardPlayed;
    }

    public boolean isSucceed() {
        checkChallenge();
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public NeutralPlayer getChallenger() {
        return challenger;
    }

    public void setChallenger(NeutralPlayer challenger) {
        this.challenger = challenger;
    }
}
