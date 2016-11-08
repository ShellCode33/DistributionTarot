package fr.iut.etu.view;

import fr.iut.etu.model.Card;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class CardView extends Box implements Observer {
    private Card card;

    public CardView(Card card) {

        super(100,200,1);

        setDrawMode(DrawMode.LINE);

    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
