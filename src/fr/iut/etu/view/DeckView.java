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

        image = new Image("file:res/deck.jpg");

        update(deck, null);
    }

    @Override
    public void update(Observable observable, Object o) {

        int childrenCount = getChildren().size();
        int deckSize = deck.size();

        System.out.println("childrenCount: " + childrenCount);
        System.out.println("deckSize: " + deckSize);

        if(childrenCount < deckSize){
            for(int i = childrenCount; i < deckSize; i++) {
                ImageView view = new ImageView(image);
                view.setTranslateZ(-Controller.CARD_THICK*Controller.SCALE_COEFF*78-i*Controller.CARD_THICK*Controller.SCALE_COEFF);
                view.setScaleX(Controller.SCALE_COEFF);
                view.setScaleY(Controller.SCALE_COEFF);
                view.setFitWidth(Controller.CARD_WIDTH);
                view.setFitHeight(Controller.CARD_HEIGHT);
                getChildren().add(view);
            }
        }
        else {
            for (int i = childrenCount; i > deckSize; i--)
                getChildren().remove(0);
        }

        Tooltip.install(this, new Tooltip(this.deck.size() + " cards!"));
    }

    public void cutAnimation() {

    }
}
