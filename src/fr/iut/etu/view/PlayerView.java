package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Notifications;
import fr.iut.etu.model.Player;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Observable;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class PlayerView extends HandView {

    HBox header;
    Label usernameLabel;
    Player player;

    public PlayerView(Player player) {
        super(player);
        this.player = player;

        header = new HBox();
        header.setSpacing(20);
        header.setTranslateY(-75);

        usernameLabel = new Label();
        usernameLabel.setText(player.getName());
        usernameLabel.setTextFill(Color.WHITE);
        usernameLabel.setFont(new Font(30 * Controller.SCALE_COEFF));

        header.getChildren().add(usernameLabel);
        header.setTranslateX(-50 * Controller.SCALE_COEFF / 2);
        getChildren().add(header);

    }

    @Override
    public Animation getDispatchAnimation() {

        ParallelTransition pt = new ParallelTransition();

        for (int i = cardViews.size() - 1; i >= 0; i--) {

            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), cardViews.get(i));
            translateTransition.setByX(-i*GAP_BETWEEN_CARDS +  cardViews.size()*GAP_BETWEEN_CARDS/2);
            translateTransition.setToZ(-1 -i*0.1);
            translateTransition.setCycleCount(1);

            pt.getChildren().add(translateTransition);
        }

        return pt;
    }

    public void setAvatar(Image img) {
        ImageView avatar = new ImageView(img);
        avatar.setFitHeight(50 * Controller.SCALE_COEFF);
        avatar.setFitWidth(50 * Controller.SCALE_COEFF);
        header.getChildren().setAll(avatar, usernameLabel);
    }

    @Override
    public void update(Observable observable, Object o) {
        super.update(observable, o);

        if(o == Notifications.USERNAME_CHANGED)
            usernameLabel.setText(player.getName());
    }
}
