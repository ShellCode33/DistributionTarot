package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Deck;
import fr.iut.etu.model.Notifications;
import javafx.animation.*;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class DeckView extends Group implements Observer {

    private Deck deck;

    public DeckView(Deck deck) {
        super();

        this.deck = deck;
        deck.addObserver(this);
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
                    new KeyValue(getChildren().get(i).translateXProperty(), -CardView.CARD_WIDTH / 2 - 10));

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
                    new KeyValue(getChildren().get(i).translateZProperty(), -Controller.CARD_THICK * childrenSize - i * Controller.CARD_THICK));

            timeline2.getKeyFrames().add(cut);
        }

        timeline = new Timeline();

        for (int i = 0; i < childrenSize / 2; i++) {
            KeyFrame cut = new KeyFrame(Duration.seconds(0.5),
                    new KeyValue(getChildren().get(childrenSize - 1 - i).translateXProperty(), CardView.CARD_WIDTH / 2 + 10));

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
        tt.setToZ(-Controller.CARD_THICK * childrenSize);

        st.getChildren().addAll(rt, pt, timeline2, pt2, rt2, tt);

        return st;
    }

    public void removeImageViewOnTop(){
        FilteredList<Node> imageViews = getChildren().filtered(i -> i instanceof ImageView);
        if(imageViews.isEmpty())
            throw new NoSuchElementException("No ImageView to remove in deckView");

        ArrayList<Node> imageViewsSorted = new ArrayList<>();
        imageViewsSorted.addAll(imageViews);
        imageViewsSorted.sort((node, t1) -> (int) (node.getBoundsInParent().getMaxZ() - t1.getBoundsInParent().getMaxZ()));

        getChildren().remove(imageViewsSorted.get(0));
        Tooltip.install(this, new Tooltip(getChildren().size() + " cards!"));
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o == null)
            return;

        if (o == Notifications.CARD_ADDED) {
            ImageView imageView = new ImageView(CardView.backCard);
            imageView.setSmooth(true);
            imageView.setFitHeight(CardView.CARD_HEIGHT);
            imageView.setFitWidth(CardView.CARD_WIDTH);
            imageView.setTranslateZ(-getChildren().size() * Controller.CARD_THICK);

            getChildren().add(imageView);
        }
    }
}
