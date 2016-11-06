package fr.iut.etu.model;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Observable;

/**
 * Created by Sylvain DUPOUY on 03/11/16.
 */
public class Hand extends Observable{

    private ArrayList<Card> cards = new ArrayList<>();

    public int getCardCount(){
        return cards.size();
    }

    public ArrayList<Card> getCards(){
        return cards;
    }

    public void pickACard(Deck deck) {

        try {
            cards.add(deck.deal());
            setChanged();
            notifyObservers(Notifications.CARD_PICKED);
        }catch (NoSuchElementException e){
            e.printStackTrace();
        }
    }
}
