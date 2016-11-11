package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Card;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
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
        back.setScaleX(Controller.SCALE_COEFF);
        back.setScaleY(Controller.SCALE_COEFF);
        back.setFitHeight(Controller.CARD_HEIGHT);
        back.setFitWidth(Controller.CARD_WIDTH);
        back.setTranslateZ(-Controller.CARD_WIDTH/2-1.01);

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

//        RotateTransition rotate = new RotateTransition(Duration.seconds(2), this);
//
//        rotate.setAxis(Rotate.Y_AXIS);
//        rotate.setFromAngle(0);
//        rotate.setToAngle(180);
//        rotate.setInterpolator(Interpolator.LINEAR);
//        rotate.setCycleCount(1);
//        rotate.play();


        Bounds boundsInLocal = getBoundsInLocal();

        Rotate rotate1 = new Rotate(0, boundsInLocal.getWidth()/2, boundsInLocal.getHeight()/2, -Controller.CARD_HEIGHT/2);
        rotate1.setAxis(Rotate.X_AXIS);
        getTransforms().add(rotate1);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(rotate1.angleProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(rotate1.angleProperty(), 90))
        );
        timeline.play();

        timeline.setOnFinished(actionEvent -> {
            Rotate rotate2 = new Rotate(0, boundsInLocal.getWidth()/2, boundsInLocal.getHeight()/2, boundsInLocal.getDepth()/2);
            rotate2.setAxis(Rotate.X_AXIS);
            getTransforms().add(rotate2);

            Timeline timeline2 = new Timeline();
            timeline2.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO, new KeyValue(rotate2.angleProperty(), 0)),
                    new KeyFrame(Duration.seconds(2), new KeyValue(rotate2.angleProperty(), 180))
            );
            timeline2.play();
        });
    }
}