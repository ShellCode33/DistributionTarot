package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Deck;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class DeckView extends Group implements Observer {

    private Deck deck;
    private Image image;
    private ArrayList<ImageView> images = new ArrayList<>();

    private Animation cutAnimation;

    public DeckView(Deck deck) {
        super();

        this.deck = deck;
        deck.addObserver(this);

        image = new Image("file:res/back.jpg");

        for(int i = 0; i < 78; i++) {
                ImageView view = new ImageView(image);
                view.setTranslateZ(-Controller.CARD_THICK*78-i*Controller.CARD_THICK);
                view.setFitWidth(Controller.CARD_WIDTH);
                view.setFitHeight(Controller.CARD_HEIGHT);
                images.add(view);
                getChildren().add(view);
        }


        update(deck, null);

        createCutAnimation();
    }

    private void createCutAnimation() {

        SequentialTransition st = new SequentialTransition();
        cutAnimation = st;
    }

    public Animation getCutAnimation() {
        return cutAnimation;
    }

    @Override
    public void update(Observable observable, Object o) {

        int childrenCount = getChildren().size();
        int deckSize = deck.size();

//        if(childrenCount < deckSize){
//            for(int i = childrenCount; i < deckSize; i++) {
//                ImageView view = new ImageView(image);
//                view.setTranslateZ(-Controller.CARD_THICK*78-i*Controller.CARD_THICK);
//                view.setFitWidth(Controller.CARD_WIDTH);
//                view.setFitHeight(Controller.CARD_HEIGHT);
//                images.add(view);
//                getChildren().add(view);
//            }
//        }
//        else {
//            for (int i = childrenCount; i > deckSize; i--)
//                getChildren().remove(0);
//        }

        Tooltip.install(this, new Tooltip(this.deck.size() + " cards!"));
    }

}
