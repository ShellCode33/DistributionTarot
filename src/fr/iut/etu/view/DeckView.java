package fr.iut.etu.view;

import fr.iut.etu.model.Card;
import fr.iut.etu.model.Deck;
import javafx.scene.Group;
import javafx.scene.Node;

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

        for (Card card : deck.getCards()) {
            getChildren().add(new CardView(card));
        }

    }

    public void setX(double x){
        for (Node node : getChildren()) {
            if(node instanceof CardView){
                ((CardView) node).setX(x);
            }
        }
    }

    public void setY(double y){
        for (Node node : getChildren()) {
            if(node instanceof CardView){
                ((CardView) node).setY(y);
            }
        }
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
