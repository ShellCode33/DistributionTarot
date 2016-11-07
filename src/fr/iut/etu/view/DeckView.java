package fr.iut.etu.view;

import fr.iut.etu.model.Deck;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class DeckView extends Group implements Observer {

    private Deck deck;

    public DeckView(Deck deck) {
        super();

        this.deck = deck;
        deck.addObserver(this);

        Image image = new Image("file:./res/deck.jpg");

        int nb_cards_to_draw = this.deck.size();

        for(int i = 0; i < nb_cards_to_draw; i++) {
            ImageView view = new ImageView(image);
            view.setTranslateZ(i);
            view.setFitWidth(image.getWidth()/5);
            view.setFitHeight(image.getHeight()/5);
            getChildren().add(view);
        }

        Tooltip.install(this, new Tooltip(this.deck.size() + " cards !"));
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
