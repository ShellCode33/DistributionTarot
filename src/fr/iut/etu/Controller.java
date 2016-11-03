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

    public static final int WIDTH_SCENE = 1280;
    public static final int HEIGHT_SCENE = 720;

    @Override
    public void start(Stage primaryStage) throws Exception{


        primaryStage.setTitle("Sylvain DUPOUY - Clément FLEURY S3D");
        primaryStage.setFullScreen(true);

        boardView = new BoardView(board, WIDTH_SCENE, HEIGHT_SCENE);
        Scene scene = new Scene(boardView, WIDTH_SCENE, HEIGHT_SCENE);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
