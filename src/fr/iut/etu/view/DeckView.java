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

        Tooltip.install(this, new Tooltip(this.deck.size() + " cards!"));
    }

    @Override
    public void update(Observable observable, Object o) {
        int cardsCountDifference = getChildren().size() - deck.size();
        System.out.println("cardsCountDiffenrence: " + cardsCountDifference);

        if(cardsCountDifference < 0){
            for(int i = 0; i < -cardsCountDifference; i++) {
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
            for (int i = 0; i < cardsCountDifference; i++)
                getChildren().remove(0);
        }
    }

    public void cutAnimation() {

    }
}
