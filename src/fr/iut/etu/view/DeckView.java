package fr.iut.etu.view;

import fr.iut.etu.model.Deck;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(deck.getSize() + " cards !");
            }
        });


    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
