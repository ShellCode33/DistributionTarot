package fr.iut.etu;

import fr.iut.etu.model.Board;
import fr.iut.etu.model.Player;
import fr.iut.etu.view.BoardView;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.DepthTest;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller extends Application {

    public static final int CARD_THICK = 1;
    private static final int PLAYER_COUNT = 4;
    public static double SCREEN_WIDTH;
    public static double SCREEN_HEIGHT;
    public static int CARD_WIDTH = 120;
    public static int CARD_HEIGHT = 212;
    public static double SCALE_COEFF = 1;
    public static int Y_SCREEN_START = 0;

    int test = 0;

    private Board board;
    private BoardView boardView;

    private Menu menu;

    public Stage stage;
    private Scene sceneGame;

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

        else
            System.out.println("Your display is 16/9 : " + SCREEN_WIDTH + "x" + SCREEN_HEIGHT);

        SCALE_COEFF = SCREEN_WIDTH / 1920;
        CARD_WIDTH *= SCALE_COEFF;
        CARD_HEIGHT *= SCALE_COEFF;
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

        board.addPlayer(new Player(myPlayerUsername));

        for(int i = 0; i < PLAYER_COUNT-1; i++)
            board.addPlayer(new Player());


        boardView = new BoardView(board);
        boardView.setDepthTest(DepthTest.ENABLE);

        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setRotationAxis(Rotate.X_AXIS);
        camera.setRotate(15);

        sceneGame = new Scene(boardView, SCREEN_WIDTH, SCREEN_HEIGHT, true, SceneAntialiasing.BALANCED);
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
            }
        });

       deal();
    }

    private boolean deal(){

        SequentialTransition st = new SequentialTransition();

        board.getDeck().refill();
        st.getChildren().add(boardView.getBringDeckOnBoardAnimation());

        board.getDeck().shuffle();
        board.getDeck().cut(29);
        //st.getChildren().add(boardView.getDeckView().getCutAnimation());

//        for(int i = 0; i < 6; i++){
//            for(int j = 0; j < PLAYER_COUNT; j++){
//                ParallelTransition pt = new ParallelTransition();
//                board.getDeck().deal(board.getPlayer(j));
//                pt.getChildren().add(boardView.getDeckView().getDealACardAnimation(boardView.getPlayerView(j)));
//                board.getDeck().deal(board.getPlayer(j));
//                pt.getChildren().add(boardView.getDeckView().getDealACardAnimation(boardView.getPlayerView(j)));
//                board.getDeck().deal(board.getPlayer(j));
//                pt.getChildren().add(boardView.getDeckView().getDealACardAnimation(boardView.getPlayerView(j)));
//
//
//                st.getChildren().add(pt);
//            }
//
//            board.getDeck().deal(board.getDog());
//            st.getChildren().add(boardView.getDeckView().getDealACardAnimation(boardView.getDogView()));
//        }

//        st.getChildren().add(boardView.getDogView().getDispatchAnimation());
//        st.getChildren().add(boardView.getPlayerView(0).flipAllCardViewsAnimation());
//
//        st.setOnFinished(event -> {
//                boardView.askUserChoice(getClass().getResource("user_choice.fxml"), this);
//        });

        st.play();

        return true;
    }

    private void reset() {
        //TODO : repenser l'usage de cette fonction
        board = new Board(PLAYER_COUNT);
        boardView = new BoardView(board);
    }

    public void askUsername() {

        try {
            stage.setScene(new UserInput(this));
            stage.setFullScreen(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserChoice(Player.UserChoice userChoice) {
        board.getPlayer(0).setChoice(userChoice);

        if(userChoice == Player.UserChoice.KEEP || userChoice == Player.UserChoice.TAKE) {

            System.out.println("show dog");

            //boardView.getDogView().getFlipAllCardViewsAnimation().play();

            //ecart
        }

        else {
            System.out.println("user choice set");
        }
    }
}