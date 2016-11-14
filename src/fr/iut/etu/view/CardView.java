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
public class CardView extends Group implements Observer {
    private Card card;
    private ImageView front = null, back;

    public CardView(Card card) {

        this.card = card;
        this.card.addObserver(this);

        Image imageBottom = new Image("file:res/back.jpg");

        back = new ImageView(imageBottom);
        back.setSmooth(true);
        back.setFitHeight(Controller.CARD_HEIGHT);
        back.setFitWidth(Controller.CARD_WIDTH);
        back.setScaleX(Controller.SCALE_COEFF);
        back.setScaleY(Controller.SCALE_COEFF);
        back.setTranslateZ(-Controller.CARD_WIDTH/2-1.01);

        getChildren().add(back);
    }

    @Override
    public void update(Observable observable, Object o) {

        boolean vertical = (boolean)o; //type d'animation

        if(front == null) {

            //La carte se montre

            Image imageFace;

            if (card.getType() == Card.Type.FOOL) {
                imageFace = new Image("file:./res/FOOL.png");
            } else {
                imageFace = new Image("file:./res/" + card.getType().toString() + "_" + card.getValue() + ".png");
            }

            front = new ImageView(imageFace);
            front.setScaleX(Controller.SCALE_COEFF);
            front.setScaleY(Controller.SCALE_COEFF);
            front.setFitHeight(Controller.CARD_HEIGHT);
            front.setFitWidth(Controller.CARD_WIDTH);

            front.setTranslateZ(-Controller.CARD_WIDTH / 2 - 1);

            front.setRotationAxis(Rotate.Y_AXIS);
            front.setRotate(180);

            getChildren().add(front);
        }

        else {

            getChildren().remove(front);
            front = null;
        }

        double width = Controller.CARD_WIDTH*Controller.SCALE_COEFF;
        double height = Controller.CARD_HEIGHT*Controller.SCALE_COEFF;
        double depth = Controller.CARD_THICK*Controller.SCALE_COEFF;

        TranslateTransition translate1 = new TranslateTransition(Duration.seconds(0.3), this);

        if(vertical)
            translate1.setByY(-height);
        else
            translate1.setByX(-width);

        translate1.setCycleCount(1);

        TranslateTransition translate2 = new TranslateTransition(Duration.seconds(0.5), this);
        translate2.setByZ(-height/2);
        translate2.setCycleCount(1);

        RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), this);
        rotate.setByAngle(180);

        rotate.setAxis(vertical ? Rotate.X_AXIS : Rotate.Y_AXIS);
        rotate.setCycleCount(1);

        TranslateTransition translate3 = new TranslateTransition(Duration.seconds(0.3), this);
        translate3.setByZ(height/2);
        translate3.setCycleCount(1);

        TranslateTransition translate4 = new TranslateTransition(Duration.seconds(0.3), this);
        if(vertical)
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
}