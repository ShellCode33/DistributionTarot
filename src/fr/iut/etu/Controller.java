package fr.iut.etu;

import fr.iut.etu.model.Board;
import fr.iut.etu.view.BoardView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller extends Application {

    private static final int PLAYER_COUNT = 4;

    private Board board = new Board(PLAYER_COUNT);
    private BoardView boardView = new BoardView(board);

    @Override
    public void start(Stage primaryStage) throws Exception{

        Scene scene = new Scene(boardView, 1280, 720);

        primaryStage.setTitle("Sylvain DUPOUY - Clément FLEURY S3D");
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
