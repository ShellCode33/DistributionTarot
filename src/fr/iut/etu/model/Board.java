package fr.iut.etu.model;

import java.util.ArrayList;

/**
 * Created by shellcode on 10/4/16.
 */
public class Board {

    private ArrayList<Player> players = new ArrayList<>();
    private Player dealer;
    private Deck deck;

    public Board(int playerCount){

        if(playerCount < 1)
            throw new IllegalArgumentException("playerCount should be at least 1");

        for (int i = 0; i < playerCount; i++) {
            players.add(new Player());
        }

        dealer = players.get(0);
        deck = new Deck();
        deck.refill();
    }

    public Player getPlayer(int i){
        if(i < 0 || i >= players.size())
            throw new IndexOutOfBoundsException();

        return players.get(i);
    }

    public Player getDealer(){
        return dealer;
    }

    public Deck getDeck(){
        return deck;
    }
}
