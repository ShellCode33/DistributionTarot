package fr.iut.etu.view;

import fr.iut.etu.model.Deck;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class DeckView extends ImageView implements Observer {

    private Deck deck;

    public DeckView(Deck deck) {
        super();

        this.deck = deck;
        deck.addObserver(this);

        Image image = new Image("file:./res/deck.jpg");

        setImage(image);

        setFitWidth(120);
        setFitHeight(200);

        Tooltip.install(this, new Tooltip(this.deck.getSize() + " cards !"));
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
