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
        cards = new ArrayList<>(15); //min 15 cards in this game
    }
}
