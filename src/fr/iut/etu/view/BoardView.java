package fr.iut.etu.view;

import fr.iut.etu.model.Board;
import javafx.scene.Group;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class BoardView extends Group {

    private Board board;

    public BoardView(Board board) {
        super();

        this.board = board;

        for (int i = 0; i < this.board.getPlayerCount(); i++) {
            getChildren().add(new PlayerView(this.board.getPlayer(i)));
        }

        DeckView deckView = new DeckView(this.board.getDeck());
        deckView.setRotate(90);

        getChildren().add(deckView);
    }
}
