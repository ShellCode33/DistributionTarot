package fr.iut.etu.model;

/**
 * Created by shellcode on 10/4/16.
 */
public class Player extends Hand{

    private String name = "";

    public Player(String name) {
        super();
        this.name = name;
    }

    public Player() {
        super();
    }

    public String getName(){
        return name;
    }
}
