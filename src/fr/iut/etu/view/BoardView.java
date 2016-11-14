package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Board;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class BoardView extends Group {

    private Board board;

    private ArrayList<PlayerView> playerViews = new ArrayList<>();
    private DeckView deckView;

    public BoardView(Board board) {
        super();

        this.board = board;

        ImageView backgroundView = new ImageView(new Image("file:res/background_board.jpg"));
        backgroundView.setFitWidth(Controller.SCREEN_WIDTH);
        backgroundView.setFitHeight(Controller.SCREEN_HEIGHT);
        getChildren().add(backgroundView);

        for (int i = 0; i < board.getPlayerCount(); i++) {
            PlayerView playerView = new PlayerView(this.board.getPlayer(i));
            playerViews.add(playerView);
            getChildren().add(playerView);
        }

        Bounds boundsInLocal = playerViews.get(0).getBoundsInLocal();
        Point2D point2D = playerViews.get(0).localToParent(boundsInLocal.getWidth() / 2, boundsInLocal.getHeight() / 2);

        playerViews.get(0).setTranslateX(Controller.SCREEN_WIDTH/2 - point2D.getX());
        playerViews.get(0).setTranslateY(Controller.SCREEN_HEIGHT - point2D.getY());

        boundsInLocal = playerViews.get(1).getBoundsInLocal();
        point2D = playerViews.get(1).localToParent(boundsInLocal.getWidth() / 2, boundsInLocal.getHeight() / 2);

        playerViews.get(1).setTranslateX(- point2D.getX() + Controller.CARD_HEIGHT);
        playerViews.get(1).setTranslateY(Controller.SCREEN_HEIGHT/2 - point2D.getY());
        playerViews.get(1).getTransforms().add(new Rotate(90, boundsInLocal.getWidth() / 2, boundsInLocal.getHeight() / 2));

        boundsInLocal = playerViews.get(2).getBoundsInLocal();
        point2D = playerViews.get(2).localToParent(boundsInLocal.getWidth() / 2, boundsInLocal.getHeight() / 2);

        playerViews.get(2).setTranslateX(Controller.SCREEN_WIDTH/2 - point2D.getX());
        playerViews.get(2).setTranslateY(- point2D.getY()+Controller.CARD_HEIGHT/2);
        playerViews.get(2).getTransforms().add(new Rotate(180, boundsInLocal.getWidth() / 2, boundsInLocal.getHeight() / 2));

        boundsInLocal = playerViews.get(3).getBoundsInLocal();
        point2D = playerViews.get(3).localToParent(boundsInLocal.getWidth() / 2, boundsInLocal.getHeight() / 2);

        playerViews.get(3).setTranslateX(Controller.SCREEN_WIDTH - point2D.getX() - Controller.CARD_HEIGHT);
        playerViews.get(3).setTranslateY(Controller.SCREEN_HEIGHT/2 - point2D.getY());
        playerViews.get(3).getTransforms().add(new Rotate(270, boundsInLocal.getWidth() / 2, boundsInLocal.getHeight() / 2));

        this.deckView = new DeckView(board.getDeck());
        getChildren().add(this.deckView);

    }

    public void bringDeckOnBoardAnimation(){

        RotateTransition rotate = new RotateTransition(Duration.seconds(3), deckView);

        rotate.setAxis(Rotate.Z_AXIS);
        rotate.setFromAngle(0);
        rotate.setToAngle(270);
        rotate.setCycleCount(1);
        rotate.play();

        TranslateTransition translate = new TranslateTransition(Duration.seconds(3), deckView);
        translate.setToX((Controller.SCREEN_WIDTH-Controller.CARD_WIDTH)/2);
        translate.setToY((Controller.SCREEN_HEIGHT-Controller.CARD_HEIGHT)/2);

        translate.setCycleCount(1);
        translate.play();

    }

    public void dealCardAnimation() {

    }

    public DeckView getDeckView() {
        return deckView;
    }
}
