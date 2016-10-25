package fr.iut.etu.view;

import fr.iut.etu.model.Deck;
import javafx.scene.Group;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class DeckView extends Group implements Observer {
    private Deck deck;

    public DeckView(Deck deck) {
        this.deck = deck;
        deck.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
