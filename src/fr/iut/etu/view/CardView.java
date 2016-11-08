package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Card;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class CardView extends Group implements Observer {
    private Card card;
    private ImageView front, back;

    private int count = 0;

    public CardView(Card card) {

        this.card = card;
        this.card.addObserver(this);

        Image imageBottom = new Image("file:./res/back.jpg");

        Image imageFace;

        if(card.getType() == Card.Type.FOOL){
            imageFace = new Image("file:./res/cards/FOOL.jpg");
        }
        else{
            switch(card.getValue()){
                case 11:
                    imageFace = new Image("file:./res/cards/"+card.getType().toString()+"_Jack.jpg");
                    break;
                case 12:
                    imageFace = new Image("file:./res/cards/"+card.getType().toString()+"_Knight.jpg");
                    break;
                case 13:
                    imageFace = new Image("file:./res/cards/"+card.getType().toString()+"_Queen.jpg");
                    break;
                case 14:
                    imageFace = new Image("file:./res/cards/"+card.getType().toString()+"_King.jpg");
                    break;

                default:
                    System.out.println("loading file:./res/cards/"+card.getType().toString()+"_"+card.getValue()+".jpg");
                    imageFace = new Image("file:./res/cards/"+card.getType().toString()+"_"+card.getValue()+".jpg");
                    break;
            }
        }

        front = new ImageView(imageFace);
        back = new ImageView(imageBottom);
        front.setFitHeight(Controller.CARD_HEIGHT);
        front.setFitWidth(Controller.CARD_WIDTH);
        back.setFitHeight(Controller.CARD_HEIGHT);
        back.setFitWidth(Controller.CARD_WIDTH);

        front.setTranslateZ(-Controller.CARD_WIDTH/2-1);
        back.setTranslateZ(-Controller.CARD_WIDTH/2-1.01);

        front.setRotationAxis(Rotate.Y_AXIS);
        front.setRotate(180);

        getChildren().add(front);
        getChildren().add(back);
    }

    @Override
    public void update(Observable observable, Object o) {
        RotateTransition rotate = new RotateTransition(Duration.seconds(2), this);

        rotate.setAxis(Rotate.Y_AXIS);
        rotate.setFromAngle(0);
        rotate.setToAngle(360);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setCycleCount(Animation.INDEFINITE);
        rotate.play();
    }
}