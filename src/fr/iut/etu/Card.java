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
    }


    @Override
    public String toString() {

        String name;

        switch(cardType) {
            case HEART:
                name = "HEART";
                break;
            case TILE:
                name = "TILE";
                break;
            case CLOVER:
                name = "CLOVER";
                break;
            case PIKE:
                name = "PIKE";
                break;
            case TRUMP:
                name = "TRUMP";
                break;
            default:
                name = "Unknown";
                break;
        }

        return name + ": " + value;
    }
}
