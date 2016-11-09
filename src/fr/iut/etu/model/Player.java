package fr.iut.etu.model;

/**
 * Created by shellcode on 10/4/16.
 */
public class Player extends Hand{

    private String name = "";
    private static int nb_computers = 0;

    public Player() {
        this("#computer" + nb_computers++);
    }

    public Player(String name) {
        super();
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
