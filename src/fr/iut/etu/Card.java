package fr.iut.etu;

/**
 * Created by shellcode on 9/27/16.
 */
public class Card {

    String name; //hearts, tiles, clovers, pikes
    int value;

    public Card(String name, int value) {
        this.name = name;
        this.value = value;
    }


    @Override
    public String toString() {
        return name + ": " + value;
    }
}
