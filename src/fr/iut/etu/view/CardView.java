package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Card;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class CardView extends Group implements Observer, Comparable<CardView> {
    private Card card;
    private ImageView front = null, back;
    private boolean animVertical = true;

    public CardView(Card card) {

        this.card = card;
        this.card.addObserver(this);

        Image imageBottom = new Image("file:res/back.jpg");

        back = new ImageView(imageBottom);
        back.setSmooth(true);
        back.setFitHeight(Controller.CARD_HEIGHT);
        back.setFitWidth(Controller.CARD_WIDTH);
        back.setTranslateZ(-1.01);

        getChildren().add(back);
    }

    @Override
    public void update(Observable observable, Object o) {

        if(front == null) {

            //La carte se montre

            Image imageFace;

            if (card.getType() == Card.Type.FOOL) {
                imageFace = new Image("file:./res/FOOL.png");
            } else {
                imageFace = new Image("file:./res/" + card.getType().toString() + "_" + card.getValue() + ".png");
            }

            front = new ImageView(imageFace);
            front.setFitHeight(Controller.CARD_HEIGHT);
            front.setFitWidth(Controller.CARD_WIDTH);

            front.setTranslateZ(-1);

            front.setRotationAxis(Rotate.Y_AXIS);
            front.setRotate(180);

            getChildren().add(front);
        }

        else {

            getChildren().remove(front);
            front = null;
        }

        double width = Controller.CARD_WIDTH;
        double height = Controller.CARD_HEIGHT;

        TranslateTransition translate1 = new TranslateTransition(Duration.seconds(0.3), this);

        if(animVertical)
            translate1.setByY(-height);
        else
            translate1.setByX(-width);

        translate1.setCycleCount(1);

        TranslateTransition translate2 = new TranslateTransition(Duration.seconds(0.5), this);
        translate2.setByZ(-height/2);
        translate2.setCycleCount(1);

        RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), this);
        rotate.setByAngle(180);

        rotate.setAxis(animVertical ? Rotate.X_AXIS : Rotate.Y_AXIS);
        rotate.setCycleCount(1);

        TranslateTransition translate3 = new TranslateTransition(Duration.seconds(0.3), this);
        translate3.setByZ(height/2);
        translate3.setCycleCount(1);

        TranslateTransition translate4 = new TranslateTransition(Duration.seconds(0.3), this);
        if(animVertical)
            translate4.setByY(height);
        else
            translate4.setByX(width);

        translate4.setCycleCount(1);

        translate1.play();

        translate1.setOnFinished(event1 -> {

            translate2.play();
            rotate.play();

            translate2.setOnFinished(event2 -> {

                translate3.play();

                translate3.setOnFinished(event3 -> {

                    translate4.play();

                });
            });

        });
    }

    public void setVertical(boolean vertical) {
        animVertical = vertical;
    }

    @Override
    public int compareTo(CardView cardView) {
        return card.compareTo(cardView.card);
    }

    @Override
    public String toString() {
        return card.toString();
    }
}