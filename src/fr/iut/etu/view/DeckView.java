package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Deck;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class DeckView extends Group implements Observer {

    private Deck deck;
    private Image image;

    public DeckView(Deck deck) {
        super();

        this.deck = deck;
        deck.addObserver(this);

        image = new Image("file:./res/deck.jpg");

        for(int i = 0; i < this.deck.size(); i++) {
            ImageView view = new ImageView(image);
            view.setTranslateZ(i*Controller.CARD_DEPTH);
            view.setFitWidth(Controller.CARD_WIDTH);
            view.setFitHeight(Controller.CARD_HEIGHT);
            getChildren().add(view);
        }

        Tooltip.install(this, new Tooltip(this.deck.size() + " cards !"));
    }

    @Override
    public void update(Observable observable, Object o) {
        getChildren().clear();

        for(int i = 0; i < this.deck.size(); i++) {
            ImageView view = new ImageView(image);
            view.setTranslateZ(i);
            view.setFitWidth(image.getWidth()/5);
            view.setFitHeight(image.getHeight()/5);
            getChildren().add(view);
        }

    }
}
