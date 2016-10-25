package fr.iut.etu.model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by shellcode on 10/4/16.
 */
public class Player extends Observable{

    private String name;
    private ArrayList<Card> cards = new ArrayList<>();

    public Player(){
        this.name = "";
    }

    public Player(String name) {
        this.name = name;
    }

    public void addCard(Card card) {
        cards.add(card);

        setChanged();
        notifyObservers();
    }

    public int getCardCount(){
        return cards.size();
    }

    public ArrayList<Card> getCards(){
        return cards;
    }

    public String getName(){
        return name;
    }
}
