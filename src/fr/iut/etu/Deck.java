package fr.iut.etu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdupouy002 on 04/10/16.
 */
public class Deck {

    private int size = 0;
    private List<Card> cards = new ArrayList<Card>();

    public int getSize() {
        return size;
    }

    public void refill() {
        for(int i = 1; i <= 14;i++){
            addCard(new Card(Card.Type.TILE, i));
            addCard(new Card(Card.Type.CLOVER, i));
            addCard(new Card(Card.Type.HEART, i));
            addCard(new Card(Card.Type.PIKE, i));
        }

        for(int i = 1; i <= 21; i++)
            cards.add(new Trump(i));

        cards.add(new Fool());
    }

    private void addCard(Card card){
        cards.add(card);
        size++;
    }
}
