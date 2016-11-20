package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Deck;
import fr.iut.etu.model.Notifications;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class DeckView extends Group implements Observer {

    private Deck deck;
    private Image backCard;
    private LinkedList<CardView> cardViewsWaitingToBeDealt = new LinkedList<>();

    public DeckView(Deck deck, Image backCardCustom) {
        super();

        this.deck = deck;
        deck.addObserver(this);

        if(backCardCustom == null)
            backCard = new Image("file:res/cards/back0.jpg");
        else
            backCard = backCardCustom;
    }

    public Animation createCutAnimation() {

        SequentialTransition st = new SequentialTransition();

        RotateTransition rt = new RotateTransition(Duration.seconds(0.5), this);
        rt.setAxis(Rotate.Z_AXIS);
        rt.setByAngle(90);

        ParallelTransition pt = new ParallelTransition();
        ParallelTransition pt2 = new ParallelTransition();

        int childrenSize = getChildren().size();

        Timeline timeline = new Timeline();

        for (int i = 0; i < childrenSize / 2; i++) {

            KeyFrame cut = new KeyFrame(Duration.seconds(0.5),
                    new KeyValue(getChildren().get(i).translateXProperty(), -Controller.CARD_WIDTH / 2 - 10));

            timeline.getKeyFrames().add(cut);
        }

        pt.getChildren().add(timeline);

        timeline = new Timeline();

        for (int i = 0; i < childrenSize / 2; i++) {

            KeyFrame cut = new KeyFrame(Duration.seconds(0.5),
                    new KeyValue(getChildren().get(i).translateXProperty(), 0));

            timeline.getKeyFrames().add(cut);
        }

        pt2.getChildren().add(timeline);

        Timeline timeline2 = new Timeline();

        for (int i = 0; i < childrenSize / 2; i++) {
            KeyFrame cut = new KeyFrame(Duration.seconds(0.5),
                    new KeyValue(getChildren().get(i).translateZProperty(), 2*(-Controller.CARD_THICK * childrenSize - i * Controller.CARD_THICK)));

            timeline2.getKeyFrames().add(cut);
        }

        timeline = new Timeline();

        for (int i = 0; i < childrenSize / 2; i++) {
            KeyFrame cut = new KeyFrame(Duration.seconds(0.5),
                    new KeyValue(getChildren().get(childrenSize - 1 - i).translateXProperty(), Controller.CARD_WIDTH / 2 + 10));

            timeline.getKeyFrames().add(cut);
        }

        pt.getChildren().add(timeline);

        timeline = new Timeline();

        for (int i = 0; i < childrenSize / 2; i++) {
            KeyFrame cut = new KeyFrame(Duration.seconds(0.5),
                    new KeyValue(getChildren().get(childrenSize - 1 - i).translateXProperty(), 0));

            timeline.getKeyFrames().add(cut);
        }

        pt2.getChildren().add(timeline);

        RotateTransition rt2 = new RotateTransition(Duration.seconds(0.5), this);
        rt2.setAxis(Rotate.Z_AXIS);
        rt2.setByAngle(-90);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), this);
        tt.setByZ(Controller.CARD_THICK * childrenSize);

        st.getChildren().addAll(rt, pt, timeline2, pt2, rt2, tt);

        return st;
    }

    @Override
    public void update(Observable observable, Object o) {
        if( o == null)
            return;

        if(o == Notifications.CARD_DEALED) {
            cardViewsWaitingToBeDealt.push(new CardView(deck.getLastCardDealt(), backCard));
        }
        else if(o == Notifications.CARD_ADDED){
            ImageView imageView = new ImageView(backCard);
            imageView.setSmooth(true);
            imageView.setFitHeight(Controller.CARD_HEIGHT);
            imageView.setFitWidth(Controller.CARD_WIDTH);
            imageView.setTranslateZ(-getChildren().size()*2*Controller.CARD_THICK);

            getChildren().add(imageView);
        }

        Tooltip.install(this, new Tooltip(this.deck.size() + " cards!"));
    }

    public LinkedList<CardView> getCardViewsWaitingToBeDealt() {
        return cardViewsWaitingToBeDealt;
    }
}
