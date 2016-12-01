package fr.iut.etu;

import fr.iut.etu.layouts.Menu;
import fr.iut.etu.layouts.UserChoice;
import fr.iut.etu.model.Board;
import fr.iut.etu.model.Card;
import fr.iut.etu.model.Player;
import fr.iut.etu.view.BoardView;
import fr.iut.etu.view.CardView;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

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

        SequentialTransition sequentialTransition = new SequentialTransition();

        board.getDeck().refill();
        board.getDeck().shuffle();

        sequentialTransition.getChildren().add(boardView.getBringDeckOnBoardAnimation());

        int currentPlayerIndex = 0;
        ParallelTransition dealSequence = new ParallelTransition();

        int cardDealtCount = 0;
        while(board.getDeck().size() > 0){

            board.getDeck().deal(board.getPlayer(currentPlayerIndex));
            Animation animation = boardView.getDealACardAnimation(boardView.getPlayerView(currentPlayerIndex));
            animation.setDelay(Duration.millis(cardDealtCount*150));
            dealSequence.getChildren().add(animation);
            cardDealtCount++;

            if(board.getPlayer(currentPlayerIndex).getCardCount()%3 == 0) {
                currentPlayerIndex = (currentPlayerIndex+ 1) % PLAYER_COUNT;

                while(board.getDog().getCardCount() < 6
                        && board.getDeck().size() > 0
                        && (Math.random() < 0.3 || board.getDeck().size() - 3 == 6 - board.getDog().getCardCount())){

                    board.getDeck().deal(board.getDog());
                    animation = boardView.getDealACardAnimation(boardView.getDogView());
                    animation.setDelay(Duration.millis(cardDealtCount*150));
                    dealSequence.getChildren().add(animation);
                    cardDealtCount++;
                }
            }
        }
        sequentialTransition.getChildren().add(dealSequence);

        sequentialTransition.setOnFinished(event -> {

            ParallelTransition parallelTransition = new ParallelTransition();

            parallelTransition.getChildren().add(boardView.getDogView().getDispatchAnimation());
            for(int i = 0; i < PLAYER_COUNT; i++)
                parallelTransition.getChildren().add(boardView.getPlayerView(i).getDispatchAnimation());

            parallelTransition.setOnFinished(event1 -> {
                Animation flipAllCardViewsAnimation = boardView.getPlayerView(0).getFlipAllCardViewsAnimation();

                flipAllCardViewsAnimation.setOnFinished(event2 -> askUserChoice());
                flipAllCardViewsAnimation.play();
            });
            parallelTransition.play();
        });

        sequentialTransition.play();


//        Animation bringDeckOnBoardAnimation = boardView.getBringDeckOnBoardAnimation();
//        Animation cutAnim = boardView.getDeckView().getCutAnimation();
//        ParallelTransition dealingAnimation = new ParallelTransition();
//        ParallelTransition dispatchAllCardsAnimation = new ParallelTransition();
//
//        bringDeckOnBoardAnimation.setOnFinished(event -> {
//            board.getDeck().cut((int) (Math.random()*(board.getDeck().size()-8) + 4));
//            cutAnim.play();
//        });
//
//        cutAnim.setOnFinished(event -> {
//            recursiveDealingSequence(0, Duration.ZERO, dealingAnimation);
//            dealingAnimation.play();
//        });
//
//        dealingAnimation.setOnFinished(event -> {
//            dispatchAllCardsAnimation.getChildren().add(boardView.getDogView().getDispatchAnimation());
//
//            for(int i = 0; i < PLAYER_COUNT; i++)
//                dispatchAllCardsAnimation.getChildren().add(boardView.getPlayerView(i).getDispatchAnimation());
//
//            dispatchAllCardsAnimation.play();
//        });
//
//        dispatchAllCardsAnimation.setOnFinished(event -> {
//            Animation flipAllCardViewsAnimation = boardView.getPlayerView(0).getFlipAllCardViewsAnimation();
//            flipAllCardViewsAnimation.setOnFinished(event1 -> askUserChoice());
//            flipAllCardViewsAnimation.play();
//        });
//
//        bringDeckOnBoardAnimation.play();
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