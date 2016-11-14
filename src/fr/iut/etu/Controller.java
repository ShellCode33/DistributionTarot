package fr.iut.etu;

import fr.iut.etu.model.*;
import fr.iut.etu.view.BoardView;
import fr.iut.etu.view.CardView;
import fr.iut.etu.view.DeckView;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Controller extends Application {

    private static final int PLAYER_COUNT = 4;

    public static double SCREEN_WIDTH;
    public static double SCREEN_HEIGHT;

    public static final int CARD_WIDTH = 120;
    public static final int CARD_HEIGHT = 212;
    public static final int CARD_THICK = 1;

    public static double SCALE_COEFF = 1;
    public static int Y_SCREEN_START = 0;

    int test = 0;

    private Board board;
    private BoardView boardView;

    private Menu menu;

    public Stage stage;
    private Scene sceneGame;

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
        primaryStage.setTitle("Sylvain DUPOUY - Clément FLEURY S3D");

        menu = new Menu(this);

        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); //Disable Esc to exit fullscreen
        primaryStage.setFullScreen(true);
        primaryStage.setScene(menu);
        primaryStage.show();
        primaryStage.setY(Y_SCREEN_START);
        primaryStage.setHeight(SCREEN_HEIGHT);
        primaryStage.setWidth(SCREEN_WIDTH);
    }

    public void startGame(String myPlayerUsername, Image selectedImage) {
        board = new Board(PLAYER_COUNT);

        board.addPlayer(new Player(myPlayerUsername, selectedImage));

        for(int i = 0; i < PLAYER_COUNT-1; i++)
            board.addPlayer(new Player());


        boardView = new BoardView(board);
        boardView.setScaleX(SCALE_COEFF);
        boardView.setScaleY(SCALE_COEFF);

        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setRotationAxis(Rotate.X_AXIS);
        camera.setRotate(15);

        sceneGame = new Scene(boardView, SCREEN_WIDTH, SCREEN_HEIGHT, true);
        sceneGame.setCamera(camera);
        boardView.setTranslateY(Y_SCREEN_START);
        sceneGame.setFill(Color.BLACK);

        stage.setScene(sceneGame);
        stage.setFullScreen(true);

        sceneGame.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    stage.setScene(menu);
                    stage.setFullScreen(true);
                    board = null;
                    boardView = null;
                    break;

                case A:
                    board.getPlayers().get(0).getCards().get(test++).show();
                    break;
            }
        });

        while(!deal());
    }

    private boolean deal(){
        final boolean[] isWellDealt = {true};
        Deck deck = board.getDeck();
        DeckView deckView = boardView.getDeckView();

        deck.refill();

        Task<Void> waitBringDeckAnimation = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                boardView.bringDeckOnBoardAnimation();
                Thread.sleep(3000);
                return null;
            }
        };

        waitBringDeckAnimation.setOnSucceeded(event -> {

            System.out.println("bring deck animation is over");

            Task<Void> waitCutAnimation = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    deckView.cutAnimation();
                    Thread.sleep(3000);
                    return null;
                }
            };

            waitCutAnimation.setOnSucceeded(workerStateEvent -> {
                System.out.println("cut animation is over");
                deck.shuffle();
                for(int i = 0; i < 6; i++){
                    for(Player p : board.getPlayers()){

                        deck.deal(p);
                        deck.deal(p);
                        deck.deal(p);

                    }

                    deck.deal(board.getDog());
                    ((CardView)boardView.getDogView().getChildren().get(boardView.getDogView().getChildren().size()-1)).setVertical(false);
                }

                for(Player p : board.getPlayers()){
                    ArrayList<Card> trumps = p.getCards().stream().filter(card -> card.getType() == Card.Type.TRUMP).collect(Collectors.toCollection(ArrayList::new));
                    boolean gotFool = p.getCards().contains(new Fool());

                    if(trumps.size() == 1 && trumps.get(0).getValue() == 1 && !gotFool){
                        reset();
                        isWellDealt[0] = false;
                    }
                }

                boardView.dealCardAnimation();

                Task<Void> waitDispatchAnimation = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        boardView.getDogView().dispatch();
                        Thread.sleep(1200);
                        return null;
                    }
                };

                waitDispatchAnimation.setOnSucceeded(workerStateEvent1 -> {
                    for(Card card : board.getDog().getCards()) {
                        card.show();
                    }
                });

                new Thread(waitDispatchAnimation).start();
            });

            new Thread(waitCutAnimation).start();
        });
        new Thread(waitBringDeckAnimation).start();

        return isWellDealt[0];

    }

    private void reset() {
        //TODO : repenser l'usage de cette fonction
        board = new Board(PLAYER_COUNT);
        boardView = new BoardView(board);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void askUsername() {

        try {
            stage.setScene(new UserInput(this));
            stage.setFullScreen(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}