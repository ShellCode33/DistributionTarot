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
import java.util.ArrayList;
import java.util.Random;

/*
 *  Projet Distribution tarot de Clément FLEURY et Sylvain DUPOUY - S3D
 *
 *  Architecture : MVP - Supervising Controller
 *
 *  Le controller s'occupe uniquement de lancer les actions dans le model et de la view
 *  La view se met à jour seule en observant le model
 *  Le model est totalement indépendant du controller et de la vue
 *
 */

public class Controller extends Application {

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

    private static final ArrayList<Animation> animationsUsed = new ArrayList<>();

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

    //Permet de conserver le ration 16/9 sur tout type d'écran
    private void keepScreenRatio() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        SCREEN_WIDTH = (int)bounds.getWidth();
        SCREEN_HEIGHT = (int)bounds.getHeight();

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

        //Mise à jour de la taille des CardView
        CardView.CARD_WIDTH *= SCALE_COEFF;
        CardView.CARD_HEIGHT *= SCALE_COEFF;
        CardView.CARD_THICK *= SCALE_COEFF;
    }
    //Retourne la scene principale avec le layout correspondant au menu principal
    private Scene initSceneWithMenu() throws IOException {

        //Construction de la scene
        menu = new Menu(this);
        Scene scene = new Scene(menu, SCREEN_WIDTH, SCREEN_HEIGHT, true, SceneAntialiasing.DISABLED); //Anti aliasing bug with some versions of jdk
        scene.setFill(Color.BLACK);

        //Construction de la camera
        camera = new PerspectiveCamera(false);
        camera.setRotationAxis(Rotate.X_AXIS);
        scene.setCamera(camera);

        //Si on appuie sur echap, on revient au menu principal et le model et la view sont reinit
        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    camera.setRotate(0); //On enlève l'inclinaison de la caméra dans le menu
                    setLayout(menu);
                    resetModelAndView();
                    break;
            }
        });
        return scene;
    }
    //Initialisation de la fenêtre
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
    //Initialisation de la musique
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
    //Préparation du model et de la view
    private void initModelAndView() {
        board = new Board(PLAYER_COUNT);
        boardView = new BoardView(board);
        boardView.setDepthTest(DepthTest.ENABLE);
    }
    //Reset de la partie si le joueur fait "ECHAP"
    private void resetModelAndView() {

        //On stop toutes les animations au cas où il en resterait en cours...
        animationsUsed.forEach(Animation::stop);
        animationsUsed.clear();

        boardView.reset();
        board.reset();
        initModelAndView();
    }

    //Lancement de la distribution !
    public void prepareDeal(String myPlayerUsername, Image selectedImage) {

        initPlayersModelAndView(myPlayerUsername, selectedImage);

        //Changement de layout, ajustement de la caméra et du layout
        camera.setRotate(15);
        setLayout(boardView);
        boardView.setTranslateY(Y_SCREEN_START);

        //Début de la distribution
        deal();
    }
    //Initialisation des joueurs dans le model et dans la vue
    private void initPlayersModelAndView(String myPlayerUsername, Image selectedImage) {
        board.getPlayer(0).setName(myPlayerUsername);

        for(int i = 1; i < PLAYER_COUNT; i++)
            board.getPlayer(i).setName("#computer"+i);

        Image defaultImage = new Image("file:res/avatars/avatar_default.png");
        boardView.getPlayerView(0).setAvatar(selectedImage != null ? selectedImage : defaultImage);

        for(int i = 1; i < PLAYER_COUNT; i++)
            boardView.getPlayerView(i).setAvatar(defaultImage);
    }
    //Et là c'est parti !
    private void deal(){

        SequentialTransition sequentialTransition = new SequentialTransition();
        //Dans un premier temps on enchaîne les animations de de l'arrivée du deck ainsi que la phase de distribution
        sequentialTransition.getChildren().addAll(deckArrival(), dealSequence());

        //Lorsqu'elles ont fini...
        sequentialTransition.setOnFinished(event -> {

            SequentialTransition st = new SequentialTransition();
            //On etale les cartes de tous les joueurs
            //On retourne les cartes du joueur 0
            //On trie les cartes du joueur 0

            st.getChildren().addAll(boardView.dispatchAllHandViews(),
                    boardView.getPlayerView(0).flipAllCardViews(),
                    boardView.getPlayerView(0).sortCardViews());

            //ensuite, on demande le contrat au joueur
            st.setOnFinished(event2 -> askUserChoice());

            playAnimation(st);
        });

        playAnimation(sequentialTransition);
    }
    //Arrivée du deck
    private SequentialTransition deckArrival() {
        SequentialTransition st = new SequentialTransition();

        //On remplit le deck dans le model
        board.getDeck().refill();
        //En théorie il ne faudrait pas mélanger, mais les cartes étant générées dans l'ordre en début de partie, il faut les mélanger en plus de couper
        board.getDeck().shuffle();
        //On coupe le deck à un endroit random, pas trop proche du début ou de la fin
        board.getDeck().cut(new Random().nextInt(54)+12);

        //On amène le deck au centre du plateau et on le coupe
        st.getChildren().addAll(boardView.getBringDeckViewOnCenterAnimation(),
                boardView.getDeckView().cut());
        return st;
    }
    //La sequence de distribution en elle-même
    private ParallelTransition dealSequence() {
        int currentPlayerIndex = 0;
        ParallelTransition dealSequence = new ParallelTransition();

        int cardDealtCount = 0;
        while(board.getDeck().size() > 0){

            board.getDeck().deal(board.getPlayer(currentPlayerIndex));
            Animation animation = boardView.getDeckView().dealACardViewTo(boardView.getPlayerView(currentPlayerIndex));
            animation.setDelay(Duration.millis(cardDealtCount*100));
            dealSequence.getChildren().add(animation);
            cardDealtCount++;

            //Si on a distribué 3 cartes d'affilé au joueur courant...
            if(board.getPlayer(currentPlayerIndex).getCardCount()%3 == 0) {
                //On peut passer au joueur suivant ...
                currentPlayerIndex = (currentPlayerIndex+ 1) % PLAYER_COUNT;
                //Mais on peut aussi, avec une certaine probabilité distribué des cartes au chien
                //En prenant soin qu'il ne s'agisse pas de la dernière carte
                //Mais que le chien contiennent bien 6 cartes quoi qu'il arrive
                while(board.getDog().getCardCount() < 6
                        && board.getDeck().size() > 0
                        && (Math.random() < 0.25 || board.getDeck().size() - 3 == 6 - board.getDog().getCardCount())){

                    board.getDeck().deal(board.getDog());
                    animation = boardView.getDeckView().dealACardViewTo(boardView.getDogView());
                    animation.setDelay(Duration.millis(cardDealtCount*100));
                    dealSequence.getChildren().add(animation);
                    cardDealtCount++;
                }
            }
        }
        return dealSequence;
    }

    //Demande du contrat du joueur
    public void askUserChoice() {
        UserChoice userChoice = new UserChoice(this);
        boardView.getChildren().add(userChoice);
    }
    //Gestion du contrat du joueur
    public void processUserChoice(Player.UserChoice userChoice) {
        //Une fois que le joueur a choisi son contrat, on enleve le layout du choix
        boardView.getChildren().remove(boardView.getChildren().size()-1);
        board.getPlayer(0).setChoice(userChoice);

        if(userChoice == Player.UserChoice.KEEP || userChoice == Player.UserChoice.TAKE) {
            keepOrTake();
        }
        else {
            playAnimation(boardView.getDogView().explode());
            boardView.getHint().setText("Press esc to go back to main menu");
            boardView.showHint();
        }
    }
    //Si le joueur a choisi de prendre ou de garder
    private void keepOrTake() {
        SequentialTransition sequentialTransition = new SequentialTransition();

        ParallelTransition parallelTransition = new ParallelTransition();

        //On transfère toutes les cartes du chien au joueur 0
        while(!board.getDog().getCards().isEmpty()){
            try {
                board.getDog().transferCardTo(board.getPlayer(0), board.getDog().getCards().get(0));
                parallelTransition.getChildren().add(boardView.getDogView().transferCardViewTo(boardView.getPlayerView(0)));
            } catch (InvalidObjectException e) {
                e.printStackTrace();
            }
        }

        //Mais avant on retourne toutes les cartes du chien
        sequentialTransition.getChildren().addAll(boardView.getDogView().flipAllCardViews(),
                parallelTransition);

        //Ensuite, on peut trier les cartes du joueur 0 et constituer l'écart
        sequentialTransition.setOnFinished(event -> {
            playAnimation(boardView.getPlayerView(0).sortCardViews());
            boardView.getPlayerView(0).handleGap(this);
        });

        playAnimation(sequentialTransition);
    }
    //Une fois que l'écart est constitué
    public void gapIsDone() {
        boardView.getPlayerView(0).getGap().forEach(cardView -> board.getPlayer(0).removeCard(cardView.getCard()));
        Controller.playAnimation(boardView.getPlayerView(0).sortCardViews());
        boardView.getHint().setText("Press esc to go back to main menu");
        boardView.showHint();
    }

    public void setLayout(Parent root) {
        stage.getScene().setRoot(root);
    }

    public Menu getMenu() {
        return menu;
    }

    public void setBoardImage(Image image) { //à cause du bug javafx le paramètre ne sert plus à rien, mais nous gardons espoir qu'un jour une mise à jour de javafx corrige le problème
        resetModelAndView(); //Il y a un bug javafx qui fait que si on change d'image à la volée (comme c'était fait intialement) des bugs graphiques (notamment sur les particules) apparaissent, nous sommes donc obligés de tout reset lorsque l'on change de background
        //boardView.setBackground(image); //bug javafx
    }

    public MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }

    //Toutes les animations sont lancées à partir de cette methode statique afin de pouvoir les arrêter proprement si le joueur fait "ECHAP" alors que des animations sont encore en cours
    public static void playAnimation(Animation animation) {
        animationsUsed.add(animation);
        animation.play();
    }
}