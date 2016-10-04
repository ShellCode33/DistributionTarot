package fr.iut.etu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    //Board boardModel;
    //BoardController boardController;

    @Override
    public void start(Stage primaryStage) throws Exception{

        //boardModel = new Board();
        //boardController = new BoardController(boardModel);

        Parent root = FXMLLoader.load(getClass().getResource("board.fxml"));
        primaryStage.setTitle("Tarot");
        primaryStage.setFullScreen(true);
        primaryStage.setScene(new Scene(root, 1280, 720)); //HD 720p
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
