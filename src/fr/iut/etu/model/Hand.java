package fr.iut.etu.model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Sylvain DUPOUY on 03/11/16.
 */
public class Hand extends Observable{

    private Card lastCardAdded;
    private Card lastCardRemoved;

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

    public void transferCardsTo(Hand hand) {

        for(Card card : cards) {
            hand.addCard(card);
        }

        cards.clear();
    }

    public Card getLastCardAdded() {
        return lastCardAdded;
    }

    public Card getLastCardRemoved() {
        return lastCardRemoved;
    }
}
