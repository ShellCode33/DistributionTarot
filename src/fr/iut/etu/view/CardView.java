package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Card;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

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

        setTranslateY(-Controller.CARD_HEIGHT);

//        RotateTransition rotation1 = new RotateTransition(Duration.seconds(1), this);
//        rotation1.setByAngle(90);
//        rotation1.setAxis(Rotate.X_AXIS);
//        rotation1.setCycleCount(1);
//
//        RotateTransition rotation2 = new RotateTransition(Duration.seconds(1), this);
//        rotation2.setByAngle(180);
//        rotation2.setAxis(Rotate.X_AXIS);
//        rotation2.setCycleCount(1);
//
//        RotateTransition rotation3 = new RotateTransition(Duration.seconds(1), this);
//        rotation3.setByAngle(-90);
//        rotation3.setAxis(Rotate.X_AXIS);
//        rotation3.setCycleCount(1);
//
//        rotation1.play();
//        rotation1.setOnFinished(event1 -> {
//            rotation2.play();
//            rotation2.setOnFinished(event2 -> {
//                rotation3.play();
//            } );
//        });
    }
}