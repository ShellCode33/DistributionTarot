package fr.iut.etu.view;

import fr.iut.etu.model.Hand;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
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

        for (int i = cardViews.size() - 1; i >= 0; i--) {

            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), cardViews.get(i));
            translateTransition.setByX(-i*CardView.CARD_WIDTH-i*GAP_BETWEEN_CARDS / 2);
            translateTransition.setToZ(-1);
            translateTransition.setCycleCount(1);

            pt.getChildren().add(translateTransition);

        }

        return pt;
    }

    @Override
    public Animation getFlipAllCardViewsAnimation() {
        ParallelTransition pt = new ParallelTransition();

        for (int i =0; i < cardViews.size(); i++) {
            Animation flipAnimation = cardViews.get(i).getFlipAnimation();
            flipAnimation.setDelay(Duration.millis(i*300));

            pt.getChildren().add(flipAnimation);
        }

        return pt;
    }
}
