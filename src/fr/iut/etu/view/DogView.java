package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Card;
import fr.iut.etu.model.Hand;
import fr.iut.etu.model.Notifications;
import javafx.animation.*;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;

/**
 * Created by shellcode on 11/14/16.
 */
public class DogView extends Group implements Observer {

    Hand dog;
    private PriorityQueue<CardView> cardViewsWaiting = new PriorityQueue<CardView>();
    private ArrayList<CardView> cardViews = new ArrayList<>();

    public DogView(Hand dog) {
        this.dog = dog;
        dog.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o != null && o == Notifications.CARD_PICKED) {
            ArrayList<Card> cards = dog.getCards();

            CardView cardView = new CardView(cards.get(cards.size() - 1), true);
            cardViewsWaiting.add(cardView);
        }
    }

    public Animation getDispatchAnimation(){

        ParallelTransition pt = new ParallelTransition();

        for (int i = cardViews.size() - 1; i >= 0; i--) {

                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), cardViews.get(i));
                translateTransition.setByX(-i*(Controller.CARD_WIDTH-20));
                translateTransition.setCycleCount(1);

                pt.getChildren().add(translateTransition);

        }

        return pt;
    }

    public Animation getPickACardFromDeckAnimation(){
        SequentialTransition st = new SequentialTransition();

        CardView cardView = cardViewsWaiting.poll();
        cardViews.add(cardView);
        getChildren().add(cardView);

        //On récupère la position du centre dessus du deckview dans le référentiel de la playerView
        DeckView deckView = ((BoardView) getParent()).getDeckView();
        Point3D deckViewCenterTopInParent = deckView.localToParent(deckView.getBoundsInLocal().getWidth() / 2, deckView.getBoundsInLocal().getHeight() / 2, -deckView.getBoundsInLocal().getDepth());
        Point3D deckViewCenterTop = parentToLocal(deckViewCenterTopInParent);

        //On récupère la position du centre de la carte dans le référentiel de la playerView
        Bounds cardViewBoundsInLocal = cardView.getBoundsInLocal();
        Point2D cardViewCenter = cardView.localToParent(cardViewBoundsInLocal.getWidth() / 2, cardViewBoundsInLocal.getHeight() / 2);

        //Détermination de la position finale de la carte
        Point3D destination = new Point3D(0, 0, -Controller.CARD_THICK - 0.1 * cardViews.size());

        //Détermination de la rotation de la playerView actuelle
        FilteredList<Transform> rotates = getTransforms().filtered(r -> r instanceof Rotate);
        Rotate rotate;
        if (rotates.size() > 0)
            rotate = (Rotate) rotates.get(0);
        else
            rotate = new Rotate(0);

        //création de l'animation
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), cardView);
        translateTransition.setFromX(deckViewCenterTop.getX() - cardViewCenter.getX());
        translateTransition.setFromY(deckViewCenterTop.getY() - cardViewCenter.getY());
        translateTransition.setFromZ(deckViewCenterTop.getZ());
        translateTransition.setToX(destination.getX());
        translateTransition.setToY(destination.getY());
        translateTransition.setToZ(destination.getZ());
        translateTransition.setCycleCount(1);

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), cardView);
        rotateTransition.setAxis(Rotate.Z_AXIS);
        rotateTransition.setFromAngle(270 - rotate.getAngle());
        rotateTransition.setByAngle(270 - rotate.getAngle());
        rotateTransition.setCycleCount(1);

        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(translateTransition, rotateTransition);

        st.getChildren().addAll(pt);

        st.setOnFinished(event ->{deckView.getChildren().remove(deckView.getChildren().size() - 1);});

        return st;
    }

    public Animation getFlipAllCardViewsAnimation() {

        SequentialTransition st = new SequentialTransition();

        for (Node node : getChildren()) {
            if(node instanceof CardView){
                st.getChildren().add(((CardView) node).getFlipAnimation());
            }
        }

        return st;
    }
}
