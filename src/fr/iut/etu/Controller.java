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

    private void reset(){
        board = new Board(PLAYER_COUNT);
        boardView = new BoardView(board);
    }

    private boolean deal(){
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

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        SCREEN_WIDTH = bounds.getWidth();
        SCREEN_HEIGHT = bounds.getHeight();

        board = new Board(PLAYER_COUNT);
        boardView = new BoardView(board);

        primaryStage.setTitle("Sylvain DUPOUY - Clément FLEURY S3D");

        Scene scene = new Scene(boardView, SCREEN_WIDTH, SCREEN_HEIGHT);

        PerspectiveCamera camera = new PerspectiveCamera(false);
        scene.setCamera(camera); //3D

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        while (!deal());
    }


    public static void main(String[] args) {
        launch(args);
    }
}