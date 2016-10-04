package fr.iut.etu;

/**
 * Created by shellcode on 9/27/16.
 */
public class Card {

    public enum Type {
        HEART,
        TILE,
        CLOVER,
        PIKE,
        TRUMP
    }

    Type cardType;
    int value;

    public Card(Type cardType, int value) {
        this.cardType = cardType;
        this.value = value;

        if(value < 1 || value > 14) //entre as et roi
            throw new IllegalStateException("La valeur doit être entre 1 et 14");
    }

    @Override
    public String toString() {
        return cardType.toString() + ": " + value;
    }

    public Type getType() {
        return cardType;
    }

    public int  getValue() {
        return value;
    }
}
