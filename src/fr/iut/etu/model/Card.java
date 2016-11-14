package fr.iut.etu.model;

import java.util.Observable;

/**
 * Created by shellcode on 9/27/16.
 */
public class Card extends Observable{

    public enum Type {
        HEART,
        DIAMOND,
        CLUB,
        SPADE,
        TRUMP,
        FOOL
    }

    private Type cardType;
    private int value;
    private boolean hidden = false;

    public Card(Type cardType, int value) {
        this.cardType = cardType;
        this.value = value;

        if(cardType != Type.TRUMP && cardType != Type.FOOL && (value < 1 || value > 14)) //between ace and king
            throw new IllegalArgumentException("The value must be between 1 and 14");
    }

    public Type getType() {
        return cardType;
    }

    public int  getValue() {
        return value;
    }

    public void show(boolean vertical){
        this.hidden = false;
        setChanged();
        notifyObservers(vertical);
    }

    public void hide(boolean vertical){
        this.hidden = true;
        setChanged();
        notifyObservers(vertical);
    }

    public boolean isHidden() {
        return hidden;
    }

    @Override
    public String toString() {
        return cardType.toString() + ": " + value;
    }

    @Override
    public boolean equals(Object o) {
        return cardType == ((Card)o).getType() && value == ((Card)o).getValue();
    }
}
