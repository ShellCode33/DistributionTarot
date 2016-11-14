package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Deck;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class DeckView extends Group implements Observer {

    private Deck deck;
    private Image image;
    private ArrayList<ImageView> images = new ArrayList<>();

    public DeckView(Deck deck) {
        super();

        this.deck = deck;
        deck.addObserver(this);

        image = new Image("file:res/back.jpg");

        update(deck, null);
    }

    @Override
    public void update(Observable observable, Object o) {

        int childrenCount = getChildren().size();
        int deckSize = deck.size();

        if(childrenCount < deckSize){
            for(int i = childrenCount; i < deckSize; i++) {
                ImageView view = new ImageView(image);
                view.setTranslateZ(-Controller.CARD_THICK*78-i*Controller.CARD_THICK);
                view.setScaleX(Controller.SCALE_COEFF);
                view.setScaleY(Controller.SCALE_COEFF);
                view.setFitWidth(Controller.CARD_WIDTH);
                view.setFitHeight(Controller.CARD_HEIGHT);
                images.add(view);
                getChildren().add(view);
            }
        }
        else {
            for (int i = childrenCount; i > deckSize; i--)
                getChildren().remove(0);
        }

        Tooltip.install(this, new Tooltip(this.deck.size() + " cards!"));
    }

    public void cutAnimation() {

        RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), this);

        rotate.setAxis(Rotate.Z_AXIS);
        rotate.setFromAngle(270);
        rotate.setToAngle(360);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setCycleCount(1);
        rotate.play();

        rotate.setOnFinished(actionEvent -> {

            Timeline timeline = null;

            for(int i = 0; i < deck.size()/2; i++) {
                timeline = new Timeline();

                KeyFrame cut = new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(getChildren().get(deck.size()-1-i).translateXProperty(), Controller.CARD_WIDTH/2*Controller.SCALE_COEFF+5));

                KeyFrame cut2 = new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(getChildren().get(i).translateXProperty(), -Controller.CARD_WIDTH/2*Controller.SCALE_COEFF-5));

                timeline.getKeyFrames().addAll(cut, cut2);
                timeline.setAutoReverse(true);
                timeline.play();
            }

            timeline.setOnFinished(actionEvent1 -> {

                Timeline timeline2 = null;

                for(int i = 0; i < deck.size()/2; i++) {
                    timeline2 = new Timeline();

                    KeyFrame cut = new KeyFrame(Duration.seconds(0.5),
                            new KeyValue(getChildren().get(i).translateZProperty(), -Controller.CARD_THICK*deck.size()*2-i*Controller.CARD_THICK));

                    timeline2.getKeyFrames().add(cut);
                    timeline2.play();
                }

                timeline2.setOnFinished(actionEvent2 -> {

                    Timeline timeline3 = null;

                    for(int i = 0; i < deck.size(); i++) {
                        timeline3 = new Timeline();

                        KeyFrame cut = new KeyFrame(Duration.seconds(0.5),
                                new KeyValue(getChildren().get(deck.size()-1-i).translateXProperty(), 0));

                        timeline3.getKeyFrames().add(cut);
                        timeline3.play();
                    }


                    timeline3.setOnFinished(actionEvent3 -> {
                        RotateTransition rotate2 = new RotateTransition(Duration.seconds(0.5), this);
                        rotate2.setAxis(Rotate.Z_AXIS);
                        rotate2.setFromAngle(360);
                        rotate2.setToAngle(270);
                        rotate2.setInterpolator(Interpolator.LINEAR);
                        rotate2.setCycleCount(1);
                        rotate2.play();
                    });

                });
            });

        });
    }
}
