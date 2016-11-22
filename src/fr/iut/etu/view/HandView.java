package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Hand;
import fr.iut.etu.model.Notifications;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
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
    private Hand hand;
    protected ArrayList<CardView> cardViews = new ArrayList<>();
    private LinkedList<CardView> cardViewsWaitingToBeDealt = new LinkedList<>();

    protected static int GAP_BETWEEN_CARDS = (int) (40 * Controller.SCALE_COEFF);

    public HandView(Hand hand) {
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
        else if(o == Notifications.CARD_DELETED){

        }
    }

    public Animation getSortAnimation() {
        ParallelTransition pt = new ParallelTransition();

        for (int i = 0; i < cardViews.size(); i++) {
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), cardViews.get(i));
            translateTransition.setToX(i*GAP_BETWEEN_CARDS -  cardViews.size()*GAP_BETWEEN_CARDS/2);
            translateTransition.setToZ(-1-i* Controller.CARD_THICK);
            translateTransition.setCycleCount(1);
            pt.getChildren().add(translateTransition);
        }

        return pt;
    }

    public void sort() {
        cardViews.sort(CardView::compareTo);
    }

    public void addCard(CardView cardView) {
        getChildren().add(cardView);
        cardViews.add(cardView);
    }

    public ArrayList<CardView> getCardViews() {
        return cardViews;
    }

    public Animation transferCardViewsTo(HandView playerView) {

        ParallelTransition pt = new ParallelTransition();

        Bounds boundsInScenePlayer = playerView.localToScreen(playerView.getBoundsInLocal());

        for(CardView cardView : cardViews) {

            Bounds boundsInSceneCard = cardView.localToScreen(cardView.getBoundsInLocal());
            double x_translate = boundsInScenePlayer.getMinX() - boundsInSceneCard.getMinX();
            double y_translate = boundsInScenePlayer.getMinY() - boundsInSceneCard.getMinY();
            System.out.println("x_translate: " + x_translate);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(2), cardView);
            tt.setFromY(-y_translate);
            tt.setToY(0);
            pt.getChildren().add(tt);

            playerView.addCard(cardView); //Local coordinates change here
            cardView.setTranslateX(-x_translate-400); //TODO : Pourquoi -400?....
        }

        cardViews.clear();

        return pt;
    }

    public LinkedList<CardView> getCardViewsWaitingToBeDealt() {
        return cardViewsWaitingToBeDealt;
    }
}
