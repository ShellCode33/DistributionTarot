package fr.iut.etu.model;

import java.util.*;

/**
 * Created by Sylvain DUPOUY on 04/10/16.
 */
public class Deck extends Observable{

    private List<Card> cards = new ArrayList<Card>();
    private Card lastCardDealt;
    private Card lastCardAdded;

    public int size() {
        return cards.size();
    }
    public boolean contains(Card card){
        return cards.contains(card);
    }
    public List<Card> getCards() {
        return cards;
    }
    public Card getCard(int i){

        if(i < 0 || i > cards.size() - 1)
            throw new IndexOutOfBoundsException("Card index doesn't exist !");

        return cards.get(i);
    }

    private void add(Card card){
        cards.add(card);

        lastCardAdded = card;

        setChanged();
        notifyObservers(Notifications.CARD_ADDED);
    }

    public void refill() {


        for(int i = 1; i <= 14;i++){
            add(new Card(Card.Type.DIAMOND, i));
            add(new Card(Card.Type.CLUB, i));
            add(new Card(Card.Type.HEART, i));
            add(new Card(Card.Type.SPADE, i));
        }

        for(int i = 1; i <= 21; i++)
            add(new Trump(i));

        add(new Fool());
    }

    public void shuffle() {
        Collections.shuffle(cards, new Random(System.nanoTime()));

        setChanged();
        notifyObservers(Notifications.SHUFFLED);
    }

    public void deal(Hand hand){
        if(cards.isEmpty())
            throw new NoSuchElementException("Deck is empty, can't deal !");

        Card card = cards.get(cards.size() - 1);
        cards.remove(card);

        hand.addCard(card);

        lastCardDealt = card;

        setChanged();
        notifyObservers(Notifications.CARD_DEALED);
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

        setChanged();
        notifyObservers(Notifications.CUT);
    }


    public Card getLastCardDealt() {
        return lastCardDealt;
    }

    public Card getLastCardAdded() {
        return lastCardAdded;
    }
}
