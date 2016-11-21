package fr.iut.etu;

import fr.iut.etu.model.Board;
import fr.iut.etu.model.Card;
import fr.iut.etu.model.Player;
import fr.iut.etu.view.BoardView;
import fr.iut.etu.view.CardView;
import fr.iut.etu.view.HandView;
import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class Controller extends Application {

    public static final double CARD_THICK = 1.5;
    private static final int PLAYER_COUNT = 4;
    public static double SCREEN_WIDTH;
    public static double SCREEN_HEIGHT;
    public static int CARD_WIDTH = 120;
    public static int CARD_HEIGHT = 212;
    public static double SCALE_COEFF = 1;
    public static int Y_SCREEN_START = 0;

    int test = 0;

    private Board board;
    private BoardView boardView;
    private Image boardImage = null;
    private Image backCardImage = null;

    private Menu menu;

    public Stage stage;
    private Scene sceneGame;
    private MediaPlayer musicPlayer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        stage = primaryStage;

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        SCREEN_WIDTH = (int)bounds.getWidth();
        SCREEN_HEIGHT = (int)bounds.getHeight();

        //In order to fit all type of screens
        if(SCREEN_WIDTH / SCREEN_HEIGHT != 16.0/9.0) {
            System.out.println("Your display is not 16/9 : " + SCREEN_WIDTH + "x" + SCREEN_HEIGHT);
            SCREEN_HEIGHT = (int)(SCREEN_WIDTH * 9.0 / 16.0);
            Y_SCREEN_START = (int)((bounds.getHeight()-SCREEN_HEIGHT)/2);
            System.out.println("New screen height : " + SCREEN_HEIGHT);
            System.out.println("New Y start : " + Y_SCREEN_START);
        }

        else
            System.out.println("Your display is 16/9 : " + SCREEN_WIDTH + "x" + SCREEN_HEIGHT);

        SCALE_COEFF = SCREEN_WIDTH / 1920;
        CARD_WIDTH *= SCALE_COEFF;
        CARD_HEIGHT *= SCALE_COEFF;
        primaryStage.setTitle("Sylvain DUPOUY - Clément FLEURY S3D");

        menu = new Menu(this);

        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); //Disable Esc to exit fullscreen
        primaryStage.setFullScreen(true);
        primaryStage.setScene(menu);
        primaryStage.show();
        primaryStage.setY(Y_SCREEN_START);
        primaryStage.setHeight(SCREEN_HEIGHT);
        primaryStage.setWidth(SCREEN_WIDTH);

        Media music = new Media(new File("res/audio/main.mp3").toURI().toString());

        try {
            musicPlayer = new MediaPlayer(music);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            musicPlayer.setVolume(0.5);
            musicPlayer.play();
        }

        catch(MediaException e) {
            System.out.println("Your OS doesn't support music player : might be a javafx issue");
        }
    }

    public void startGame(String myPlayerUsername, Image selectedImage) {
        board = new Board(PLAYER_COUNT);

        board.addPlayer(new Player(myPlayerUsername));

        for(int i = 0; i < PLAYER_COUNT-1; i++)
            board.addPlayer(new Player());


        boardView = new BoardView(board, boardImage, backCardImage);
        boardView.setDepthTest(DepthTest.ENABLE);

        Image defaultImage = new Image("file:res/avatars/avatar_default.png");
        boardView.getPlayerView(0).setAvatar(selectedImage != null ? selectedImage : defaultImage);

        for(int i = 1; i < board.getPlayerCount(); i++)
            boardView.getPlayerView(i).setAvatar(defaultImage);

        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setRotationAxis(Rotate.X_AXIS);
        camera.setRotate(15);

        sceneGame = new Scene(boardView, SCREEN_WIDTH, SCREEN_HEIGHT, true, SceneAntialiasing.BALANCED);
        sceneGame.setCamera(camera);
        boardView.setTranslateY(Y_SCREEN_START);
        sceneGame.setFill(Color.BLACK);

        setScene(sceneGame);

        sceneGame.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    setScene(menu);
                    board = null;
                    boardView = null;
                    break;
            }
        });

        deal();
    }

    private void deal(){

        board.getDeck().refill();
        board.getDeck().shuffle();

        boardView.getBringDeckOnBoardAnimation().setOnFinished(event1 -> {

            //Cut animation even if we shuffled the deck, because the deck is initially sorted...
            Animation cutAnim = boardView.getDeckView().createCutAnimation();

            cutAnim.setOnFinished(actionEvent -> {
                //Z reset because the deal begins on the middle of the deck
                ObservableList<Node> children = boardView.getDeckView().getChildren();
                for(int i = 0; i < children.size(); i++)
                        children.get(i).setTranslateZ(-(i+1) * Controller.CARD_THICK);
                //--------------------------------------------------

                SequentialTransition st = new SequentialTransition();
                recursiveDealingSequence(0, st);

                st.setOnFinished(event2 -> {

                    SequentialTransition st2 = new SequentialTransition();

                    ParallelTransition pt = new ParallelTransition();
                    pt.getChildren().add(boardView.getDogView().getDispatchAnimation());

                    for(int i = 0; i < PLAYER_COUNT; i++)
                        pt.getChildren().add(boardView.getPlayerView(i).getDispatchAnimation());

                    st2.getChildren().add(pt);
                    st2.getChildren().add(boardView.getPlayerView(0).getFlipAllCardViewsAnimation());

                    boardView.getPlayerView(0).sort();
                    st2.getChildren().add(boardView.getPlayerView(0).getSortAnimation());

                    st2.setOnFinished(event3 ->{
                        boardView.askUserChoice(this);
                    });

                    st2.play();
                });

                st.play();
            });

            cutAnim.play();
        });

        boardView.getBringDeckOnBoardAnimation().play();
    }

    private void recursiveDealingSequence(int playerIndex, SequentialTransition st){

        Animation animation;

        if(playerIndex == -1) {
            board.getDeck().deal(board.getDog());
            animation = boardView.getDealACardAnimation(boardView.getDogView());
        }
        else{
            board.getDeck().deal(board.getPlayer(playerIndex));
            board.getDeck().deal(board.getPlayer(playerIndex));
            board.getDeck().deal(board.getPlayer(playerIndex));
            animation = new ParallelTransition();
            ((ParallelTransition) animation).getChildren().addAll(
                    boardView.getDealACardAnimation(boardView.getPlayerView(playerIndex)),
                    boardView.getDealACardAnimation(boardView.getPlayerView(playerIndex)),
                    boardView.getDealACardAnimation(boardView.getPlayerView(playerIndex))
            );
        }

        animation.setCycleCount(1);
        st.getChildren().add(animation);
        if(board.getDeck().size() > 0){

            int nextHand = playerIndex + 1;
            if(nextHand == PLAYER_COUNT)
                nextHand = -1;

            recursiveDealingSequence(nextHand, st);
        }
    }

    private void reset() {
        //TODO : repenser l'usage de cette fonction
        board = new Board(PLAYER_COUNT);
        boardView = new BoardView(board, boardImage, backCardImage);
    }

    public void setUserChoice(int user_index, Player.UserChoice userChoice) {
        board.getPlayer(user_index).setChoice(userChoice);

        if(userChoice == Player.UserChoice.KEEP || userChoice == Player.UserChoice.TAKE) {
            Animation animFlip = boardView.getDogView().getFlipAllCardViewsAnimation();
            animFlip.setOnFinished(actionEvent -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Transfert dog to player");
                board.getDog().transferCardsTo(board.getPlayer(0));
                Animation transferAnim  = boardView.getDogView().transferCardViewsTo(boardView.getPlayerView(0));

                transferAnim.setOnFinished(actionEvent12 -> {
                    boardView.getPlayerView(0).sort();
                    Animation sortAnim = boardView.getPlayerView(0).getSortAnimation();

                    sortAnim.setOnFinished(actionEvent1 -> {

                        for (CardView cardView : boardView.getPlayerView(0).getCardViews()) {
                            cardView.setOnMousePressed(mouseEvent -> {
                                System.out.println("Drag");
                                cardView.setMovement(true);
                            });

                            cardView.setOnMouseMoved(mouseEvent -> {
                                if(cardView.isMoving()) {
                                    System.out.println("moving card " + cardView);
                                    cardView.setLayoutX(mouseEvent.getX());
                                    cardView.setLayoutY(mouseEvent.getY());
                                }
                            });

                            cardView.setOnMouseReleased(mouseEvent -> {
                                System.out.println("Drop");
                                cardView.setMovement(false);
                            });
                        }

                    });

                    sortAnim.play();
                });

                transferAnim.play();
            });

            animFlip.play();
        }

        System.out.println("user choice set");
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
        stage.setFullScreen(true);

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    setScene(menu);
                    break;
            }
        });
    }

    public Menu getMenu() {
        return menu;
    }

    public void setBackCardImage(Image imageView) {
        backCardImage = imageView;
    }

    public void setBoardImage(Image imageView) {
        boardImage = imageView;
    }

    public MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }
}