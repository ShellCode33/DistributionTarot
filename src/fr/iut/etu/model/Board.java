package fr.iut.etu.model;

import java.util.ArrayList;

/**
 * Created by shellcode on 10/4/16.
 */
public class Board {

    private ArrayList<Player> players = new ArrayList<>();
    private Dog dog = new Dog();
    private Deck deck = new Deck();
    private int playerCount;

    public Board(int playerCount){

        this.playerCount = playerCount;

        if(playerCount < 1)
            throw new IllegalArgumentException("playerCount should be at least 1");

        for (int i = 0; i < playerCount; i++) {
            players.add(new Player());
        }
    }

    public Player getPlayer(int i){
        if(i < 0 || i >= players.size())
            throw new IndexOutOfBoundsException();

        return players.get(i);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Dog getDog() {
        return dog;
    }

    public Deck getDeck(){
        return deck;
    }

    public int getPlayerCount() {
        return playerCount;
    }

}
