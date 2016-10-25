package fr.iut.etu.view;

import fr.iut.etu.model.Player;
import javafx.scene.Group;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class PlayerView extends Group implements Observer {

    private Player player;

    public PlayerView(Player player) {
        this.player = player;
        this.player.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
