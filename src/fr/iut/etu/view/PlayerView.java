package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Card;
import fr.iut.etu.model.Notifications;
import fr.iut.etu.model.Player;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class PlayerView extends Group implements Observer {

    private static int GAP_BETWEEN_CARDS = (int)(40 * Controller.SCALE_COEFF);

    private Player player;

    private ArrayList<CardView> cardViews = new ArrayList<>();

    public PlayerView(Player player) {
        super();

        this.player = player;
        this.player.addObserver(this);

        ImageView avatar = new ImageView(player.getAvatar());
        avatar.setFitHeight(50*Controller.SCALE_COEFF);
        avatar.setFitWidth(50*Controller.SCALE_COEFF);
        avatar.setTranslateY(-75);
        avatar.setTranslateZ(-1);

        Label usernameLabel = new Label();
        usernameLabel.setText(player.getName());
        usernameLabel.setTranslateY(-68);
        usernameLabel.setTranslateX(55);
        usernameLabel.setTranslateZ(-1);
        usernameLabel.setTextFill(Color.WHITE);
        usernameLabel.setFont(new Font(30*Controller.SCALE_COEFF));

        getChildren().add(avatar);
        getChildren().add(usernameLabel);
    }

    @Override
    public void update(Observable observable, Object o) {
        if(o != null && o == Notifications.CARD_PICKED){
            ArrayList<Card> cards = player.getCards();

            CardView cardView = new CardView(cards.get(cards.size()-1));
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
            double x = 0;
            for(int i = 1; i < cards.size(); i++)
                x += GAP_BETWEEN_CARDS;

            Point3D destination = new Point3D(x, 0 , -Controller.CARD_THICK-0.1*cardViews.size());

            //Détermination de la rotation de la playerView actuelle
            FilteredList<Transform> rotates = getTransforms().filtered(r -> r instanceof Rotate);
            Rotate rotate;
            if(rotates.size() > 0)
                rotate = (Rotate) rotates.get(0);
            else
                rotate = new Rotate(0);

            //Animation
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), cardView);
            translateTransition.setFromX(deckViewCenterTop.getX() - cardViewCenter.getX());
            translateTransition.setFromY(deckViewCenterTop.getY() - cardViewCenter.getY());
            translateTransition.setFromZ(deckViewCenterTop.getZ());
            translateTransition.setToX(destination.getX());
            translateTransition.setToY(destination.getY());
            translateTransition.setToZ(destination.getZ());
            translateTransition.setCycleCount(1);

            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), cardView);
            rotateTransition.setFromAngle(270 - rotate.getAngle());
            rotateTransition.setByAngle(270 - rotate.getAngle());
            rotateTransition.setCycleCount(1);

            translateTransition.play();
            rotateTransition.play();


            //Recentre la playerView


        }
    }

    public int getWidth() {
        return GAP_BETWEEN_CARDS*17 + Controller.CARD_WIDTH;
    }
}
