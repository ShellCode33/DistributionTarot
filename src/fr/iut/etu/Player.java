package fr.iut.etu;

import java.util.ArrayList;

/**
 * Created by shellcode on 10/4/16.
 */
public class Player {
    String name;
    int points;
    private ArrayList<Card> cards;

    public Player() {
        this("Unknown");
    }

    public Player(String name) {
        this.name = name;
        cards = new ArrayList<>(15); //min 15 cards in this game
    }

    void addCard(Card card) {
        cards.add(card);
    }


}
