package fr.iut.etu.view;

import fr.iut.etu.model.Card;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class CardView extends Group implements Observer {
    private Card card;

    private ImageView cardImage = new ImageView();

    public CardView(Card card) {
        this.card = card;
        this.card.addObserver(this);

        Image image;

        if(card.getType() == Card.Type.FOOL){
            image = new Image("file:./res/FOOL.jpg");
        }
        else{
            switch(card.getValue()){
                case 11:
                    image = new Image("file:./res/"+card.getType()+"_Jack.jpg");
                    break;
                case 12:
                    image = new Image("file:./res/"+card.getType()+"_Knight.jpg");
                    break;
                case 13:
                    image = new Image("file:./res/"+card.getType()+"_Queen.jpg");
                    break;
                case 14:
                    image = new Image("file:./res/"+card.getType()+"_King.jpg");
                    break;

                default:
                    image = new Image("file:./res/"+card.getType()+"_"+card.getValue()+".jpg");
                    break;
            }
        }

        cardImage.setImage(image);
    }

    public void setX(double x){
        cardImage.setX(x);
    }

    public void setY(double y) {
        cardImage.setY(y);
    }

    public Card getCard() {
        return card;
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
