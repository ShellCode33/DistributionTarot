package fr.iut.etu.model;

import fr.iut.etu.model.Card;

/**
 * Created by shellcode on 9/27/16.
 */
public class Trump extends Card {

    public Trump(int value) {
        super(Type.TRUMP, value);

        if(value < 1 || value > 21)
            throw new IllegalArgumentException("Value must be between 1 and 21");

    }

}
