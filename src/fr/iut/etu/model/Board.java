package fr.iut.etu.model;

import java.util.ArrayList;

/**
 * Created by shellcode on 10/4/16.
 */
public class Board {

    private ArrayList<Player> players = new ArrayList<>();
    private Hand dog = new Hand();
    private Deck deck = new Deck();

    //Même si le jeu n'est qu'à 4 joueurs, le modèle a été partiellement prévu pour une potentielle évolution à plusieurs joueurs
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

    public void reset() {
        players.clear();
        players = null;
        dog = null;
        deck = null;
    }

    //Retourne true si il y a petit sec
    public boolean checkPetitSec() {

        int sum = 0;

        for(Player player : players) {
            for (Card card : player.getCards())
                if (card instanceof Trump || card instanceof Fool)
                    sum += card.getValue();

            if(sum == 1) //petit sec
                return true;

            sum = 0;
        }

        return false;
    }
}
