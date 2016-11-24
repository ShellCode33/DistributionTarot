package fr.iut.etu.model;

import java.util.ArrayList;

/**
 * Created by shellcode on 10/4/16.
 */
public class Board {

    private final ArrayList<Player> players = new ArrayList<>();
    private final Hand dog = new Hand();
    private final Deck deck = new Deck();

    public Board(int playerCount){

        if(playerCount < 3 || playerCount > 5)
            throw new IllegalArgumentException("playerCount should be between 3 and 5");

        for(int i = 0; i < playerCount; i++)
            players.add(new Player());

    }

    public Player getPlayer(int i){
        if(i < 0 || i >= players.size())
            throw new IndexOutOfBoundsException();

        return players.get(i);
    }

    public Hand getDog() {
        return dog;
    }

    public Deck getDeck(){
        return deck;
    }

    public int getPlayerCount() {
        return players.size();
    }

}
