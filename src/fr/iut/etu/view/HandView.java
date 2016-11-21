package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Card;
import fr.iut.etu.model.Hand;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 11/18/16.
 */
public abstract class HandView extends Group implements Observer {
    private Hand hand;

    protected static int GAP_BETWEEN_CARDS = (int) (40 * Controller.SCALE_COEFF);

    public HandView(Hand hand) {
        super();

        this.hand = hand;
        this.hand.addObserver(this);

    }

    public Animation getFlipAllCardViewsAnimation() {
        ParallelTransition pt = new ParallelTransition();

        for (Node node : getChildren()) {
            if(node instanceof CardView)
                pt.getChildren().add(((CardView)node).getFlipAnimation());
        }

        return pt;
    }

    public abstract Animation getDispatchAnimation();

    @Override
    public void update(Observable observable, Object o) {

    }

    public Animation getSortAnimation() {
        ParallelTransition pt = new ParallelTransition();

        FilteredList<Node> filteredChildren = getChildren().filtered(e -> e instanceof CardView);

        for (int i = 0; i < filteredChildren.size(); i++) {
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), filteredChildren.get(i));
            translateTransition.setToX(i*GAP_BETWEEN_CARDS -  filteredChildren.size()*GAP_BETWEEN_CARDS/2);
            translateTransition.setToZ(-1-i* Controller.CARD_THICK);
            translateTransition.setCycleCount(1);
            pt.getChildren().add(translateTransition);
        }

        return pt;
    }

    public void sort() {
        ObservableList<Node> workingCollection = FXCollections.observableArrayList(getChildren());
        Collections.sort(workingCollection, HandView::compareTo);
        getChildren().setAll(workingCollection);
    }

    private static int compareTo(Node node, Node node1) {
        if(!(node instanceof CardView))
            return 0;

        if(!(node1 instanceof CardView))
            return 0;

        return ((CardView)node).compareTo((CardView)node1);
    }
}
