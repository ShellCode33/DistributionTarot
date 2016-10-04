package fr.iut.etu;

import java.util.ArrayList;

/**
 * Created by shellcode on 10/4/16.
 */
public class PlayerController {
    private Player playerModel;
    private ArrayList<Card> cards;

    public PlayerController() {
        cards = new ArrayList<>(15); //15 cards with 5 players
    }
}
