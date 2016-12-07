package fr.iut.etu.view;

import fr.iut.etu.layouts.Settings;
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

    public DeckView(Deck deck) {
        super();

        deck.addObserver(this);
    }

    //Animation de coupe
    public Animation getCutAnimation() {

        SequentialTransition st = new SequentialTransition();

        //On tourne le deck à 90°
        RotateTransition rt = new RotateTransition(Duration.seconds(0.5), this);
        rt.setAxis(Rotate.Z_AXIS);
        rt.setByAngle(90);

        //On va déplacer deux partitions du deck à l'opposé l'une de l'autre
        //Faire passer celle du dessous au-dessus
        //Et les re rassembler
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
                    new KeyValue(getChildren().get(i).translateZProperty(), -CardView.CARD_THICK * childrenSize - i * CardView.CARD_THICK));

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
        tt.setByZ(-CardView.CARD_THICK * childrenSize / 2);

        st.getChildren().addAll(rt, pt, timeline2, pt2, rt2, tt);

        return st;
    }
    //Retirer l'imageView du dessus du deck
    public void removeImageViewOnTop(){
        //On récupère uniquement les imageView
        FilteredList<Node> imageViews = getChildren().filtered(i -> i instanceof ImageView);
        if(imageViews.isEmpty())
            throw new NoSuchElementException("No ImageView to remove in deckView");
        //On les trie en fonction de leurs hauteurs
        ArrayList<Node> imageViewsSorted = new ArrayList<>();
        imageViewsSorted.addAll(imageViews);
        imageViewsSorted.sort((node, t1) -> (int) (node.getBoundsInParent().getMaxZ() - t1.getBoundsInParent().getMaxZ()));
        //On supprime celle tout en haut
        getChildren().remove(imageViewsSorted.get(0));
        Tooltip.install(this, new Tooltip(getChildren().size() + " cards!"));
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o == null)
            return;

        //Si on a jouté une carte dans  le model, alors on rajoute une imageView sur le dessus du deck
        if (o == Notifications.CARD_ADDED) {
            ImageView imageView = new ImageView(Settings.getBackCardImage());
            imageView.setSmooth(true);
            imageView.setFitHeight(CardView.CARD_HEIGHT);
            imageView.setFitWidth(CardView.CARD_WIDTH);
            imageView.setTranslateZ(-getChildren().size() * CardView.CARD_THICK);

            getChildren().add(imageView);
        }
    }
}
