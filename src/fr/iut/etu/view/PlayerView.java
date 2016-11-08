package fr.iut.etu.view;

import fr.iut.etu.model.Notifications;
import fr.iut.etu.model.Player;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class PlayerView extends Group implements Observer {

    private Player player;

    private ArrayList<CardView> cardViews = new ArrayList<>();

    public PlayerView(Player player) {
        super();

        this.player = player;
        this.player.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object o) {
        if(o != null && o == Notifications.CARD_PICKED){

            DeckView deckView = ((BoardView) getParent()).getDeckView();
            Bounds deckViewBoundsInLocal = deckView.getBoundsInLocal();
            Point2D deckViewCenter= deckView.localToParent(deckViewBoundsInLocal.getWidth() / 2, deckViewBoundsInLocal.getHeight() / 2);

            CardView cardView = new CardView(player.getCards().get(player.getCardCount() - 1));
            cardViews.add(cardView);
            getChildren().add(cardView);

            Bounds cardViewBoundsInLocal = cardView.getBoundsInLocal();
            Point2D cardViewCenter = cardView.localToParent(cardViewBoundsInLocal.getWidth() / 2, cardViewBoundsInLocal.getHeight() / 2);
            cardView.setTranslateX(deckViewCenter.getX() - cardViewCenter.getX());
            cardView.setTranslateY(deckViewCenter.getY() - cardViewCenter.getY());
            cardViewCenter = cardView.localToParent(cardViewBoundsInLocal.getWidth() / 2, cardViewBoundsInLocal.getHeight() / 2);

            Bounds boundsInLocal = getBoundsInLocal();
            Point2D center = localToParent(boundsInLocal.getWidth() / 2, boundsInLocal.getHeight() / 2);

            TranslateTransition translate = new TranslateTransition(Duration.seconds(1), cardView);
            translate.setToX(center.getX() - cardViewCenter.getX());
            translate.setToY(center.getY() - cardViewCenter.getY());

            translate.setCycleCount(1);
            translate.play();
        }
    }
}
