package fr.iut.etu.view;

import fr.iut.etu.Controller;
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

        getChildren().add(usernameLabel);
    }

    @Override
    public Animation getDispatchAnimation() {
        ParallelTransition pt = new ParallelTransition();

        FilteredList<Node> cardViews = getChildren().filtered(c -> c instanceof CardView);

        for (int i = cardViews.size() - 1; i >= 0; i--) {

            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), cardViews.get(i));
            translateTransition.setByX(-i*GAP_BETWEEN_CARDS +  cardViews.size()*GAP_BETWEEN_CARDS/2);
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
        getChildren().add(avatar);
    }
}
