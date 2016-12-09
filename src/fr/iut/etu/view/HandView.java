package fr.iut.etu.view;

import fr.iut.etu.Controller;
import fr.iut.etu.model.Hand;
import fr.iut.etu.model.Notifications;
import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sylvain DUPOUY on 11/18/16.
 */
public abstract class HandView extends Group implements Observer {
    protected final Hand hand;
    protected final ArrayList<CardView> cardViews = new ArrayList<>();
    private final LinkedList<CardView> cardViewsWaitingToBeAdded = new LinkedList<>();

    static final int GAP_BETWEEN_CARDS = (int) (40 * Controller.SCALE_COEFF);

    HandView(Hand hand) {
        super();

        this.hand = hand;
        this.hand.addObserver(this);
    }
    //L'animation d'étalement et de retournement des cartes seront différentes
    // suivant s'il s'agit d'une playerView ou d'une dogView
    public abstract Animation dispatchAllCardViews();
    public abstract Animation flipAllCardViews();

    @Override
    public void update(Observable observable, Object o) {
        if(o == Notifications.CARD_ADDED) {
            //Si une carte a été distribuée dans le model alors on sait qu'une cardView qui y correspond
            //peut être amenée à être distribuée dans la view
            CardView cardView = new CardView(hand.getLastCardAdded());
            cardViewsWaitingToBeAdded.push(cardView);
            CardView.getAllCardViewsDealt().add(cardView);
        }
        else if(o == Notifications.CARD_TRANSFERED){
            //Si on a transféré une carte depuis la main correspondant à cette vue alors on sait que
            //la cardView y correspondant peut être amenée à être transférée dans cette view aussi
            CardView cardView = null;
            for (CardView cv : CardView.getAllCardViewsDealt()) {
                if(cv.getCard() == hand.getLastCardTransfered()){
                    cardView = cv;
                    break;
                }
            }

            cardViewsWaitingToBeAdded.push(cardView);
        }
        else if(o == Notifications.CARD_DELETED){
            //Si une carte a été supprimée dans le model alors on peut directement la supprimer
            //dans la view
            CardView cardView = null;

            for (CardView c : cardViews) {
                if (c.getCard() == hand.getLastCardRemoved()) {
                    cardView = c;
                    break;
                }
            }

            CardView.getAllCardViewsDealt().remove(cardView);
            cardViews.remove(cardView);
            getChildren().remove(cardView);
        }
    }

    //Animation de transfer d'une carte d'une handView à une autre
    public Animation transferCardViewTo(HandView handView) {

        if(handView.cardViewsWaitingToBeAdded.isEmpty())
            throw new UnsupportedOperationException("No card was transfered to the model of this handview in the model");

        ParallelTransition pt = new ParallelTransition();
        Parent parent = getParent();

        CardView cardView = handView.cardViewsWaitingToBeAdded.poll();
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), cardView);

        //Cette animation ne sert qu'à avoir un événement onStart
        Transition firstAnimation = new Transition() {@Override protected void interpolate(double frac) {}};
        firstAnimation.setOnFinished(event -> {
            //Ajout des particules
            handView.addCardView(cardView);
            cardViews.remove(cardView);

            if(parent instanceof BoardView)
                ((BoardView) parent).addParticlesToCard(cardView);

            cardView.setMoving(true);
        });

        Bounds boundsInParent = cardView.getBoundsInParent();
        Bounds bounds = handView.parentToLocal(localToParent(boundsInParent));

        tt.setFromX(bounds.getMinX());
        tt.setFromY(bounds.getMinY());
        tt.setToX(0);
        tt.setToY(0);
        tt.setToZ(-0.2);

        pt.getChildren().add(tt);

        tt.setOnFinished(actionEvent -> {
            //On enlève les partiicules à la fin
            if(parent instanceof BoardView)
                ((BoardView) parent).removeParticlesOfCard(cardView);

            cardView.setMoving(false);
        });

        SequentialTransition st = new SequentialTransition();
        st.getChildren().addAll(firstAnimation, tt);

        return st;
    }

    public void addCardView(CardView cardView) {
        getChildren().add(cardView);
        cardViews.add(cardView);
    }

    public ArrayList<CardView> getCardViews() {
        return cardViews;
    }

    public LinkedList<CardView> getCardViewsWaitingToBeAdded() {
        return cardViewsWaitingToBeAdded;
    }
}
