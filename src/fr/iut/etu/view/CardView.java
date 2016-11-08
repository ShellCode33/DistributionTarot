package fr.iut.etu.view;

import fr.iut.etu.model.Card;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
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

        ImageView front = new ImageView(imageFace);
        ImageView back = new ImageView(imageBottom);
        front.setTranslateZ(0);
        back.setTranslateZ(1);

        getChildren().addAll(front);
    }

    @Override
    public void update(Observable observable, Object o) {
        RotateTransition rotate = new RotateTransition(Duration.seconds(2), this);

        rotate.setAxis(Rotate.Y_AXIS);
        rotate.setFromAngle(0);
        rotate.setToAngle(180);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setCycleCount(1);
        rotate.play();
    }
}
