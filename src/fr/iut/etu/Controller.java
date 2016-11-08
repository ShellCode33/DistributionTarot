package fr.iut.etu;

import fr.iut.etu.model.Board;
import fr.iut.etu.model.Card;
import fr.iut.etu.model.Player;
import fr.iut.etu.model.Trump;
import fr.iut.etu.view.BoardView;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Controller extends Application {

    private static final int PLAYER_COUNT = 4;

    public static double SCREEN_WIDTH;
    public static double SCREEN_HEIGHT;

    public static final int CARD_WIDTH = 120;
    public static final int CARD_HEIGHT = 212;
    public static final int CARD_DEPTH = 1;

    private Board board;
    private BoardView boardView;

    private Menu menu;

    public Stage stage;
    private Scene sceneGame;

    private boolean deal() throws InterruptedException {
        board.getDeck().refill();
        board.getDeck().shuffle();

        for(int i = 0; i < 6; i++){
            for(Player p : board.getPlayers()){

                board.getDeck().deal(p);
                board.getDeck().deal(p);
                board.getDeck().deal(p);

            }

            board.getDeck().deal(board.getDog());
        }

        for(Player p : board.getPlayers()){
            ArrayList<Card> trumps = new ArrayList<Card>();

            for (Card card : p.getCards()) {
                if(card.getType() == Card.Type.TRUMP)
                    trumps.add(card);
            }

            if(trumps.size() == 1 && trumps.get(0) == new Trump(1)){
                reset();
                return false;
            }
        }

        return true;

    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        stage = primaryStage;

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        SCREEN_WIDTH = bounds.getWidth();
        SCREEN_HEIGHT = bounds.getHeight();

        primaryStage.setTitle("Sylvain DUPOUY - Clément FLEURY S3D");

        menu = new Menu(this);

        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); //Disable Esc to exit fullscreen
        primaryStage.setFullScreen(true);
        primaryStage.setScene(menu);
        primaryStage.show();
    }

    public void startGame() {
        board = new Board(PLAYER_COUNT);
        boardView = new BoardView(board);

        sceneGame = new Scene(boardView, SCREEN_WIDTH, SCREEN_HEIGHT);

        PerspectiveCamera camera = new PerspectiveCamera(false);
        sceneGame.setCamera(camera); //3D

        stage.setScene(sceneGame);
        stage.setFullScreen(true);

        sceneGame.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    stage.setScene(menu);
                    stage.setFullScreen(true);
                    break;
            }
        });

        //    while (!deal());

    }

    private void reset() {
        board = new Board(PLAYER_COUNT);
        boardView = new BoardView(board);
    }

    public static void main(String[] args) {
        launch(args);
    }
}