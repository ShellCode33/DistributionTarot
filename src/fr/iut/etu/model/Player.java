package fr.iut.etu.model;

import fr.iut.etu.model.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shellcode on 10/4/16.
 */
public class Player {

    private String name;
    private int points;
    private List<Card> cards;



    public Player(String name, ArrayList<Card> cards) {
        this.name = name;
        this.cards = cards;

        points = 0;
    }

    void addCard(Card card) {
        cards.add(card);
    }


}
