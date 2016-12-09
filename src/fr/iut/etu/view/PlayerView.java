package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Fool;
import fr.iut.etu.model.Notifications;
import fr.iut.etu.model.Player;
import fr.iut.etu.model.Trump;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Sylvain DUPOUY on 25/10/16.
 */
public class PlayerView extends HandView {

    private final HBox header;
    private ImageView avatar;
    private final Label usernameLabel;
    private final Player player;
    private final ArrayList<CardView> gap = new ArrayList<>(6);

    public PlayerView(Player player) {
        super(player);
        this.player = player;

        header = new HBox();
        header.setAlignment(Pos.CENTER);
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
    public Animation flipAllCardViews() {
        ParallelTransition pt = new ParallelTransition();

        Parent parent = getParent();
        //Pour toutes les cardViews on ajoute des particules
        for (CardView cardView : cardViews) {
            pt.getChildren().add(cardView.getFlipAnimation());

            if(parent instanceof BoardView)
                ((BoardView) parent).addParticlesToCard(cardView);

            cardView.setMoving(true);
        }
        //A la fin de l'animation, il faut penser à supprimer les particules des cardViews
        pt.setOnFinished(event2 -> {
            if(parent instanceof BoardView)
                cardViews.forEach(((BoardView) parent)::removeParticlesOfCard);

            cardViews.forEach(cardView -> cardView.setMoving(false));
        });


        return pt;
    }

    @Override
    public Animation dispatchAllCardViews() {

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

    public Animation sortCardViews() {

        ParallelTransition pt = new ParallelTransition();

        cardViews.sort(CardView::compareTo);

        for (int i = 0; i < cardViews.size(); i++) {
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), cardViews.get(i));
            translateTransition.setToX(i*GAP_BETWEEN_CARDS -  cardViews.size()*GAP_BETWEEN_CARDS/2);
            translateTransition.setToZ(-1-i* CardView.CARD_THICK);
            translateTransition.setCycleCount(1);
            pt.getChildren().add(translateTransition);
        }


        return pt;
    }

    public void setAvatar(Image img) {
        avatar = new ImageView(img);
        avatar.setFitHeight(50 * Controller.SCALE_COEFF);
        avatar.setFitWidth(50 * Controller.SCALE_COEFF);
        header.getChildren().setAll(avatar, usernameLabel);
    }

    //Constitution de l'écart
    public void handleGap(Controller controller) {

        BoardView parent = (BoardView) getParent();
        //On affiche l'indication
        parent.showHint();

        //Il faut d'abord définir les cartes que l'on peut jeter
        ArrayList<CardView> allowedCards = new ArrayList<>();
        ArrayList<CardView> notAllowedCards = new ArrayList<>();

        int nbAllowedTrumps = defineCardsWichCanBeExcluded(allowedCards);

        notAllowedCards.addAll(cardViews);
        notAllowedCards.removeAll(allowedCards);

        notAllowedCards.forEach(cardView -> {
            Tooltip.install(cardView, new Tooltip("You can't exclude this card"));
        });

        final int[] nbTrumpPlayed = {0};

        parent.setHintText("Choose 6 cards to exclude !\n" + (nbAllowedTrumps > 0 ? nbAllowedTrumps + " trumps can be excluded" : ""));
        parent.showHint();


        //Pour toutes les cartes autorisées il faut définir onMouseClicked
        for (CardView cardView : allowedCards) {
            cardView.setOnMouseClicked(mouseEvent -> {
                //Si la carte était déjà sélectionnée
                if (cardView.isSelected()) {
                    if(gap.size() == 6) {
                        parent.hideDoneButton();
                    }
                    if(cardView.getCard() instanceof Trump) {
                        nbTrumpPlayed[0]--;
                    }

                    gap.remove(cardView);
                    cardView.setSelect(!cardView.isSelected());
                }
                else if(gap.size() < 6) {
                    //On s'assure que le nombre d'atouts dans l'écart est respecté
                    if(!(cardView.getCard() instanceof Trump) || nbTrumpPlayed[0] < nbAllowedTrumps) {
                        gap.add(cardView);
                        cardView.setSelect(!cardView.isSelected());

                        if (gap.size() == 6) {
                            parent.showDoneButton();
                            parent.hideHint();
                        }
                    }
                }
            });
        }
        //Une fois que l'on a cliqué sur le bouton
        parent.getDoneButton().setOnAction(actionEvent2 -> {
            parent.hideDoneButton();

            ParallelTransition pt = new ParallelTransition();
            for (CardView cardView : gap) {

                //Si c'est un atout on le déplace à la vue de tous
                if (cardView.getCard() instanceof Trump) {
                    TranslateTransition tt = new TranslateTransition(Duration.seconds(1), cardView);
                    tt.setByY(-Controller.SCREEN_HEIGHT / 2);
                    pt.getChildren().add(tt);
                }

                //Les cardView disparaisse petit à petit
                FadeTransition ft = new FadeTransition(Duration.seconds(1), cardView);
                ft.setDelay(Duration.seconds(1));
                ft.setToValue(0);
                pt.getChildren().add(ft);
            }

            //On supprime les cartes du model
            //On retri les cartes
            pt.setOnFinished(event -> {
                controller.gapIsDone();
            });

            Controller.playAnimation(pt);

        });
    }
    //Définition des cartes qu'il est possible d'écarter
    private int defineCardsWichCanBeExcluded(ArrayList<CardView> allowedCards) {


        //On récupère les atouts qui ne sont pas les bouts
        //exclut les rois, les atouts et l'excuse des cartes qui sont autorisées à écarter

        ArrayList<CardView> allowedTrumps = new ArrayList<>();
        for (CardView cardView : cardViews) {
            if (cardView.getCard() instanceof Trump && cardView.getCard().getValue() != 1 && cardView.getCard().getValue() != 21) {
                allowedTrumps.add(cardView);
            }
            else if (cardView.getCard().getValue() != 14 && !(cardView.getCard() instanceof Trump) && !(cardView.getCard() instanceof Fool)) {
                allowedCards.add(cardView);
            }
        }

        int nbAllowedTrumps = 0;

        //Si aucune carte de la main n'est jouable, alors on a la possiblité de jouer ses atouts (mais ils doivent être montrés aux autres joueurs)
        if(allowedCards.size() < 6) {
            nbAllowedTrumps = 6 - allowedCards.size();
            allowedCards.addAll(allowedTrumps);
        }
        return nbAllowedTrumps;
    }


    @Override
    public void update(Observable observable, Object o) {
        super.update(observable, o);

        if(o == Notifications.USERNAME_CHANGED)
            usernameLabel.setText(player.getName());
    }

    public ArrayList<CardView> getGap() {
        return gap;
    }

    public ImageView getAvatar() {
        return avatar;
    }
}
