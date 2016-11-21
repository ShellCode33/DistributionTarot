package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Board;
import fr.iut.etu.model.Player;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class BoardView extends Group {

    private Board board;

    private ArrayList<PlayerView> playerViews = new ArrayList<>();
    private DeckView deckView;
    private DogView dogView;
    private Animation bringDeckOnBoardAnimation;

    public BoardView(Board board, Image backgroundCustom, Image backCardCustom) {
        super();
        this.board = board;


        ImageView backgroundCustomView = new ImageView(backgroundCustom == null ? new Image("file:res/backgrounds/background_board0.jpg") : backgroundCustom);

        backgroundCustomView.setFitWidth(Controller.SCREEN_WIDTH);
        backgroundCustomView.setFitHeight(Controller.SCREEN_HEIGHT);
        getChildren().add(backgroundCustomView);

        for (int i = 0; i < board.getPlayerCount(); i++) {
            PlayerView playerView = new PlayerView(this.board.getPlayer(i));
            playerViews.add(playerView);
            getChildren().add(playerView);
        }


        deckView = new DeckView(board.getDeck(), backCardCustom);
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
                        (Controller.SCREEN_HEIGHT-Controller.CARD_HEIGHT)/2,
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
                    Controller.SCREEN_HEIGHT - Controller.CARD_HEIGHT/2,
                    -1),
                new Rotate(
                        0,
                        Rotate.Z_AXIS
                )
        );

        playerView = getPlayerView(1);
        playerView.getTransforms().addAll(
                new Translate(
                        Controller.CARD_HEIGHT,
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
                        Controller.CARD_HEIGHT/2,
                        -1),
                new Rotate(
                        180,
                        Rotate.Z_AXIS
                )
        );

        playerView = getPlayerView(3);
        playerView.getTransforms().addAll(
                new Translate(
                        Controller.SCREEN_WIDTH - Controller.CARD_HEIGHT,
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
        translate.setToX((Controller.SCREEN_WIDTH-Controller.CARD_WIDTH)/2);
        translate.setToY((Controller.SCREEN_HEIGHT-Controller.CARD_HEIGHT)/2);
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

        if(deckView.getCardViewsWaitingToBeDealt().isEmpty())
            throw new UnsupportedOperationException("No card was dealt in the model");

        CardView cardView = deckView.getCardViewsWaitingToBeDealt().poll();
        handView.add(cardView);

        Rotate handViewRotate = (Rotate) handView.getTransforms().get(1);
        Bounds deckViewBoundsInHandView = handView.parentToLocal(deckView.getBoundsInParent());

        cardView.setTranslateX(-100000);
        cardView.setRotationAxis(Rotate.Z_AXIS);
        cardView.setRotate(270 - handViewRotate.getAngle());

        Point3D destination = new Point3D(0,0,-handView.getCardViews().size()*Controller.CARD_THICK-1);

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.7), cardView);
        translateTransition.setFromX(deckViewBoundsInHandView.getMinX());
        translateTransition.setFromY(deckViewBoundsInHandView.getMinY());
        translateTransition.setFromZ(deckViewBoundsInHandView.getMinZ());
        translateTransition.setToX(destination.getX());
        translateTransition.setToY(destination.getY());
        translateTransition.setToZ(destination.getZ());
        translateTransition.setCycleCount(1);

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.7), cardView);
        rotateTransition.setAxis(Rotate.Z_AXIS);
        rotateTransition.setFromAngle(270 - handViewRotate.getAngle());
        rotateTransition.setByAngle(270 - handViewRotate.getAngle());
        rotateTransition.setCycleCount(1);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(translateTransition, rotateTransition);

        parallelTransition.setOnFinished(event -> deckView.getChildren().remove(deckView.getChildren().size() - 1));

        return parallelTransition;
    }

    public void askUserChoice(Controller controller) {

        URL res = controller.getClass().getResource("user_choice.fxml");

        System.out.println("Asking user...");

        VBox vbox = null;

        try {
            vbox = FXMLLoader.load(res);
            vbox.setTranslateZ(-100);
            vbox.setPrefWidth(Controller.SCREEN_WIDTH / 2);
            vbox.setMaxWidth(Controller.SCREEN_WIDTH / 2);
            vbox.setPrefHeight(Controller.SCREEN_HEIGHT / 2);
            vbox.setMaxHeight(Controller.SCREEN_HEIGHT / 2);
            vbox.setTranslateX(Controller.SCREEN_WIDTH / 4);
            vbox.setTranslateY(Controller.SCREEN_HEIGHT / 4);

            double buttonWidth = Controller.SCREEN_WIDTH / 5;
            double buttonHeight = Controller.SCREEN_HEIGHT / 12;

            Button button1 = (Button)vbox.getChildren().get(1);
            Button button2 = (Button)vbox.getChildren().get(2);
            Button button3 = (Button)vbox.getChildren().get(3);
            Button button4 = (Button)vbox.getChildren().get(4);

            button1.setPrefWidth(buttonWidth);
            button1.setPrefHeight(buttonHeight);
            button1.setMaxWidth(buttonWidth);
            button1.setMaxHeight(buttonHeight);

            button2.setPrefWidth(buttonWidth);
            button2.setPrefHeight(buttonHeight);
            button2.setMaxWidth(buttonWidth);
            button2.setMaxHeight(buttonHeight);

            button3.setPrefWidth(buttonWidth);
            button3.setPrefHeight(buttonHeight);
            button3.setMaxWidth(buttonWidth);
            button3.setMaxHeight(buttonHeight);

            button4.setPrefWidth(buttonWidth);
            button4.setPrefHeight(buttonHeight);
            button4.setMaxWidth(buttonWidth);
            button4.setMaxHeight(buttonHeight);

            Button finalButton = button1;
            Button finalButton1 = button2;
            Button finalButton2 = button3;
            Button finalButton3 = button4;
            button1.setOnMouseClicked(mouseEvent -> buttonClicked(finalButton, controller));
            button2.setOnMouseClicked(mouseEvent -> buttonClicked(finalButton1, controller));
            button3.setOnMouseClicked(mouseEvent -> buttonClicked(finalButton2, controller));
            button4.setOnMouseClicked(mouseEvent -> buttonClicked(finalButton3, controller));

        } catch (IOException e) {
            e.printStackTrace();
        }

        VBox finalBox = vbox;
        Platform.runLater(() -> getChildren().add(finalBox));
    }

    public void buttonClicked(Button button, Controller controller) {

        Player.UserChoice choice = null;
        String id = button.getId();

        if(id.equals("button1"))
            choice = Player.UserChoice.TAKE;
        else if(id.equals("button2"))
            choice = Player.UserChoice.KEEP;
        else if(id.equals("button3"))
            choice = Player.UserChoice.KEEP_WITHOUT_DOG;
        else if(id.equals("button4"))
            choice = Player.UserChoice.KEEP_AGAINST_DOG;

        System.out.println("User choose: " + choice.toString());
        controller.setUserChoice(choice);

        getChildren().remove(getChildren().size()-1);
    }
}
