package fr.iut.etu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by sdupouy002 on 04/10/16.
 */
public class Deck {

    private List<Card> cards = new ArrayList<Card>();

    public int getSize() {
        return cards.size();
    }
    public List<Card> getCards() { return cards; }

    public void refill() {
        for(int i = 1; i <= 14;i++){
            cards.add(new Card(Card.Type.TILE, i));
            cards.add(new Card(Card.Type.CLOVER, i));
            cards.add(new Card(Card.Type.HEART, i));
            cards.add(new Card(Card.Type.PIKE, i));
        }

        for(int i = 1; i <= 21; i++)
            cards.add(new Trump(i));

        cards.add(new Fool());
    }

    public void shuffle() {
        Collections.shuffle(cards, new Random(System.nanoTime()));
    }

    public boolean contains(Card card){
        return cards.contains(card);
    }
}
