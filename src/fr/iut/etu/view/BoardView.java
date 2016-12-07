package fr.iut.etu.view;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import fr.iut.etu.Controller;
import fr.iut.etu.layouts.Settings;
import fr.iut.etu.model.Board;
import fr.iut.etu.model.Fool;
import fr.iut.etu.model.Trump;
import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
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
    private Board board;

    private Canvas particlesCanvas;
    private ArrayList<CardView> cardViewsWithParticles = new ArrayList<>();
    private AnimationTimer particlesLoop;

    private ImageView background;

    //L'animation de l'arrivée du deck ne sera pas générée à la volée donc on peut la stocker
    private Animation bringDeckOnBoardAnimation;

    public BoardView(Board board) {
        super();

        this.board = board;


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
        initHintLabel();

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
    private void initHintLabel() {
        hint = new Label();
        hint.setFont(new Font(30* Controller.SCALE_COEFF));
        hint.setTextFill(Color.WHITE);
        hint.setText("Please choose 6 cards to exclude");
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader(); //Utilisé pour déterminer la taille du label
        hint.setTranslateX((Controller.SCREEN_WIDTH - fontLoader.computeStringWidth(hint.getText(), hint.getFont())) / 2);
        hint.setTranslateY((Controller.SCREEN_HEIGHT - hint.getHeight()) / 2);
        hint.setTranslateZ(-1);
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

        bringDeckOnBoardAnimation = st;
    }

    //Animation de distribution d'une carte du deck vers une PlayerView ou DogView
    public Animation getDealACardAnimation(HandView handView) {
        //Il faut qu'une carte ait été distribué dans le model
        if(handView.getCardViewsWaitingToBeDealt().isEmpty())
            throw new UnsupportedOperationException("No card was dealt in the model");

        CardView cardView = handView.getCardViewsWaitingToBeDealt().poll();

        //Translation jusqu'à la handview
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), cardView);
        //Rotation pour être dans le même sens que la handView
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), cardView);
        //Ajustement de la hauteur à laquelle doit se trouver la cardView
        TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.3), cardView);

        //Cette animation "vide" sert uniquement à avoir un evenement onStart
        Transition firstAnimation = new Transition() {@Override protected void interpolate(double frac) {}};
        firstAnimation.setOnFinished(event -> {
            //Ajout des particules
            addParticlesToCard(cardView);
            cardView.setMoving(true);

            //Ajout de la cardView dans la handView et réduction de la deckView
            handView.addCardView(cardView);
            deckView.removeImageViewOnTop();

            //On récupère l'angle de rotation de la handView
            //ainsi que les bounds de la deckView dans référentiel de la handView
            Rotate handViewRotate = (Rotate) handView.getTransforms().get(1);
            Bounds deckViewBoundsInHandView = handView.parentToLocal(deckView.getBoundsInParent());
            //On détermine la position d'arrivée de la cardView
            Point3D destination = new Point3D(0,0,-handView.getCardViews().size()*CardView.CARD_THICK-1);

            //On peut maintenant définir les différentes animations
            cardView.setRotationAxis(Rotate.Z_AXIS);
            cardView.setRotate(270 - handViewRotate.getAngle());

            translateTransition1.setFromX(deckViewBoundsInHandView.getMinX());
            translateTransition1.setFromY(deckViewBoundsInHandView.getMinY());
            translateTransition1.setFromZ(deckViewBoundsInHandView.getMinZ());
            translateTransition1.setToX(destination.getX());
            translateTransition1.setToY(destination.getY());
            translateTransition1.setToZ(deckViewBoundsInHandView.getMinZ());
            translateTransition1.setCycleCount(1);

            rotateTransition.setAxis(Rotate.Z_AXIS);
            rotateTransition.setFromAngle(handViewRotate.getAngle() - 270);
            rotateTransition.setByAngle((handViewRotate.getAngle() - 270)%180);
            rotateTransition.setCycleCount(1);

            translateTransition2.setFromZ(deckViewBoundsInHandView.getMinZ());
            translateTransition2.setToZ(destination.getZ());
            translateTransition2.setCycleCount(1);
        });

        //Séquençage des animations
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(translateTransition1, rotateTransition);

        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(firstAnimation, parallelTransition, translateTransition2);

        //A la fin de l'animation on enlève les particules sur la carte
        sequentialTransition.setOnFinished(actionEvent -> {
            cardView.setMoving(false);
            removeParticlesOfCard(cardView);
        });

        return sequentialTransition;
    }
    //Etalement des playerView et dogView
    public Animation getDispatchAllViewsAnimation(){
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().add(dogView.getDispatchAnimation());

        for(PlayerView pv : playerViews)
            parallelTransition.getChildren().add(pv.getDispatchAnimation());

        return parallelTransition;
    }
    //Ajout et suppression de particules à une cardView
    public synchronized void addParticlesToCard(CardView cardView) {
        cardViewsWithParticles.add(cardView);
    }
    public synchronized void removeParticlesOfCard(CardView cardView) {
        cardViewsWithParticles.remove(cardView);
    }

    //Constitution de l'écart
    public void handleGap() {
        //On affiche l'indication
        getChildren().add(hint);

        //Il faut d'abord définir les cartes que l'on peut jeter
        ArrayList<CardView> trumps = new ArrayList<>();
        ArrayList<CardView> allowedCards = new ArrayList<>();

        int nbAllowedTrumps = defineCardsWichCanBeExcluded(trumps, allowedCards);

        ArrayList<CardView> gap = new ArrayList<>(6);


        final int[] nbTrumpPlayed = {0};
        //Pour toutes les cartes autorisées il faut définir onMouseClicked
        for (CardView cardView : allowedCards) {
            cardView.setOnMouseClicked(mouseEvent -> {
                //Si la carte était déjà sélectionnée
                if (cardView.isSelected()) {
                    if(gap.size() == 6) {
                        getChildren().remove(doneButton);
                        getChildren().add(hint);
                    }
                    if(cardView.getCard() instanceof Trump) {
                        nbTrumpPlayed[0]--;
                    }

                    gap.remove(cardView);
                    cardView.setSelect(!cardView.isSelected());
                }
                else if(gap.size() < 6) {
                    //On s'assure que le nombre d'atouts dans l'écart est respecté
                    if(!(cardView.getCard() instanceof Trump) || nbTrumpPlayed[0] < nbAllowedTrumps) {
                        gap.add(cardView);
                        cardView.setSelect(!cardView.isSelected());

                        if (gap.size() == 6) {
                            getChildren().remove(hint);
                            getChildren().add(doneButton);
                        }
                    }
                }
            });
        }
        //Une fois que l'on a cliqué sur le bouton
        doneButton.setOnAction(actionEvent2 -> {
            getChildren().remove(doneButton);

            SequentialTransition st = new SequentialTransition();
            for(CardView cardView : gap) {

                //Si c'est un atout on le déplace à la vue de tous
                if(cardView.getCard() instanceof Trump) {
                    TranslateTransition tt = new TranslateTransition(Duration.seconds(1), cardView);
                    tt.setByY(-Controller.SCREEN_HEIGHT / 2);
                    st.getChildren().add(tt);
                }

                //Les cardView disparaisse petit à petit
                FadeTransition ft = new FadeTransition(Duration.seconds(1), cardView);
                ft.setDelay(Duration.seconds(1));
                ft.setToValue(0);
                st.getChildren().add(ft);
            }

            //On supprme les cartes du model
            //On retri les cartes
            st.setOnFinished(event -> {
                gap.forEach(cardView -> board.getPlayer(0).removeCard(cardView.getCard()));
                Controller.playAnimation(getPlayerView(0).getSortAnimation());
            });

            Controller.playAnimation(st);

        });
    }
    //Définition des cartes qu'il est possible de jeter
    private int defineCardsWichCanBeExcluded(ArrayList<CardView> trumps, ArrayList<CardView> allowedCards) {
        //On exclut les bouts
        //et on exclut les rois et l'excuse
        for (CardView cardView : getPlayerView(0).getCardViews()) {
            if (cardView.getCard() instanceof Trump && cardView.getCard().getValue() != 1 && cardView.getCard().getValue() != 21) {
                trumps.add(cardView);
            }
            else if (cardView.getCard().getValue() != 14 && !(cardView.getCard() instanceof Trump) && !(cardView.getCard() instanceof Fool)) {
                allowedCards.add(cardView);
            }
        }

        int nbAllowedTrumps = 0;

        //Si aucune carte de la main n'est jouable, alors on a la possiblité de jouer ses atouts (mais ils doivent être montrés aux autres joueurs)
        if(allowedCards.size() < 6) {
            nbAllowedTrumps = 6 - allowedCards.size();
            allowedCards.addAll(trumps);
        }
        return nbAllowedTrumps;
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

    public Animation getBringDeckOnBoardAnimation() {
        return bringDeckOnBoardAnimation;
    }

    public void reset() {
        getChildren().clear();
        playerViews.clear();
        deckView = null;
        dogView = null;

        doneButton = null;
        hint = null;
        board = null;

        particlesLoop.stop();
        cardViewsWithParticles.clear();
        cardViewsWithParticles = null;
        particlesCanvas.getGraphicsContext2D().clearRect(0, 0, Controller.SCREEN_WIDTH, Controller.SCREEN_HEIGHT);
        particlesCanvas = null;
        background = null;
    }
}
