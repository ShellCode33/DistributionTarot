package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Card;
import fr.iut.etu.model.Notifications;
import fr.iut.etu.model.Player;
import javafx.animation.*;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

import java.util.*;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class PlayerView extends Group implements Observer {

    private static int GAP_BETWEEN_CARDS = (int) (40 * Controller.SCALE_COEFF);

    private Player player;

    private ArrayDeque<Card> cardWaiting = new ArrayDeque<Card>();
    private ArrayList<CardView> cardViews = new ArrayList<>();
    private Label usernameLabel = new Label();

    public PlayerView(Player player) {
        super();

        this.player = player;
        this.player.addObserver(this);

        ImageView avatar = new ImageView(player.getAvatar());
        avatar.setFitHeight(50 * Controller.SCALE_COEFF);
        avatar.setFitWidth(50 * Controller.SCALE_COEFF);
        avatar.setTranslateY(-75);
        avatar.setTranslateZ(-1);

        usernameLabel.setText(player.getName());
        usernameLabel.setTranslateY(-68);
        usernameLabel.setTranslateX(55);
        usernameLabel.setTranslateZ(-1);
        usernameLabel.setTextFill(Color.WHITE);
        usernameLabel.setFont(new Font(30 * Controller.SCALE_COEFF));

        getChildren().add(avatar);
        getChildren().add(usernameLabel);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o != null && o == Notifications.CARD_PICKED) {
            ArrayList<Card> cards = player.getCards();

            cardWaiting.addLast(cards.get(cards.size()-1));
        }
    }

    public Animation getPickACardFromDeckAnimation(){
        if(cardWaiting.isEmpty())
            return new TranslateTransition();

        SequentialTransition st = new SequentialTransition();

        CardView cardView = new CardView(cardWaiting.pollFirst(), true);
        cardViews.add(cardView);

        //On récupère la position du centre dessus du deckview dans le référentiel de la playerView
        DeckView deckView = ((BoardView) getParent()).getDeckView();
        Point3D deckViewCenterTopInParent = deckView.localToParent(deckView.getBoundsInLocal().getWidth() / 2, deckView.getBoundsInLocal().getHeight() / 2, -deckView.getBoundsInLocal().getDepth());
        Point3D deckViewCenterTop = parentToLocal(deckViewCenterTopInParent);

        //On récupère la position du centre de la carte dans le référentiel de la playerView
        Bounds cardViewBoundsInLocal = cardView.getBoundsInLocal();
        Point2D cardViewCenter = cardView.localToParent(cardViewBoundsInLocal.getWidth() / 2, cardViewBoundsInLocal.getHeight() / 2);

        //Détermination de la position finale de la carte
        Point3D destination = new Point3D(getBoundsInLocal().getMinX() + (cardViews.size() - 1)*GAP_BETWEEN_CARDS , 0, -Controller.CARD_THICK - 0.1 * cardViews.size());

        //Détermination de la rotation de la playerView actuelle
        FilteredList<Transform> rotates = getTransforms().filtered(r -> r instanceof Rotate);
        Rotate rotate;
        if (rotates.size() > 0)
            rotate = (Rotate) rotates.get(0);
        else
            rotate = new Rotate(0);

        //création de l'animation
        getChildren().add(cardView);

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

        st.getChildren().add(pt);

        st.setOnFinished(event ->{deckView.getChildren().remove(deckView.getChildren().size() - 1);});

        return st;
    }

    public Animation getRecenterCardViewsAnimation(){

        ParallelTransition pt = new ParallelTransition();

        for (Node node : getChildren()) {
            if(node instanceof CardView){
                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), node);
                translateTransition.setByX(-(cardViews.size() - 1)*(Controller.CARD_WIDTH-GAP_BETWEEN_CARDS)/2);
                translateTransition.setCycleCount(1);

                pt.getChildren().add(translateTransition);
            }
        }

        return pt;
    }

    public Animation flipAllCardViewsAnimation() {

        SequentialTransition st = new SequentialTransition();

        for (Node node : getChildren()) {
            if(node instanceof CardView){
                st.getChildren().add(((CardView) node).getFlipAnimation());
            }
        }

        return st;
    }
}
