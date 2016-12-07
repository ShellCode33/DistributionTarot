package fr.iut.etu;

import fr.iut.etu.layouts.Menu;
import fr.iut.etu.layouts.UserChoice;
import fr.iut.etu.model.Board;
import fr.iut.etu.model.Player;
import fr.iut.etu.view.BoardView;
import fr.iut.etu.view.CardView;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Random;

public class Presenter extends Application {

    public static final int PLAYER_COUNT = 4;
    public static double SCREEN_WIDTH;
    public static double SCREEN_HEIGHT;

    public static double SCALE_COEFF = 1;
    public static int Y_SCREEN_START = 0;

    private Board board;
    private BoardView boardView;

    private Menu menu;

    private Stage stage;
    private MediaPlayer musicPlayer;

    private PerspectiveCamera camera;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        stage = primaryStage;

        keepScreenRatio();
        Scene menuScene = initSceneWithMenu();
        initStage(primaryStage, menuScene);
        initMusic();
        initModelAndView();
    }

    private void initModelAndView() {
        board = new Board(PLAYER_COUNT);
        boardView = new BoardView(board);
        boardView.setDepthTest(DepthTest.ENABLE);
    }

    private void initStage(Stage primaryStage, Scene menuScene) {
        primaryStage.setTitle("Sylvain DUPOUY - Clément FLEURY S3D");
        primaryStage.setScene(menuScene);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); //Disable Esc to exit fullscreen
        primaryStage.setFullScreen(true);
        primaryStage.setY(Y_SCREEN_START);
        primaryStage.setHeight(SCREEN_HEIGHT);
        primaryStage.setWidth(SCREEN_WIDTH);
        primaryStage.show();
    }

    private Scene initSceneWithMenu() throws IOException {
        Scene scene;
        menu = new Menu(this);
        scene = new Scene(menu, SCREEN_WIDTH, SCREEN_HEIGHT, true, SceneAntialiasing.DISABLED); //Anti aliasing bug with some versions of jdk
        scene.setFill(Color.BLACK);

        camera = new PerspectiveCamera(false);
        camera.setRotationAxis(Rotate.X_AXIS);
        scene.setCamera(camera);

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    camera.setRotate(0);
                    setLayout(menu);
                    board = null;
                    boardView = null;
                    initModelAndView();
                    break;
            }
        });
        return scene;
    }

    private void initMusic() {
        Media music = new Media(new File("res/audio/main.wav").toURI().toString());

        try {
            musicPlayer = new MediaPlayer(music);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            musicPlayer.setVolume(0.5);
            musicPlayer.play();
        }
        catch(MediaException e) {
            System.out.println("Your OS doesn't support music player : might be a javafx issue");
        }
    }

    private void keepScreenRatio() {
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
        else {
            System.out.println("Your display is 16/9 : " + SCREEN_WIDTH + "x" + SCREEN_HEIGHT);
        }
        SCALE_COEFF = SCREEN_WIDTH / 1920;

        CardView.CARD_WIDTH *= SCALE_COEFF;
        CardView.CARD_HEIGHT *= SCALE_COEFF;
        CardView.CARD_THICK *= SCALE_COEFF;
    }

    public void startGame(String myPlayerUsername, Image selectedImage) {

        initPlayersModelAndView(myPlayerUsername, selectedImage);

        camera.setRotate(15);
        setLayout(boardView);
        boardView.setTranslateY(Y_SCREEN_START);

        deal();
    }

    private void initPlayersModelAndView(String myPlayerUsername, Image selectedImage) {
        board.getPlayer(0).setName(myPlayerUsername);

        for(int i = 1; i < PLAYER_COUNT; i++)
            board.getPlayer(i).setName("#computer"+i);

        Image defaultImage = new Image("file:res/avatars/avatar_default.png");
        boardView.getPlayerView(0).setAvatar(selectedImage != null ? selectedImage : defaultImage);

        for(int i = 1; i < PLAYER_COUNT; i++)
            boardView.getPlayerView(i).setAvatar(defaultImage);
    }

    private void deal(){

        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(deckArrival(), dealSequence());

        sequentialTransition.setOnFinished(event -> {

            Animation dispatchAllViewsAnimation = boardView.getDispatchAllViewsAnimation();

            dispatchAllViewsAnimation.setOnFinished(event1 -> {
                SequentialTransition st = new SequentialTransition();

                st.getChildren().add(boardView.getPlayerView(0).getFlipAllCardViewsAnimation());
                st.getChildren().add(boardView.getPlayerView(0).getSortAnimation());

                st.setOnFinished(event2 -> askUserChoice());

                st.play();
            });

            dispatchAllViewsAnimation.play();
        });

        sequentialTransition.play();
    }

    private SequentialTransition deckArrival() {
        SequentialTransition sequentialTransition = new SequentialTransition();

        board.getDeck().refill();
        board.getDeck().shuffle(); //En théorie il ne faudrait pas mélanger, mais les cartes étant générées dans l'ordre en début de partie, il faut les mélanger en plus de couper
        board.getDeck().cut(new Random().nextInt(60)+9);

        sequentialTransition.getChildren().add(boardView.getBringDeckOnBoardAnimation());
        sequentialTransition.getChildren().add(boardView.getDeckView().getCutAnimation());
        return sequentialTransition;
    }

    private ParallelTransition dealSequence() {
        int currentPlayerIndex = 0;
        ParallelTransition dealSequence = new ParallelTransition();

        int cardDealtCount = 0;
        while(board.getDeck().size() > 0){

            board.getDeck().deal(board.getPlayer(currentPlayerIndex));
            Animation animation = boardView.getDealACardAnimation(boardView.getPlayerView(currentPlayerIndex));
            animation.setDelay(Duration.millis(cardDealtCount*100));
            dealSequence.getChildren().add(animation);
            cardDealtCount++;

            if(board.getPlayer(currentPlayerIndex).getCardCount()%3 == 0) {
                currentPlayerIndex = (currentPlayerIndex+ 1) % PLAYER_COUNT;

                while(board.getDog().getCardCount() < 6
                        && board.getDeck().size() > 0
                        && (Math.random() < 0.25 || board.getDeck().size() - 3 == 6 - board.getDog().getCardCount())){

                    board.getDeck().deal(board.getDog());
                    animation = boardView.getDealACardAnimation(boardView.getDogView());
                    animation.setDelay(Duration.millis(cardDealtCount*100));
                    dealSequence.getChildren().add(animation);
                    cardDealtCount++;
                }
            }
        }
        return dealSequence;
    }

    public void processUserChoice(Player.UserChoice userChoice) {
        boardView.getChildren().remove(boardView.getChildren().size()-1); //Remove UserChoice GUI
        board.getPlayer(0).setChoice(userChoice);

        if(userChoice == Player.UserChoice.KEEP || userChoice == Player.UserChoice.TAKE)
            keepOrTake();
        else {
            boardView.getDogView().getCardViews().forEach(cardView -> {
                cardView.setMoving(true);
                boardView.addParticlesToCard(cardView);
            });

            boardView.getDogView().createExplodeAnimation().play();
        }
    }

    private void keepOrTake() {
        SequentialTransition sequentialTransition = new SequentialTransition();
        Animation flipAllCardViewsAnimation = boardView.getDogView().getFlipAllCardViewsAnimation();
        sequentialTransition.getChildren().add(flipAllCardViewsAnimation);

        boardView.getDogView().getCardViews().forEach(cardView -> {
            boardView.addParticlesToCard(cardView);
        });

        //Lorsque les cartes ont terminé de se retourner, on les remet en mouvement afin de préparer le déplacement vers la playerview
        flipAllCardViewsAnimation.setOnFinished(actionEvent -> {
            boardView.getDogView().getCardViews().forEach(cardView -> cardView.setMoving(true));
        });

        ParallelTransition parallelTransition = new ParallelTransition();

        //transfer all cards from dog to player 0
        while(!board.getDog().getCards().isEmpty()){
            try {
                board.getDog().transferCardTo(board.getPlayer(0), board.getDog().getCards().get(0));
                parallelTransition.getChildren().add(boardView.getDogView().transferCardViewTo(boardView.getPlayerView(0)));
            } catch (InvalidObjectException e) {
                e.printStackTrace();
            }
        }

        sequentialTransition.getChildren().add(parallelTransition);

        sequentialTransition.setOnFinished(event -> {
            boardView.getPlayerView(0).getCardViews().forEach(cardView -> {
                cardView.setMoving(false);
                boardView.removeParticlesOfCard(cardView);
            });
            boardView.getPlayerView(0).getSortAnimation().play();
            boardView.handleGap();
        });

        sequentialTransition.play();
    }

    public void setLayout(Parent root) {
        stage.getScene().setRoot(root);
    }

    public Menu getMenu() {
        return menu;
    }

    public void setBoardImage(Image image) {
        boardView.setBackgroundCustom(image);
    }

    public MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public void askUserChoice() {
        System.out.println("Asking user...");
        UserChoice userChoice = new UserChoice(this);
        boardView.getChildren().add(userChoice);
    }


}