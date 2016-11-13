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

        if(playerCount < 3 || playerCount > 5)
            throw new IllegalArgumentException("playerCount should be between 3 and 5");
    }

    public void addPlayer(Player player) {

        if(players.size() == 5)
            throw new IllegalArgumentException("playerCount should be between 3 and 5");

        players.add(player);
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
