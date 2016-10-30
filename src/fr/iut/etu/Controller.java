package fr.iut.etu;

import fr.iut.etu.model.Board;
import fr.iut.etu.view.BoardView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller extends Application {

    private static final int PLAYER_COUNT = 4;

    private Board board = new Board(PLAYER_COUNT);
    private BoardView boardView;

    @Override
    public void start(Stage primaryStage) throws Exception{


        primaryStage.setTitle("Sylvain DUPOUY - Clément FLEURY S3D");
        primaryStage.setFullScreen(true);

        boardView = new BoardView(board, primaryStage.getWidth(), primaryStage.getHeight());
        Scene scene = new Scene(boardView, primaryStage.getWidth(), primaryStage.getHeight());

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
