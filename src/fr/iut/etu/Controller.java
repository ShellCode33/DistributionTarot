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

    private Board board = new Board(PLAYER_COUNT);
    private BoardView boardView = new BoardView(board);

    public static final int WIDTH_SCENE = 1280;
    public static final int HEIGHT_SCENE = 720;

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
        primaryStage.setFullScreen(true);

        Scene scene = new Scene(boardView, WIDTH_SCENE, HEIGHT_SCENE);

        PerspectiveCamera camera = new PerspectiveCamera(false);
        scene.setCamera(camera); //3D

        while(!deal());

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
