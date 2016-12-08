package fr.iut.etu.model;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Sylvain DUPOUY on 03/11/16.
 */
public class Hand extends Observable{

    private Card lastCardAdded;
    private Card lastCardRemoved;
    private Card lastCardTransfered;

    private final ArrayList<Card> cards = new ArrayList<>();

    public int getCardCount(){
        return cards.size();
    }

    public ArrayList<Card> getCards(){
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
        lastCardAdded = card;

        setChanged();
        notifyObservers(Notifications.CARD_ADDED);
    }

    public void removeCard(Card card) {
        cards.remove(card);
        lastCardRemoved = card;

        setChanged();
        notifyObservers(Notifications.CARD_DELETED);
    }

    public Card getLastCardAdded() {
        return lastCardAdded;
    }

    public Card getLastCardRemoved() {
        return lastCardRemoved;
    }

    public Card getLastCardTransfered() {
        return lastCardTransfered;
    }

    public void transferCardTo(Hand hand, Card card) throws InvalidObjectException {
        if(!cards.contains(card))
            throw new InvalidObjectException("Source hand doesn't contain this card !");

        hand.cards.add(card);
        hand.lastCardTransfered = card;
        cards.remove(card);


        hand.setChanged();
        hand.notifyObservers(Notifications.CARD_TRANSFERED);
    }
}
