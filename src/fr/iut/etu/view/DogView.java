package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Card;
import fr.iut.etu.model.Hand;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by shellcode on 11/14/16.
 */
public class DogView extends Group implements Observer {

    Hand dog;
    private ArrayList<CardView> cardViews = new ArrayList<>();

    public DogView(Hand dog) {
        this.dog = dog;
        dog.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object o) {
        System.out.println("Add card to dog");
        CardView cardView = new CardView(dog.getCards().get(dog.getCardCount()-1));
        cardView.setTranslateZ(-dog.getCardCount()-1);

        //ANIMATION DU DECK VERS LE CHIEN

        cardViews.add(cardView);
        getChildren().add(cardView);
    }

    public void dispatch() {

        System.out.println("Dispatch dog");

        for(int i = 0; i < this.getChildren().size(); i++) {
            Timeline timeline = new Timeline();

            KeyFrame cut = new KeyFrame(Duration.seconds(1),
                    new KeyValue(cardViews.get(i).translateXProperty(), -Controller.CARD_WIDTH*i-i*20));

            timeline.getKeyFrames().add(cut);
            timeline.play();
        }
    }
}
