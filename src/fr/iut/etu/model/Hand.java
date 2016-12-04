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

    public static void transferCard(Hand source, Hand destination, Card card) throws InvalidObjectException {

        if(!source.cards.contains(card))
            throw new InvalidObjectException("Source hand doesn't contains this card !");

        destination.cards.add(card);
        destination.lastCardTransfered = card;
        source.cards.remove(card);

        source.setChanged();
        destination.setChanged();

        destination.notifyObservers(Notifications.CARD_TRANSFERED);
    }

    public void transferCardTo(Hand hand, Card card) throws InvalidObjectException {
        if(!cards.contains(card))
            throw new InvalidObjectException("Source hand doesn't contains this card !");

        hand.cards.add(card);
        lastCardTransfered = card;
        cards.remove(card);


        setChanged();
        notifyObservers(Notifications.CARD_TRANSFERED);

    }
}
