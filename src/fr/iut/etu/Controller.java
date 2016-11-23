package fr.iut.etu;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import fr.iut.etu.model.*;
import fr.iut.etu.view.BoardView;
import fr.iut.etu.view.CardView;
import javafx.animation.*;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.DepthTest;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Controller extends Application {

    public static final double CARD_THICK = 1.5;
    private static final int PLAYER_COUNT = 4;
    public static double SCREEN_WIDTH;
    public static double SCREEN_HEIGHT;

    public static double SCALE_COEFF = 1;
    public static int Y_SCREEN_START = 0;

    private static PerspectiveCamera camera;
    private Board board;
    private BoardView boardView;
    private Image boardImage = null;

    private Menu menu;

    public Stage stage;
    private Scene sceneGame;
    private MediaPlayer musicPlayer;

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
        CardView.CARD_WIDTH *= SCALE_COEFF;
        CardView.CARD_HEIGHT *= SCALE_COEFF;
        primaryStage.setTitle("Sylvain DUPOUY - Clément FLEURY S3D");

        menu = new Menu(this);

        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); //Disable Esc to exit fullscreen
        primaryStage.setFullScreen(true);
        primaryStage.setScene(menu);
        primaryStage.show();
        primaryStage.setY(Y_SCREEN_START);
        primaryStage.setHeight(SCREEN_HEIGHT);
        primaryStage.setWidth(SCREEN_WIDTH);

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

        CardView.backCard = new Image("file:./res/cards/back0.jpg");
    }

    public void startGame(String myPlayerUsername, Image selectedImage) {

        board = new Board(PLAYER_COUNT);

        board.addPlayer(new Player(myPlayerUsername));

        for(int i = 0; i < PLAYER_COUNT-1; i++)
            board.addPlayer(new Player());


        boardView = new BoardView(board, boardImage);
        boardView.setDepthTest(DepthTest.ENABLE);

        Image defaultImage = new Image("file:res/avatars/avatar_default.png");
        boardView.getPlayerView(0).setAvatar(selectedImage != null ? selectedImage : defaultImage);

        for(int i = 1; i < board.getPlayerCount(); i++)
            boardView.getPlayerView(i).setAvatar(defaultImage);

        camera = new PerspectiveCamera(false);
        camera.setRotationAxis(Rotate.X_AXIS);
        camera.setRotate(15);

        sceneGame = new Scene(boardView, SCREEN_WIDTH, SCREEN_HEIGHT, true, SceneAntialiasing.BALANCED);
        sceneGame.setCamera(camera);
        boardView.setTranslateY(Y_SCREEN_START);
        sceneGame.setFill(Color.BLACK);

        setScene(sceneGame);

        sceneGame.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    setScene(menu);
                    board = null;
                    boardView = null;
                    break;
            }
        });

        deal();
    }

    private void deal(){

        board.getDeck().refill();
        board.getDeck().shuffle();

        boardView.getBringDeckOnBoardAnimation().setOnFinished(event1 -> {

            //Cut animation even if we shuffled the deck, because the deck is initially sorted...
            Animation cutAnim = boardView.getDeckView().createCutAnimation();

            cutAnim.setOnFinished(actionEvent -> {

                SequentialTransition st = new SequentialTransition();
                recursiveDealingSequence(0, st);

                st.setOnFinished(event2 -> {

                    SequentialTransition st2 = new SequentialTransition();

                    ParallelTransition pt = new ParallelTransition();
                    pt.getChildren().add(boardView.getDogView().getDispatchAnimation());

                    for(int i = 0; i < PLAYER_COUNT; i++)
                        pt.getChildren().add(boardView.getPlayerView(i).getDispatchAnimation());

                    st2.getChildren().add(pt);
                    st2.getChildren().add(boardView.getPlayerView(0).getFlipAllCardViewsAnimation());

                    boardView.getPlayerView(0).sort();
                    st2.getChildren().add(boardView.getPlayerView(0).getSortAnimation());

                    st2.setOnFinished(event3 ->{
                        askUserChoice();
                    });

                    st2.play();
                });

                st.play();
            });

            cutAnim.play();
        });

        boardView.getBringDeckOnBoardAnimation().play();
    }

    private void recursiveDealingSequence(int playerIndex, SequentialTransition st){

        Animation animation;

        if(playerIndex == -1) {
            board.getDeck().deal(board.getDog());
            animation = boardView.getDealACardAnimation(boardView.getDogView());
        }
        else{
            board.getDeck().deal(board.getPlayer(playerIndex));
            Animation firstAnimation = boardView.getDealACardAnimation(boardView.getPlayerView(playerIndex));
            firstAnimation.setDelay(Duration.millis(0));

            board.getDeck().deal(board.getPlayer(playerIndex));
            Animation secondAnimation = boardView.getDealACardAnimation(boardView.getPlayerView(playerIndex));
            secondAnimation.setDelay(Duration.millis(200));

            board.getDeck().deal(board.getPlayer(playerIndex));
            Animation thirdAnimation = boardView.getDealACardAnimation(boardView.getPlayerView(playerIndex));
            thirdAnimation.setDelay(Duration.millis(400));

            animation = new ParallelTransition();
            ((ParallelTransition) animation).getChildren().addAll(
                    firstAnimation, secondAnimation, thirdAnimation
            );
        }

        animation.setCycleCount(1);
        st.getChildren().add(animation);
        if(board.getDeck().size() > 0){

            int nextHand = playerIndex + 1;
            if(nextHand == PLAYER_COUNT)
                nextHand = -1;

            recursiveDealingSequence(nextHand, st);
        }
    }

    private void reset() {
        //TODO : repenser l'usage de cette fonction
        board = new Board(PLAYER_COUNT);
        boardView = new BoardView(board, boardImage);
    }

    public void processUserChoice(int user_index, Player.UserChoice userChoice) {
        boardView.getChildren().remove(boardView.getChildren().size()-1); //Remove UserChoice GUI
        board.getPlayer(user_index).setChoice(userChoice);

        if(userChoice == Player.UserChoice.KEEP || userChoice == Player.UserChoice.TAKE) {
            Animation animFlip = boardView.getDogView().getFlipAllCardViewsAnimation();
            animFlip.setOnFinished(actionEvent -> {
                System.out.println("Transfert dog to player");
                board.getDog().transferCardsTo(board.getPlayer(user_index));
                Animation transferAnim  = boardView.getDogView().transferCardViewsTo(boardView.getPlayerView(0));
                transferAnim.setDelay(Duration.seconds(1.5));

                transferAnim.setOnFinished(actionEvent12 -> {
                    boardView.getPlayerView(user_index).sort();
                    Animation sortAnim = boardView.getPlayerView(user_index).getSortAnimation();

                    sortAnim.setOnFinished(actionEvent1 -> {

                        Button doneButton = new Button();
                        doneButton.setText("Done");
                        doneButton.setFont(new Font(30*SCALE_COEFF));
                        doneButton.getStylesheets().add("file:src/fr/iut/etu/style.css");
                        doneButton.getStyleClass().add("button");
                        doneButton.setMaxWidth(SCREEN_WIDTH / 5);
                        doneButton.setPrefWidth(SCREEN_WIDTH / 5);
                        doneButton.setMaxHeight(SCREEN_HEIGHT / 12);
                        doneButton.setPrefHeight(SCREEN_HEIGHT / 12);
                        doneButton.setTranslateX((SCREEN_WIDTH - SCREEN_WIDTH / 5) / 2);
                        doneButton.setTranslateY((SCREEN_HEIGHT - SCREEN_HEIGHT / 12) / 2);
                        doneButton.setTranslateZ(-1);

                        Label hint = new Label();
                        hint.setFont(new Font(30*SCALE_COEFF));
                        hint.setTextFill(Color.WHITE);
                        hint.setText("Please choose 6 cards to exclude");
                        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader(); //Utilisé pour déterminer la taille du label
                        hint.setTranslateX((SCREEN_WIDTH - fontLoader.computeStringWidth(hint.getText(), hint.getFont())) / 2);
                        hint.setTranslateY((SCREEN_HEIGHT - hint.getHeight()) / 2);
                        hint.setTranslateZ(-1);
                        boardView.getChildren().add(hint);

                        ArrayList<CardView> trumps = new ArrayList<>();
                        ArrayList<CardView> allowed_cards = new ArrayList<>();

                        for (CardView cardView : boardView.getPlayerView(user_index).getCardViews())
                            if(cardView.getCard() instanceof Trump && cardView.getCard().getValue() != 1 && cardView.getCard().getValue() != 21) //On exclut les bouts
                                trumps.add(cardView);
                            else if(cardView.getCard().getValue() != 14 && !(cardView.getCard() instanceof Trump) && !(cardView.getCard() instanceof Fool)) //On exclut les rois et l'excuse
                                allowed_cards.add(cardView);

                        int nb_allowed_trumps = 0;

                        if(allowed_cards.size() < 6) {
                            nb_allowed_trumps = 6 - allowed_cards.size(); //Si aucune carte de la main n'est jouable, alors on a la possiblité de jouer ses atouts (mais ils doivent être montrés aux autres joueurs)
                            allowed_cards.addAll(trumps);
                        }

                        ArrayList<CardView> gap = new ArrayList<>(6);

                        final int[] nb_trump_played = {0};

                        int finalNb_allowed_trumps = nb_allowed_trumps;
                        for (CardView cardView : allowed_cards) {
                            cardView.setOnMouseClicked(mouseEvent -> {

                                if (cardView.isSelected()) {

                                    if(gap.size() == 6) {
                                        boardView.getChildren().remove(doneButton);
                                        boardView.getChildren().add(hint);
                                    }

                                    if(cardView.getCard() instanceof Trump)
                                        nb_trump_played[0]--;

                                    gap.remove(cardView);
                                    cardView.setSelect(!cardView.isSelected());


                                }

                                else if(gap.size() < 6) {

                                    //On s'assure que le nombre d'atouts dans l'écart est respecté
                                    if((cardView.getCard() instanceof Trump && nb_trump_played[0] < finalNb_allowed_trumps) || !(cardView.getCard() instanceof Trump)) {
                                        gap.add(cardView);
                                        cardView.setSelect(!cardView.isSelected());

                                        if (gap.size() == 6) {
                                            boardView.getChildren().remove(hint);
                                            boardView.getChildren().add(doneButton);
                                        }
                                    }
                                }
                            });
                        }

                        doneButton.setOnAction(actionEvent2 -> {
                            System.out.println("6 cards will be removed");

                            boardView.getChildren().remove(doneButton);

                            ParallelTransition pt = new ParallelTransition();

                            for(CardView cardView : gap) {
                                SequentialTransition st = new SequentialTransition();

                                if(cardView.getCard() instanceof Trump) {
                                    TranslateTransition tt = new TranslateTransition(Duration.seconds(1), cardView);
                                    tt.setByY(-SCREEN_HEIGHT / 2);
                                    st.getChildren().add(tt);
                                }

                                FadeTransition ft = new FadeTransition(Duration.seconds(1), cardView);
                                ft.setDelay(Duration.seconds(1));
                                ft.setToValue(0);
                                st.getChildren().add(ft);
                                pt.getChildren().add(st);
                            }

                            pt.setOnFinished(workerStateEvent -> {
                                gap.forEach(cardView -> board.getPlayer(user_index).removeCard(cardView.getCard()));
                                boardView.getPlayerView(user_index).getSortAnimation().play();
                            });

                            pt.play();
                        });

                    });

                    sortAnim.play();
                });

                transferAnim.play();
            });

            animFlip.play();
        }

        System.out.println("user choice set");
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
        stage.setFullScreen(true);

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    setScene(menu);
                    break;
            }
        });
    }

    public Menu getMenu() {
        return menu;
    }

    public void setBackCardImage(Image image) {
        CardView.backCard = image;
    }

    public void setBoardImage(Image imageView) {
        boardImage = imageView;
    }

    public MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public static PerspectiveCamera getCamera() {
        return camera;
    }

    public void askUserChoice() {
        System.out.println("Asking user...");
        UserChoice userChoice = new UserChoice(this);
        boardView.getChildren().add(userChoice);
    }


}