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
import javafx.scene.image.ImageView;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class DeckView extends Group implements Observer {

    private Deck deck;
    private Image backCard;
    private LinkedList<CardView> cardViewsWaitingToBeDealt = new LinkedList<>();
    private Animation cutAnimation;

    public DeckView(Deck deck, Image backCardCustom) {
        super();

        this.deck = deck;
        deck.addObserver(this);

        if(backCardCustom != null)
            backCard = new Image("file:res/back0.jpg");

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
            cardViewsWaitingToBeDealt.push(new CardView(deck.getLastCardDealt(), backCard));
        }
        else if(o == Notifications.CARD_ADDED){
            ImageView imageView = new ImageView(backCard);
            imageView.setSmooth(true);
            imageView.setFitHeight(Controller.CARD_HEIGHT);
            imageView.setFitWidth(Controller.CARD_WIDTH);
            imageView.setTranslateZ(-getChildren().size()*Controller.CARD_THICK);

            getChildren().add(imageView);
        }

        Tooltip.install(this, new Tooltip(this.deck.size() + " cards!"));
    }

    public LinkedList<CardView> getCardViewsWaitingToBeDealt() {
        return cardViewsWaitingToBeDealt;
    }
}
