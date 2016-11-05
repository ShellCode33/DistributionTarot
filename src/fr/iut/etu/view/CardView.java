package fr.iut.etu.view;

import fr.iut.etu.model.Card;
import javafx.scene.image.Image;
import javafx.scene.shape.Box;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class CardView extends Box implements Observer {
    private Card card;

    public CardView(Card card) {

        super(100,200,1);


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
                    imageFace = new Image("file:./res/cards/"+card.getType()+"_Jack.jpg");
                    break;
                case 12:
                    imageFace = new Image("file:./res/cards/"+card.getType()+"_Knight.jpg");
                    break;
                case 13:
                    imageFace = new Image("file:./res/cards/"+card.getType()+"_Queen.jpg");
                    break;
                case 14:
                    imageFace = new Image("file:./res/cards/"+card.getType()+"_King.jpg");
                    break;

                default:
                    imageFace = new Image("file:./res/cards/"+card.getType()+"_"+card.getValue()+".jpg");
                    break;
            }
        }

    }

    @Override
    public void update(Observable observable, Object o) {


    }
}
