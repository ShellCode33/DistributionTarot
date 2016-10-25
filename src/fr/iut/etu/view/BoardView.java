package fr.iut.etu.view;

import fr.iut.etu.model.Board;
import javafx.scene.Group;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class BoardView extends Group {

    private Board board;

    public BoardView(Board board) {
        this.board = board;

        for (int i = 0; i < this.board.getPlayerCount(); i++) {
            getChildren().add(new PlayerView(this.board.getPlayer(i)));
        }

        getChildren().add(new DeckView(this.board.getDeck()));
    }
}
