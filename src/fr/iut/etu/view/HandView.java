package fr.iut.etu.view;

import fr.iut.etu.model.Card;
import fr.iut.etu.model.Hand;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 11/18/16.
 */
public abstract class HandView extends ArrayList<CardView> implements Observer {
    private Hand hand;
    protected Group elementsToDraw = new Group();

    public HandView(Hand hand) {
        super();

        this.hand = hand;
        this.hand.addObserver(this);

    }

    public Animation getFlipAllCardViewsAnimation() {
        ParallelTransition pt = new ParallelTransition();

        for (CardView cardView : this) {
            pt.getChildren().add(cardView.getFlipAnimation());
        }

        return pt;
    }

    public abstract Animation getDispatchAnimation();

    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public boolean add(CardView cardView) {
        boolean ret = super.add(cardView);

        if(ret)
            elementsToDraw.getChildren().add(cardView);

        return ret;
    }

    public Group getElementsToDraw() {
        return elementsToDraw;
    }
}
