package fr.iut.etu.view;

import fr.iut.etu.model.Deck;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class DeckView extends Box implements Observer {

    private Deck deck;

    public DeckView(Deck deck) {
        super(100, 200, deck.size());

        this.deck = deck;
        deck.addObserver(this);

        //Create the Material
        PhongMaterial mat = new PhongMaterial();
        Image image = new Image("file:./res/deck.jpg");
        mat.setDiffuseMap(image);
        mat.setSpecularColor(Color.BLACK);

        //Apply Material to the Box
        setMaterial(mat);

        Tooltip.install(this, new Tooltip(this.deck.size() + " cards !"));
    }

    @Override
    public void update(Observable observable, Object o) {
        setDepth(deck.size());
    }
}
