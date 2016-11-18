package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Deck;
import fr.iut.etu.model.Notifications;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class DeckView extends Group implements Observer {

    private Deck deck;
    private Image image;
    private ArrayList<CardView> cardViews = new ArrayList<>();
    private LinkedList<CardView> cardViewsWaitingToBeDealt = new LinkedList<>();
    private Animation cutAnimation;

    public DeckView(Deck deck) {
        super();

        this.deck = deck;
        deck.addObserver(this);

        image = new Image("file:res/back.jpg");

        createCutAnimation();
    }

    private void createCutAnimation() {

        SequentialTransition st = new SequentialTransition();
        cutAnimation = st;
    }

    public Animation getCutAnimation() {
        return cutAnimation;
    }

    public Animation getDealACardAnimation(HandView handView){

        Point2D handViewPosInBoardView = handView.localToParent(0, 0);
        Point2D handViewPosInDeckView = parentToLocal(handViewPosInBoardView);

        CardView cardView = cardViewsWaitingToBeDealt.poll();

        System.out.println(cardView);

        return null;
    }

    @Override
    public void update(Observable observable, Object o) {
        if( o == null)
            return;

        if(o == Notifications.CARD_DEALED) {
            cardViewsWaitingToBeDealt.push(cardViews.get(cardViews.size() - 1));
        }
        else if(o == Notifications.CARD_ADDED){
            CardView cardView = new CardView(deck.getLastCardAdded(), true);
            cardViews.add(cardView);
            getChildren().add(cardView);
            cardView.setTranslateZ(-cardViews.size()*Controller.CARD_THICK);
        }

        Tooltip.install(this, new Tooltip(this.deck.size() + " cards!"));
    }

}
