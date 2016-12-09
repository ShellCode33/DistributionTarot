package fr.iut.etu.view;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import fr.iut.etu.Controller;
import fr.iut.etu.layouts.Settings;
import fr.iut.etu.model.Board;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class BoardView extends Group {

    private final ArrayList<PlayerView> playerViews = new ArrayList<>();
    private DeckView deckView;
    private DogView dogView;

    private Button doneButton;
    private Label hint;

    private Canvas particlesCanvas;
    private ArrayList<CardView> cardViewsWithParticles = new ArrayList<>();
    private AnimationTimer particlesLoop;

    private ImageView background;

    //L'animation de l'arrivée du deck ne sera pas générée à la volée donc on peut la stocker
    private Animation bringDeckViewOnCenterAnimation;

    public BoardView(Board board) {
        super();

        deckView = new DeckView(board.getDeck());
        getChildren().add(deckView);
        dogView = new DogView(board.getDog());
        getChildren().add(dogView);

        for (int i = 0; i < board.getPlayerCount(); i++) {
            PlayerView playerView = new PlayerView(board.getPlayer(i));
            playerViews.add(playerView);
            getChildren().add(playerView);
        }

        //Positionnement des vues des joueurs et du chien
        placeHandViews();

        initBackground();
        initParticlesHandling();

        initDoneButton();
        initLabels();

        createBringDeckOnBoardAnimation();
    }
    //Initialisation du background
    private void initBackground() {
        background = new ImageView(Settings.getBackgroundImage());
        background.setFitWidth(Controller.SCREEN_WIDTH);
        background.setFitHeight(Controller.SCREEN_HEIGHT);
        getChildren().add(background);
    }
    //Initialisation de la gestion des particles
    private void initParticlesHandling() {
        particlesCanvas = new Canvas(Controller.SCREEN_WIDTH, Controller.SCREEN_HEIGHT);
        particlesCanvas.setTranslateZ(-1);
        getChildren().add(particlesCanvas);

        ParticleView.createTexture();

        //Timer permettant la mise à jour des particules
        particlesLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //synchronized pour éviter les accès concurrents à la list de CardViews
                synchronized (this) {
                    for (CardView cardView : cardViewsWithParticles) {
                        cardView.removeDeadParticles();

                        //Tant que la CardView est en mouvement, on lui ajoute des particules
                        if (cardView.isMoving()) {
                            for (int i = 0; i < 5; i++)
                                cardView.addParticle();
                        }
                    }
                }

                particlesCanvas.getGraphicsContext2D().clearRect(0, 0, Controller.SCREEN_WIDTH, Controller.SCREEN_HEIGHT);

                // move sprite: apply acceleration, calculate velocity and location
                //On dessine toutes les particules
                //On diminue la durée de vie des  particules
                CardView.getAllParticles().stream().parallel().forEach(ParticleView::move);
                CardView.getAllParticles().stream().forEach(particle -> particle.draw(particlesCanvas.getGraphicsContext2D()));
                CardView.getAllParticles().stream().parallel().forEach(ParticleView::decreaseLifeSpan);
            }
        };

        particlesLoop.start();
    }
    //Initialisation du label d'incation pour l'utilisateur
    private void initLabels() {
        hint = new Label();
        hint.setFont(new Font(30* Controller.SCALE_COEFF));
        hint.setTextFill(Color.WHITE);
        hint.setTranslateY((Controller.SCREEN_HEIGHT - hint.getHeight()) / 2);
        hint.setTranslateZ(-20);
    }
    //Initialisation du boutton de fin de constituion de l'écart
    private void initDoneButton() {
        doneButton = new Button();
        doneButton.setText("Done");
        doneButton.setFont(new Font(30* Controller.SCALE_COEFF));
        doneButton.getStylesheets().add("file:res/style.css");
        doneButton.getStyleClass().add("button");
        doneButton.setMaxWidth(Controller.SCREEN_WIDTH / 5);
        doneButton.setPrefWidth(Controller.SCREEN_WIDTH / 5);
        doneButton.setMaxHeight(Controller.SCREEN_HEIGHT / 12);
        doneButton.setPrefHeight(Controller.SCREEN_HEIGHT / 12);
        doneButton.setTranslateX((Controller.SCREEN_WIDTH - Controller.SCREEN_WIDTH / 5) / 2);
        doneButton.setTranslateY((Controller.SCREEN_HEIGHT - Controller.SCREEN_HEIGHT / 12) / 2);
        doneButton.setTranslateZ(-1);
    }
    //Placement des playerViews et de la dogView (attention, cela ne vaut que pour 4 joueurs)
    private void placeHandViews(){

        dogView.getTransforms().addAll(
                new Translate(
                        4* Controller.SCREEN_WIDTH/6,
                        (Controller.SCREEN_HEIGHT-CardView.CARD_HEIGHT)/2,
                        -1),
                new Rotate(
                        0,
                        Rotate.Z_AXIS
                )
        );

        PlayerView playerView;

        playerView = getPlayerView(0);
        playerView.getTransforms().addAll(
                new Translate(
                        (Controller.SCREEN_WIDTH - CardView.CARD_WIDTH)/2,
                        Controller.SCREEN_HEIGHT - CardView.CARD_HEIGHT/2,
                        -1),
                new Rotate(
                        0,
                        Rotate.Z_AXIS
                )
        );

        playerView = getPlayerView(1);
        playerView.getTransforms().addAll(
                new Translate(CardView.CARD_HEIGHT,
                        Controller.SCREEN_HEIGHT/2,
                        -1),
                new Rotate(
                        90,
                        Rotate.Z_AXIS
                )
        );

        playerView = getPlayerView(2);
        playerView.getTransforms().addAll(
                new Translate(
                        Controller.SCREEN_WIDTH/2,
                        CardView.CARD_HEIGHT/2,
                        -1),
                new Rotate(
                        180,
                        Rotate.Z_AXIS
                )
        );

        playerView = getPlayerView(3);
        playerView.getTransforms().addAll(
                new Translate(
                        Controller.SCREEN_WIDTH - CardView.CARD_HEIGHT,
                        Controller.SCREEN_HEIGHT/2,
                        -1),
                new Rotate(
                        270,
                        Rotate.Z_AXIS
                )
        );

    }
    //Création de l'animation d'arrivée du deck
    private void createBringDeckOnBoardAnimation() {
        RotateTransition rotate = new RotateTransition(Duration.seconds(2), deckView);
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.setFromAngle(0);
        rotate.setToAngle(270);
        rotate.setCycleCount(1);

        TranslateTransition translate = new TranslateTransition(Duration.seconds(2), deckView);
        translate.setToX((Controller.SCREEN_WIDTH-CardView.CARD_WIDTH)/2);
        translate.setToY((Controller.SCREEN_HEIGHT-CardView.CARD_HEIGHT)/2);
        translate.setCycleCount(1);

        ParallelTransition st = new ParallelTransition();
        st.getChildren().addAll(rotate, translate);

        bringDeckViewOnCenterAnimation = st;
    }

    //Etalement des playerView et dogView
    public Animation dispatchAllHandViews(){
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().add(dogView.dispatchAllCardViews());

        for(PlayerView pv : playerViews)
            parallelTransition.getChildren().add(pv.dispatchAllCardViews());

        return parallelTransition;
    }
    //Ajout et suppression de particules à une cardView
    public synchronized void addParticlesToCard(CardView cardView) {
        cardViewsWithParticles.add(cardView);
    }
    public synchronized void removeParticlesOfCard(CardView cardView) {
        cardViewsWithParticles.remove(cardView);
    }

    public void setHintText(String text){
        hint.setText(text);
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader(); //Utilisé pour déterminer la taille du label
        hint.setTranslateX((Controller.SCREEN_WIDTH - fontLoader.computeStringWidth(hint.getText(), hint.getFont())) / 2);
    }

    public void showHint(){
        if(!getChildren().contains(hint))
            getChildren().add(hint);
    }

    public void hideHint(){
        if(getChildren().contains(hint))
            getChildren().remove(hint);
    }

    public void showDoneButton(){
        if(!getChildren().contains(doneButton))
            getChildren().add(doneButton);
    }

    public void hideDoneButton(){
        if(getChildren().contains(doneButton))
            getChildren().remove(doneButton);
    }

    public Button getDoneButton() {
        return doneButton;
    }

    public void setBackground(Image image) {
        getChildren().remove(background);
        background = new ImageView(image);
        background.setFitWidth(Controller.SCREEN_WIDTH);
        background.setFitHeight(Controller.SCREEN_HEIGHT);
        getChildren().add(background);
    }

    public DeckView getDeckView() {
        return deckView;
    }

    public DogView getDogView() {
        return dogView;
    }

    public PlayerView getPlayerView(int i) {
        return playerViews.get(i);
    }

    public Animation getBringDeckViewOnCenterAnimation() {
        return bringDeckViewOnCenterAnimation;
    }

    public Animation createPetitSecAnimation() {
        SequentialTransition st = new SequentialTransition();
        ParallelTransition pt = new ParallelTransition();

        //onStart
        Transition onStart = new Transition() {@Override protected void interpolate(double frac) {}};
        onStart.setDelay(Duration.millis(1));

        onStart.setOnFinished(actionEvent -> {
            for(PlayerView playerView : playerViews) {
                for (CardView cardView : playerView.getCardViews()) {
                    cardView.setMoving(true);
                    addParticlesToCard(cardView);
                }
            }
        });

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(3), dogView);
        translateTransition.setToX(-Controller.SCREEN_WIDTH-CardView.CARD_WIDTH);
        translateTransition.setToY(-Controller.SCREEN_HEIGHT-CardView.CARD_HEIGHT);
        pt.getChildren().add(translateTransition);

        for(PlayerView playerView : playerViews) {
            for(CardView cardView : playerView.getCardViews()) {
                translateTransition = new TranslateTransition(Duration.seconds(3), cardView);
                translateTransition.setToX(-Controller.SCREEN_WIDTH - CardView.CARD_WIDTH);
                translateTransition.setToY(-Controller.SCREEN_HEIGHT - CardView.CARD_HEIGHT);
                pt.getChildren().add(translateTransition);
            }
        }

        pt.setOnFinished(actionEvent -> {
            for(PlayerView playerView : playerViews) {
                for (CardView cardView : playerView.getCardViews()) {
                    cardView.setMoving(false);
                    removeParticlesOfCard(cardView);
                }
            }
        });

        st.getChildren().addAll(onStart, pt);
        st.setDelay(Duration.seconds(3));
        return st;
    }

    public void reset() {
        getChildren().clear();
        playerViews.clear();
        deckView = null;
        dogView = null;

        doneButton = null;
        hint = null;

        particlesLoop.stop();
        cardViewsWithParticles.clear();
        cardViewsWithParticles = null;
        particlesCanvas.getGraphicsContext2D().clearRect(0, 0, Controller.SCREEN_WIDTH, Controller.SCREEN_HEIGHT);
        particlesCanvas = null;
        background = null;
    }
}
