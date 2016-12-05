package fr.iut.etu.view;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import fr.iut.etu.Controller;
import fr.iut.etu.model.Board;
import fr.iut.etu.model.Fool;
import fr.iut.etu.model.Trump;
import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
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
    private Animation bringDeckOnBoardAnimation;
    private Button doneButton;
    private Label hint;
    private Board board;

    public BoardView(Board board, Image backgroundCustom) {
        super();

        this.board = board;

        ImageView backgroundCustomView = new ImageView(backgroundCustom == null ? new Image("file:res/backgrounds/background_board0.jpg") : backgroundCustom);

        backgroundCustomView.setFitWidth(Controller.SCREEN_WIDTH);
        backgroundCustomView.setFitHeight(Controller.SCREEN_HEIGHT);
        getChildren().add(backgroundCustomView);

        for (int i = 0; i < board.getPlayerCount(); i++) {
            PlayerView playerView = new PlayerView(board.getPlayer(i));
            playerViews.add(playerView);
            getChildren().add(playerView);
        }

        doneButton = new Button();
        doneButton.setText("Done");
        doneButton.setFont(new Font(30*Controller.SCALE_COEFF));
        doneButton.getStylesheets().add("file:res/style.css");
        doneButton.getStyleClass().add("button");
        doneButton.setMaxWidth(Controller.SCREEN_WIDTH / 5);
        doneButton.setPrefWidth(Controller.SCREEN_WIDTH / 5);
        doneButton.setMaxHeight(Controller.SCREEN_HEIGHT / 12);
        doneButton.setPrefHeight(Controller.SCREEN_HEIGHT / 12);
        doneButton.setTranslateX((Controller.SCREEN_WIDTH - Controller.SCREEN_WIDTH / 5) / 2);
        doneButton.setTranslateY((Controller.SCREEN_HEIGHT - Controller.SCREEN_HEIGHT / 12) / 2);
        doneButton.setTranslateZ(-1);

        hint = new Label();
        hint.setFont(new Font(30*Controller.SCALE_COEFF));
        hint.setTextFill(Color.WHITE);
        hint.setText("Please choose 6 cards to exclude");
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader(); //Utilisé pour déterminer la taille du label
        hint.setTranslateX((Controller.SCREEN_WIDTH - fontLoader.computeStringWidth(hint.getText(), hint.getFont())) / 2);
        hint.setTranslateY((Controller.SCREEN_HEIGHT - hint.getHeight()) / 2);
        hint.setTranslateZ(-1);


        deckView = new DeckView(board.getDeck());
        getChildren().add(deckView);
        dogView = new DogView(board.getDog());
        getChildren().add(dogView);

        placeHandViews();
        createBringDeckOnBoardAnimation();


    }

    private void placeHandViews(){

        dogView.getTransforms().addAll(
                new Translate(
                        4*Controller.SCREEN_WIDTH/6,
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

    public Animation getDealACardAnimation(HandView handView) {

        if(handView.getCardViewsWaitingToBeDealt().isEmpty())
            throw new UnsupportedOperationException("No card was dealt in the model");

        CardView cardView = handView.getCardViewsWaitingToBeDealt().poll();

        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), cardView);
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), cardView);
        TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.3), cardView);

        Transition firstAnimation = new Transition() {@Override protected void interpolate(double frac) {}};
        firstAnimation.setOnFinished(event -> {

            handView.addCardView(cardView);
            deckView.removeImageViewOnTop();

            Rotate handViewRotate = (Rotate) handView.getTransforms().get(1);
            Bounds deckViewBoundsInHandView = handView.parentToLocal(deckView.getBoundsInParent());

            Point3D destination = new Point3D(0,0,-handView.getCardViews().size()*CardView.CARD_THICK-1);

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

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(translateTransition1, rotateTransition);

        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(firstAnimation, parallelTransition, translateTransition2);

        return sequentialTransition;
    }

    public void handleGap() { //Gestion de l'écart
        getChildren().add(hint);

        ArrayList<CardView> trumps = new ArrayList<>();
        ArrayList<CardView> allowed_cards = new ArrayList<>();

        for (CardView cardView : getPlayerView(0).getCardViews())
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

        doneButton.setOnAction(actionEvent2 -> {
            System.out.println("6 cards will be removed");

            getChildren().remove(doneButton);

            ParallelTransition pt = new ParallelTransition();

            for(CardView cardView : gap) {
                SequentialTransition st = new SequentialTransition();

                if(cardView.getCard() instanceof Trump) {
                    TranslateTransition tt = new TranslateTransition(Duration.seconds(1), cardView);
                    tt.setByY(-Controller.SCREEN_HEIGHT / 2);
                    st.getChildren().add(tt);
                }

                FadeTransition ft = new FadeTransition(Duration.seconds(1), cardView);
                ft.setDelay(Duration.seconds(1));
                ft.setToValue(0);
                st.getChildren().add(ft);
                pt.getChildren().add(st);
            }

            pt.setOnFinished(workerStateEvent -> {
                gap.forEach(cardView -> board.getPlayer(0).removeCard(cardView.getCard()));
                getPlayerView(0).getSortAnimation().play();
            });

            pt.play();
        });

        final int[] nb_trump_played = {0};

        int finalNb_allowed_trumps = nb_allowed_trumps;
        for (CardView cardView : allowed_cards) {
            cardView.setOnMouseClicked(mouseEvent -> {

                if (cardView.isSelected()) {

                    if(gap.size() == 6) {
                        getChildren().remove(doneButton);
                        getChildren().add(hint);
                    }

                    if(cardView.getCard() instanceof Trump)
                        nb_trump_played[0]--;

                    gap.remove(cardView);
                    cardView.setSelect(!cardView.isSelected());


                }

                else if(gap.size() < 6) {

                    //On s'assure que le nombre d'atouts dans l'écart est respecté
                    if(!(cardView.getCard() instanceof Trump) || nb_trump_played[0] < finalNb_allowed_trumps) {
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
    }

    public Animation getTransferAnimation(HandView src, HandView dest) {

        return null;
    }
}
