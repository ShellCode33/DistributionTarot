package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Hand;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Created by shellcode on 11/14/16.
 */
public class DogView extends HandView{

    public DogView(Hand dog) {
        super(dog);
    }

    @Override
    public Animation getDispatchAnimation(){

        ParallelTransition pt = new ParallelTransition();

        FilteredList<Node> cardViews = getChildren().filtered(c -> c instanceof CardView);

        for (int i = cardViews.size() - 1; i >= 0; i--) {

                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), cardViews.get(i));
                translateTransition.setByX(-i*Controller.CARD_WIDTH);
                translateTransition.setCycleCount(1);

                pt.getChildren().add(translateTransition);

        }

        return pt;
    }
}
