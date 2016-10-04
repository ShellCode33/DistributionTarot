package fr.iut.etu;

/**
 * Created by shellcode on 9/27/16.
 */
public class Trump extends Card {

    public Trump(int value) {
        super(Type.TRUMP, value);

        if(value < 1 || value > 21)
            throw new IllegalStateException("Value must be between 1 and 21");

    }

}
