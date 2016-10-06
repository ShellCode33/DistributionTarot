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
    public Card getCard(int i){

        if(i < 0 || i > cards.size() - 1)
            throw new IndexOutOfBoundsException("Card index doesn't exist !");

        return cards.get(i);
    }

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

    public boolean contains(Card card){ return cards.contains(card); }

    public Card deal() {
        Card card = cards.get(cards.size() - 1);
        cards.remove(card);
        return card;
    }
    
    public void cut(int i) {

        if(cards.size() < 8 || i < 4 || i > cards.size()-4)
            throw new IllegalArgumentException("You can't cut here !");

        List<Card> end = new ArrayList<Card>();
        List<Card> start = new ArrayList<Card>();

        end.addAll(cards.subList(i, this.cards.size()));
        start.addAll(cards.subList(0, i));

        cards.clear();

        cards.addAll(end);
        cards.addAll(start);
    }


}
