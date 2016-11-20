package fr.iut.etu;

import fr.iut.etu.model.Board;
import fr.iut.etu.model.Player;
import fr.iut.etu.view.BoardView;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.DepthTest;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Controller extends Application {

    public static final int CARD_THICK = 1;
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
            System.out.println("Your OS doesn't support music player");
        }
    }

    public void startGame(String myPlayerUsername, Image selectedImage) {
        board = new Board(PLAYER_COUNT);

        board.addPlayer(new Player(myPlayerUsername));

        for(int i = 0; i < PLAYER_COUNT-1; i++)
            board.addPlayer(new Player());


        boardView = new BoardView(board, boardImage, backCardImage);
        boardView.setDepthTest(DepthTest.ENABLE);

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

    private boolean deal(){

        board.getDeck().refill();
        boardView.getBringDeckOnBoardAnimation().play();

        boardView.getBringDeckOnBoardAnimation().setOnFinished(event1 -> {

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

                st2.setOnFinished(event3 ->{
                    boardView.askUserChoice(this);
                });

                st2.play();
            });

            st.play();
        });

        return true;
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

    public void setUserChoice(Player.UserChoice userChoice) {
        board.getPlayer(0).setChoice(userChoice);

        if(userChoice == Player.UserChoice.KEEP || userChoice == Player.UserChoice.TAKE) {

            System.out.println("show dog");

            boardView.getDogView().getFlipAllCardViewsAnimation().play();

            //ecart
        }

        else {
            System.out.println("user choice set");
        }
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