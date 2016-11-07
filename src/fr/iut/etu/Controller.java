package fr.iut.etu;

import fr.iut.etu.model.Board;
import fr.iut.etu.model.Card;
import fr.iut.etu.model.Player;
import fr.iut.etu.model.Trump;
import fr.iut.etu.view.BoardView;
import javafx.application.Application;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Controller extends Application {

    private static final int PLAYER_COUNT = 4;

    public static final int SCENE_WIDTH = 1280;
    public static final int SCENE_HEIGHT = 720;

    public static final int CARD_WIDTH = 120;
    public static final int CARD_HEIGHT = 212;
    public static final int CARD_DEPTH = 1;

    private Board board = new Board(PLAYER_COUNT);
    private BoardView boardView = new BoardView(board);

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


        primaryStage.setTitle("Sylvain DUPOUY - Clément FLEURY S3D");

        Scene scene = new Scene(boardView, SCENE_WIDTH, SCENE_HEIGHT);

        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setFieldOfView(45);


        primaryStage.setFullScreen(true);
        scene.setCamera(camera); //3D


        primaryStage.setScene(scene);
        primaryStage.show();

        while (!deal());
    }


    public static void main(String[] args) {
        launch(args);
    }
}