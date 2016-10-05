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
        TRUMP,
        FOOL
    }

    Type cardType;
    int value;

    public Card(Type cardType, int value) {
        this.cardType = cardType;
        this.value = value;

        if(cardType != Type.TRUMP && cardType != Type.TILE.FOOL && (value < 1 || value > 14)) //between ace and king
            throw new IllegalArgumentException("The value must be between 1 and 14");
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

    @Override
    public boolean equals(Object o) {
        return cardType == ((Card)o).getType() && value == ((Card)o).getValue();
    }
}
