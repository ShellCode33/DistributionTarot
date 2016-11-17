package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Board;
import fr.iut.etu.model.Player;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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

        playerViews.get(0).getTransforms().add(new Translate((Controller.SCREEN_WIDTH)/2 - point2D.getX(),
                Controller.SCREEN_HEIGHT - playerViews.get(0).getLayoutBounds().getHeight()*2));
        playerViews.get(3).getTransforms().add(new Rotate(0));

        boundsInLocal = playerViews.get(1).getBoundsInLocal();
        point2D = playerViews.get(1).localToParent(boundsInLocal.getWidth() / 2, boundsInLocal.getHeight() / 2);

        playerViews.get(1).getTransforms().add(new Translate(- point2D.getX() + Controller.CARD_HEIGHT,
                Controller.SCREEN_HEIGHT/2 - point2D.getY()));
        playerViews.get(1).getTransforms().add(new Rotate(90));

        boundsInLocal = playerViews.get(2).getBoundsInLocal();
        point2D = playerViews.get(2).localToParent(boundsInLocal.getWidth() / 2, boundsInLocal.getHeight() / 2);

        playerViews.get(2).getTransforms().add(new Translate(Controller.SCREEN_WIDTH/2 - point2D.getX(),
                - point2D.getY()+Controller.CARD_HEIGHT/2));
        playerViews.get(2).getTransforms().add(new Rotate(180));

        boundsInLocal = playerViews.get(3).getBoundsInLocal();
        point2D = playerViews.get(3).localToParent(boundsInLocal.getWidth() / 2, boundsInLocal.getHeight() / 2);

        playerViews.get(3).getTransforms().add(new Translate(Controller.SCREEN_WIDTH - point2D.getX() - Controller.CARD_HEIGHT,
                Controller.SCREEN_HEIGHT/2 - point2D.getY()));
        playerViews.get(3).getTransforms().add(new Rotate(270));

        deckView = new DeckView(board.getDeck());
        getChildren().add(deckView);
        dogView = new DogView(board.getDog());
        dogView.setTranslateX(4*Controller.SCREEN_WIDTH/6);
        dogView.setTranslateY((Controller.SCREEN_HEIGHT-Controller.CARD_HEIGHT)/2);
        getChildren().add(dogView);

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

    public DogView getDogView() {
        return dogView;
    }

    public PlayerView getPlayerView(int i) {
        return playerViews.get(i);
    }

    public void askUserChoice(URL res, Controller controller) {

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
