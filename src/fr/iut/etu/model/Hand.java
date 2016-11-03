package fr.iut.etu.model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Sylvain DUPOUY on 03/11/16.
 */
public class Hand extends Observable{

    private ArrayList<Card> cards = new ArrayList<>();

    public void addCard(Card card){
        cards.add(card);
    }

    public void removeCard(Card card){
        cards.remove(card);
    }

    public int getCardCount(){
        return cards.size();
    }

    public ArrayList<Card> getCards(){
        return cards;
    }
}
