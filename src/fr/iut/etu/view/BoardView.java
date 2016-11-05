package fr.iut.etu.view;

import fr.iut.etu.model.Board;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

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


        DeckView deckView = new DeckView(board.getDeck());
        getChildren().add(deckView);

        deckView.setTranslateX(200);
        deckView.setTranslateY(200);

        deckView.setRotationAxis(new Point3D(0, 0, 1));

        Rotate rotate = new Rotate(0,0,0,0);
        deckView.getTransforms().add(rotate);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rotate.angleProperty(), 0)), // initial rotate
                new KeyFrame(Duration.seconds(4), new KeyValue(rotate.angleProperty(), 360)) // end value of rotate
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

//
//        Point2D centerPoint = deckView.localToParent(deckViewLocalBounds.getWidth() / 2, deckViewLocalBounds.getHeight() / 2);
//        double x = centerPoint.getX();
//        double y = centerPoint.getY();
//
//        Translate translate = new Translate();
//        deckView.getTransforms().add(translate);
//
//        Timeline timeline2 = new Timeline(
//                new KeyFrame(Duration.ZERO, new KeyValue(translate.xProperty(), 0)), // initial rotate
//                new KeyFrame(Duration.ZERO, new KeyValue(translate.yProperty(), 0)), // initial rotate
//                new KeyFrame(Duration.seconds(3), new KeyValue(translate.xProperty(), boardWidth - x)), // initial rotate
//                new KeyFrame(Duration.seconds(3), new KeyValue(translate.yProperty(), boardHeight - y)) // initial rotate
//        );
//
//        timeline2.play();

    }
}
