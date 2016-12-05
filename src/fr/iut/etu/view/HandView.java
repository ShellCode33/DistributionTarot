package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Hand;
import fr.iut.etu.model.Notifications;
import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 11/18/16.
 */
public abstract class HandView extends Group implements Observer {
    protected final Hand hand;
    protected final ArrayList<CardView> cardViews = new ArrayList<>();
    private final LinkedList<CardView> cardViewsWaitingToBeDealt = new LinkedList<>();
    private final LinkedList<CardView> cardViewsWaitingToBeTransfered = new LinkedList<>();

    static final int GAP_BETWEEN_CARDS = (int) (40 * Controller.SCALE_COEFF);

    HandView(Hand hand) {
        super();

        this.hand = hand;
        this.hand.addObserver(this);

    }

    public Animation getFlipAllCardViewsAnimation() {
        ParallelTransition pt = new ParallelTransition();

        for (CardView cardView : cardViews) {
            pt.getChildren().add(cardView.getFlipAnimation());
        }

        return pt;
    }

    public abstract Animation getDispatchAnimation();

    @Override
    public void update(Observable observable, Object o) {
        if(o == Notifications.CARD_ADDED) {
            cardViewsWaitingToBeDealt.push(new CardView(hand.getLastCardAdded()));
        }
        else if(o == Notifications.CARD_TRANSFERED){

            CardView cardView = null;
            for (CardView cv : cardViews) {
                if(cv.getCard() == hand.getLastCardTransfered()){
                    cardView = cv;
                    break;
                }
            }

            cardViewsWaitingToBeTransfered.push(cardView);
        }
        else if(o == Notifications.CARD_DELETED){

            CardView relatedImageView = null;

            for (CardView cardView : cardViews)
                if (cardView.getCard() == hand.getLastCardRemoved())
                    relatedImageView = cardView;

            cardViews.remove(relatedImageView);
            getChildren().remove(relatedImageView);
        }
    }

    public Animation getSortAnimation() {

        ParallelTransition pt = new ParallelTransition();

        cardViews.sort(CardView::compareTo);

        for (int i = 0; i < cardViews.size(); i++) {
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), cardViews.get(i));
            translateTransition.setToX(i*GAP_BETWEEN_CARDS -  cardViews.size()*GAP_BETWEEN_CARDS/2);
            translateTransition.setToZ(-1-i* CardView.CARD_THICK);
            translateTransition.setCycleCount(1);
            pt.getChildren().add(translateTransition);
        }


        return pt;
    }

    public void addCardView(CardView cardView) {
        getChildren().add(cardView);
        cardViews.add(cardView);
    }

    public ArrayList<CardView> getCardViews() {
        return cardViews;
    }

    public Animation transferCardViewTo(HandView handView) {

        ParallelTransition pt = new ParallelTransition();

        CardView cardView = cardViewsWaitingToBeTransfered.poll();
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), cardView);

        Transition firstAnimation = new Transition() {@Override protected void interpolate(double frac) {}};
        firstAnimation.setOnFinished(event -> {

            Bounds boundsInParent = cardView.getBoundsInParent();
            Bounds bounds = handView.parentToLocal(localToParent(boundsInParent));

            handView.addCardView(cardView);
            cardViews.remove(cardView);

            tt.setFromX(bounds.getMinX());
            tt.setFromY(bounds.getMinY());
            tt.setToX(0);
            tt.setToY(0);
            tt.setToZ(-0.2);
            pt.getChildren().add(tt);
        });


        SequentialTransition st = new SequentialTransition();
        st.getChildren().addAll(firstAnimation, tt);

        return st;
    }

    public LinkedList<CardView> getCardViewsWaitingToBeDealt() {
        return cardViewsWaitingToBeDealt;
    }
}
