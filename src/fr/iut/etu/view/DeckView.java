package fr.iut.etu.view;

import fr.iut.etu.model.Deck;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.shape.Box;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class DeckView extends Box implements Observer {

    private Deck deck;

    public DeckView(Deck deck) {
        super(100,200,deck.size());

        this.deck = deck;
        deck.addObserver(this);

        Image image = new Image("file:./res/deck.jpg");

        Tooltip.install(this, new Tooltip(this.deck.size() + " cards !"));
    }

    @Override
    public void update(Observable observable, Object o) {
        setDepth(deck.size());
    }
}
