package fr.iut.etu.model;

import java.util.Observable;

/**
 * Created by shellcode on 9/27/16.
 */
public class Card extends Observable implements Comparable<Card> {

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

    @Override
    public int compareTo(Card card) {
        int value_other = card.getValue();
        Type other_type = card.getType();

        if(other_type == Type.HEART)
            value_other += 14;

        else if(other_type == Type.TRUMP || other_type == Type.FOOL)
            value_other += 28;

        else if(other_type == Type.DIAMOND)
            value_other += 50;

        else if(other_type == Type.CLUB)
            value_other += 64;

        int my_value = getValue();
        Type my_type = getType();

        if(my_type == Type.HEART)
            my_value += 14;

        else if(my_type == Type.TRUMP || my_type == Type.FOOL)
            my_value += 28;

        else if(my_type == Type.DIAMOND)
            my_value += 50;

        else if(my_type == Type.CLUB)
            my_value += 64;

        return my_value-value_other;
    }
}
