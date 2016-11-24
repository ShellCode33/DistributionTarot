package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Board;
import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class BoardView extends Group {

    private final ArrayList<PlayerView> playerViews = new ArrayList<>();
    private DeckView deckView;
    private DogView dogView;
    private Animation bringDeckOnBoardAnimation;

    public BoardView(Board board, Image backgroundCustom) {

        super();

        ImageView backgroundCustomView = new ImageView(backgroundCustom == null ? new Image("file:res/backgrounds/background_board0.jpg") : backgroundCustom);

        backgroundCustomView.setFitWidth(Controller.SCREEN_WIDTH);
        backgroundCustomView.setFitHeight(Controller.SCREEN_HEIGHT);
        getChildren().add(backgroundCustomView);

        for (int i = 0; i < board.getPlayerCount(); i++) {
            PlayerView playerView = new PlayerView(board.getPlayer(i));
            playerViews.add(playerView);
            getChildren().add(playerView);
        }


        deckView = new DeckView(board.getDeck());
        getChildren().add(deckView);
        dogView = new DogView(board.getDog());
        getChildren().add(dogView);

        placeHandViews();
        createBringDeckOnBoardAnimation();


    }

    private void placeHandViews(){

        dogView.getTransforms().addAll(
                new Translate(
                        4*Controller.SCREEN_WIDTH/6,
                        (Controller.SCREEN_HEIGHT-CardView.CARD_HEIGHT)/2,
                        -1),
                new Rotate(
                        0,
                        Rotate.Z_AXIS
                )
        );

        PlayerView playerView;

        playerView = getPlayerView(0);
        playerView.getTransforms().addAll(
                new Translate(
                    Controller.SCREEN_WIDTH/2,
                    Controller.SCREEN_HEIGHT - CardView.CARD_HEIGHT/2,
                    -1),
                new Rotate(
                        0,
                        Rotate.Z_AXIS
                )
        );

        playerView = getPlayerView(1);
        playerView.getTransforms().addAll(
                new Translate(CardView.CARD_HEIGHT,
                        Controller.SCREEN_HEIGHT/2,
                        -1),
                new Rotate(
                        90,
                        Rotate.Z_AXIS
                )
        );

        playerView = getPlayerView(2);
        playerView.getTransforms().addAll(
                new Translate(
                        Controller.SCREEN_WIDTH/2,
                        CardView.CARD_HEIGHT/2,
                        -1),
                new Rotate(
                        180,
                        Rotate.Z_AXIS
                )
        );

        playerView = getPlayerView(3);
        playerView.getTransforms().addAll(
                new Translate(
                        Controller.SCREEN_WIDTH - CardView.CARD_HEIGHT,
                        Controller.SCREEN_HEIGHT/2,
                        -1),
                new Rotate(
                        270,
                        Rotate.Z_AXIS
                )
        );

    }

    private void createBringDeckOnBoardAnimation() {
        RotateTransition rotate = new RotateTransition(Duration.seconds(2), deckView);
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.setFromAngle(0);
        rotate.setToAngle(270);
        rotate.setCycleCount(1);

        TranslateTransition translate = new TranslateTransition(Duration.seconds(2), deckView);
        translate.setToX((Controller.SCREEN_WIDTH-CardView.CARD_WIDTH)/2);
        translate.setToY((Controller.SCREEN_HEIGHT-CardView.CARD_HEIGHT)/2);
        translate.setCycleCount(1);

        ParallelTransition st = new ParallelTransition();
        st.getChildren().addAll(rotate, translate);

        bringDeckOnBoardAnimation = st;
    }

    public DeckView getDeckView() {
        return deckView;
    }

    public DogView getDogView() {
        return dogView;
    }

    public PlayerView getPlayerView(int i) {
        return playerViews.get(i);
    }

    public Animation getBringDeckOnBoardAnimation() {
        return bringDeckOnBoardAnimation;
    }

    public Animation getDealACardAnimation(HandView handView) {

        if(handView.getCardViewsWaitingToBeDealt().isEmpty())
            throw new UnsupportedOperationException("No card was dealt in the model");

        CardView cardView = handView.getCardViewsWaitingToBeDealt().poll();

        Rotate handViewRotate = (Rotate) handView.getTransforms().get(1);
        Bounds deckViewBoundsInHandView = handView.parentToLocal(deckView.getBoundsInParent());

        cardView.setRotationAxis(Rotate.Z_AXIS);
        cardView.setRotate(270 - handViewRotate.getAngle());

        Point3D destination = new Point3D(0,0,-handView.getCardViews().size()*CardView.CARD_THICK-1);

        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.4), cardView);
        translateTransition1.setFromX(deckViewBoundsInHandView.getMinX());
        translateTransition1.setFromY(deckViewBoundsInHandView.getMinY());
        translateTransition1.setFromZ(deckViewBoundsInHandView.getMinZ());
        translateTransition1.setToX(destination.getX());
        translateTransition1.setToY(destination.getY());
        translateTransition1.setToZ(deckViewBoundsInHandView.getMinZ());
        translateTransition1.setCycleCount(1);

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.4), cardView);
        rotateTransition.setAxis(Rotate.Z_AXIS);
        rotateTransition.setFromAngle(handViewRotate.getAngle() - 270);
        rotateTransition.setByAngle((handViewRotate.getAngle() - 270)%180);
        rotateTransition.setCycleCount(1);

        TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.2), cardView);
        translateTransition2.setFromZ(deckViewBoundsInHandView.getMinZ());
        translateTransition2.setToZ(destination.getZ());
        translateTransition2.setCycleCount(1);

        SequentialTransition sequentialTransition = new SequentialTransition();

        //Execution de cette transition au début de l'animation afin de réduire le deck avant le "vrai" début de l'animation
        Transition voidAnimation = new Transition() {@Override protected void interpolate(double frac) {}};
        voidAnimation.setOnFinished(event -> {
            handView.addCard(cardView);
            deckView.removeImageViewOnTop();
        });

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(voidAnimation, translateTransition1, rotateTransition);

        sequentialTransition.getChildren().addAll(parallelTransition, translateTransition2);

        return sequentialTransition;
    }
}
