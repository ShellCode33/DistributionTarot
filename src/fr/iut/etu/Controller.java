package fr.iut.etu;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import fr.iut.etu.layouts.Menu;
import fr.iut.etu.layouts.UserChoice;
import fr.iut.etu.model.*;
import fr.iut.etu.view.BoardView;
import fr.iut.etu.view.CardView;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

public class Controller extends Application {

    public static final int PLAYER_COUNT = 4;
    public static double SCREEN_WIDTH;
    public static double SCREEN_HEIGHT;

    public static double SCALE_COEFF = 1;
    public static int Y_SCREEN_START = 0;

    private Board board;
    private BoardView boardView;
    private Image boardImage = null;

    private Menu menu;

    private Stage stage;
    private MediaPlayer musicPlayer;

    private PerspectiveCamera camera;

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

        else {
            System.out.println("Your display is 16/9 : " + SCREEN_WIDTH + "x" + SCREEN_HEIGHT);
        }

        SCALE_COEFF = SCREEN_WIDTH / 1920;
        CardView.CARD_WIDTH *= SCALE_COEFF;
        CardView.CARD_HEIGHT *= SCALE_COEFF;
        CardView.CARD_THICK *= SCALE_COEFF;
        primaryStage.setTitle("Sylvain DUPOUY - Clément FLEURY S3D");

        menu = new Menu(this);

        Scene scene = new Scene(menu, SCREEN_WIDTH, SCREEN_HEIGHT, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);

        camera = new PerspectiveCamera(false);
        camera.setRotationAxis(Rotate.X_AXIS);
        scene.setCamera(camera);

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    camera.setRotate(0);
                    setLayout(menu);
                    board = null;
                    boardView = null;
                    break;
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); //Disable Esc to exit fullscreen
        primaryStage.setFullScreen(true);
        primaryStage.setY(Y_SCREEN_START);
        primaryStage.setHeight(SCREEN_HEIGHT);
        primaryStage.setWidth(SCREEN_WIDTH);
        primaryStage.show();


        Media music = new Media(new File("res/audio/main.wav").toURI().toString());

        try {
            musicPlayer = new MediaPlayer(music);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            musicPlayer.setVolume(0.5);
            musicPlayer.play();
        }

        catch(MediaException e) {
            System.out.println("Your OS doesn't support music player : might be a javafx issue");
        }

        CardView.backCard = new Image("file:./res/cards/back0.jpg");
    }

    public void startGame(String myPlayerUsername, Image selectedImage) {
        board = new Board(PLAYER_COUNT);

        board.getPlayer(0).setName(myPlayerUsername);

        for(int i = 1; i < PLAYER_COUNT; i++)
            board.getPlayer(i).setName("#computer"+i);

        boardView = new BoardView(board, boardImage);
        boardView.setDepthTest(DepthTest.ENABLE);

        Image defaultImage = new Image("file:res/avatars/avatar_default.png");
        boardView.getPlayerView(0).setAvatar(selectedImage != null ? selectedImage : defaultImage);

        for(int i = 1; i < board.getPlayerCount(); i++)
            boardView.getPlayerView(i).setAvatar(defaultImage);

        camera.setRotate(15);
        setLayout(boardView);

        boardView.setTranslateY(Y_SCREEN_START);

        deal();
    }

    private void deal(){

        board.getDeck().refill();
        board.getDeck().shuffle();

        Animation bringDeckOnBoardAnimation = boardView.getBringDeckOnBoardAnimation();
        Animation cutAnim = boardView.getDeckView().getCutAnimation();
        ParallelTransition dealingAnimation = new ParallelTransition();
        ParallelTransition dispatchAllCardsAnimation = new ParallelTransition();

        bringDeckOnBoardAnimation.setOnFinished(event -> {
            board.getDeck().cut((int) (Math.random()*(board.getDeck().size()-8) + 4));
            cutAnim.play();
        });

        cutAnim.setOnFinished(event -> {
            recursiveDealingSequence(0, Duration.ZERO, dealingAnimation);
            dealingAnimation.play();
        });

        dealingAnimation.setOnFinished(event -> {
            dispatchAllCardsAnimation.getChildren().add(boardView.getDogView().getDispatchAnimation());

            for(int i = 0; i < PLAYER_COUNT; i++)
                dispatchAllCardsAnimation.getChildren().add(boardView.getPlayerView(i).getDispatchAnimation());

            dispatchAllCardsAnimation.play();
        });

        dispatchAllCardsAnimation.setOnFinished(event -> {
            Animation flipAllCardViewsAnimation = boardView.getPlayerView(0).getFlipAllCardViewsAnimation();
            flipAllCardViewsAnimation.setOnFinished(event1 -> askUserChoice());
            flipAllCardViewsAnimation.play();
        });

        bringDeckOnBoardAnimation.play();
    }

    private void recursiveDealingSequence(int playerIndex, Duration delay, ParallelTransition st){

        if(board.getDeck().size() <= 0)
            return;

        ParallelTransition animation = new ParallelTransition();
        animation.setCycleCount(1);
        animation.setDelay(delay);

        board.getDeck().deal(board.getPlayer(playerIndex));
        animation.getChildren().add(boardView.getDealACardAnimation(boardView.getPlayerView(playerIndex)));

        int nextHand = playerIndex;
        int i = 0;
        if(board.getPlayer(playerIndex).getCardCount()%3 == 0) {
            nextHand = (playerIndex + 1) % PLAYER_COUNT;

            while(board.getDog().getCardCount() < 6
                    && board.getDeck().size() > 0
                    && (Math.random() < 0.3 || board.getDeck().size() - 3 == 6 - board.getDog().getCardCount())){

                board.getDeck().deal(board.getDog());
                Animation tmp = boardView.getDealACardAnimation(boardView.getDogView());
                tmp.setDelay(Duration.millis(i*170));
                animation.getChildren().add(tmp);

                i++;
            }
        }

        Duration nextDelay = Duration.millis(delay.toMillis() + (i+1)*170);

        st.getChildren().add(animation);
        recursiveDealingSequence(nextHand, nextDelay,st);
    }

    private void reset() {
        //TODO : repenser l'usage de cette fonction
        board = new Board(PLAYER_COUNT);
        boardView = new BoardView(board, boardImage);
    }
    public void processUserChoice(Player.UserChoice userChoice) {
        boardView.getChildren().remove(boardView.getChildren().size()-1); //Remove UserChoice GUI
        board.getPlayer(0).setChoice(userChoice);

        if(userChoice == Player.UserChoice.KEEP || userChoice == Player.UserChoice.TAKE) {
            Animation animFlip = boardView.getDogView().getFlipAllCardViewsAnimation();
            animFlip.setOnFinished(actionEvent -> {
                System.out.println("Transfert dog to player");
                board.getDog().transferCardsTo(board.getPlayer(0));
                Animation transferAnim  = boardView.getDogView().transferCardViewsTo(boardView.getPlayerView(0));
                transferAnim.setDelay(Duration.seconds(1.5));

                transferAnim.setOnFinished(actionEvent12 -> {
                    boardView.getPlayerView(0).sort();
                    Animation sortAnim = boardView.getPlayerView(0).getSortAnimation();

                    sortAnim.setOnFinished(actionEvent1 -> {
                        boardView.handleGap();
                    });

                    sortAnim.play();
                });

                transferAnim.play();
            });

            animFlip.play();
        }

        else {

            Animation explodeAnimation = boardView.getDogView().createExplodeAnimation();

            explodeAnimation.setOnFinished(actionEvent -> {
                for(Card card : board.getDog().getCards())
                    board.getDog().removeCard(card);
            });

            explodeAnimation.play();
        }

        System.out.println("user choice set");
    }

    public void setLayout(Parent root) {
        stage.getScene().setRoot(root);
    }

    public Menu getMenu() {
        return menu;
    }

    public void setBackCardImage(Image image) {
        CardView.backCard = image;
    }

    public void setBoardImage(Image imageView) {
        boardImage = imageView;
    }

    public MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public void askUserChoice() {
        System.out.println("Asking user...");
        UserChoice userChoice = new UserChoice(this);
        boardView.getChildren().add(userChoice);
    }


}