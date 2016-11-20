package fr.iut.etu.view;

import fr.iut.etu.model.Hand;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 11/18/16.
 */
public abstract class HandView extends Group implements Observer {
    private Hand hand;

    public HandView(Hand hand) {
        super();

        this.hand = hand;
        this.hand.addObserver(this);

    }

    public Animation getFlipAllCardViewsAnimation() {

        SequentialTransition st = new SequentialTransition();

        for (Node node : getChildren()) {
            if(node instanceof CardView){
                st.getChildren().add(((CardView) node).getFlipAnimation());
            }
        }

        return st;
    }

    public abstract Animation getDispatchAnimation();

    @Override
    public void update(Observable observable, Object o) {

    }
}
