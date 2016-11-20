package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Card;
import fr.iut.etu.model.Player;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class PlayerView extends HandView {

    private static int GAP_BETWEEN_CARDS = (int) (40 * Controller.SCALE_COEFF);


    private Label usernameLabel = new Label();

    public PlayerView(Player player) {
        super(player);

        usernameLabel.setText(player.getName());
        usernameLabel.setTranslateY(-68);
        usernameLabel.setTranslateX(55);
        usernameLabel.setTranslateZ(-1);
        usernameLabel.setTextFill(Color.WHITE);
        usernameLabel.setFont(new Font(30 * Controller.SCALE_COEFF));

        elementsToDraw.getChildren().add(usernameLabel);
    }

    @Override
    public Animation getDispatchAnimation() {
        ParallelTransition pt = new ParallelTransition();

        for (int i = size() - 1; i >= 0; i--) {

            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), get(i));
            translateTransition.setByX(-i*GAP_BETWEEN_CARDS +  size()*GAP_BETWEEN_CARDS/2);
            translateTransition.setCycleCount(1);

            pt.getChildren().add(translateTransition);

        }

        return pt;
    }

    public Animation getSortAnimation() {
        ParallelTransition pt = new ParallelTransition();

        for (int i = 0; i < size(); i++) {
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), get(i));
            translateTransition.setToX(i*GAP_BETWEEN_CARDS -  size()*GAP_BETWEEN_CARDS/2);
            translateTransition.setToZ(-1-i*Controller.CARD_THICK);
            translateTransition.setCycleCount(1);
            pt.getChildren().add(translateTransition);
        }

        return pt;
    }

    public void setAvatar(Image image) {
        ImageView avatar = new ImageView(image);
        avatar.setFitHeight(50 * Controller.SCALE_COEFF);
        avatar.setFitWidth(50 * Controller.SCALE_COEFF);
        avatar.setTranslateY(-75);
        avatar.setTranslateZ(-1);
        getElementsToDraw().getChildren().add(avatar);
    }
}
