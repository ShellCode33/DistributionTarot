package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Player;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class PlayerView extends HandView {

    private static int GAP_BETWEEN_CARDS = (int) (40 * Controller.SCALE_COEFF);


    private Label usernameLabel = new Label();

    public PlayerView(Player player) {
        super(player);

//        ImageView avatar = new ImageView(player.getAvatar());
//        avatar.setFitHeight(50 * Controller.SCALE_COEFF);
//        avatar.setFitWidth(50 * Controller.SCALE_COEFF);
//        avatar.setTranslateY(-75);
//        avatar.setTranslateZ(-1);
//        getChildren().add(avatar);

        usernameLabel.setText(player.getName());
        usernameLabel.setTranslateY(-68);
        usernameLabel.setTranslateX(55);
        usernameLabel.setTranslateZ(-1);
        usernameLabel.setTextFill(Color.WHITE);
        usernameLabel.setFont(new Font(30 * Controller.SCALE_COEFF));

        getChildren().add(usernameLabel);
    }

    public Animation getRecenterCardViewsAnimation(){

        ParallelTransition pt = new ParallelTransition();

        return pt;
    }
}
