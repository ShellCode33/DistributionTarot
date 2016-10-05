package tests;

import fr.iut.etu.Card;
import fr.iut.etu.Deck;
import fr.iut.etu.Player;
import fr.iut.etu.Trump;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collection;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by sdupouy002 on 04/10/16.
 */
public class TarotTests {

    public ExpectedException exception = ExpectedException.none();


    @Test
    public void canCreateCard() throws Exception {
        Card card = new Card(Card.Type.CLOVER, 2);
        assertNotEquals("Card not created !", null, card);
        assertEquals("Wrong card type !", Card.Type.CLOVER, card.getType());
        assertEquals("Wrong card value !", 2, card.getValue());
    }

    @Test
    public void canCreatetrump() throws Exception {
        Trump trump = new Trump(19);

        assertNotEquals("Trump not created !", null, trump);
        assertEquals("Wrong trump type !", Card.Type.TRUMP, trump.getType());
        assertEquals("Wrong trump value !", 19, trump.getValue());
    }

    @Test(expected =  IllegalArgumentException.class)
    public void exceptionCardCreation() throws Exception {
        new Card(Card.Type.TILE, 15);
        new Card(Card.Type.HEART, 0);
    }

    @Test(expected =  IllegalArgumentException.class)
    public void exceptionTrumpCreation() throws Exception {
        new Trump(22);
        new Trump(0);
    }


    @Test
    public void canCreatePlayer() throws Exception {
        Player player = new Player();

        assertNotEquals("Player not created !", null, player);
    }

    @Test
    public void canCreateDeck() throws Exception {
        Deck deck = new Deck();

        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.getSize(), 0);
    }

    @Test
    public void canRefillDeck() throws Exception {
        Deck deck = new Deck();

        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.getSize(), 0);

        deck.refill();
        assertEquals("Wrong deck size !", deck.getSize(), 78);
    }

    @Test
    public void doesDeckContains() throws Exception {
        Deck deck = new Deck();
        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.getSize(), 0);

        deck.refill();
        assertEquals("Wrong deck size !", deck.getSize(), 78);

        assertTrue("Deck.contains not working !", deck.contains(new Card(Card.Type.CLOVER, 9)));
    }

    @Test
    public void canShuffleDeck() throws Exception {

        Deck notshuffled = new Deck();
        assertNotEquals("Deck not created !", notshuffled, null);
        assertEquals("Wrong deck size !", notshuffled.getSize(), 0);

        notshuffled.refill();
        assertEquals("Wrong deck size !", notshuffled.getSize(), 78);

        Deck shuffled = new Deck();
        assertNotEquals("Deck not created !", shuffled, null);
        assertEquals("Wrong deck size !", shuffled.getSize(), 0);

        shuffled.refill();
        assertEquals("Wrong deck size !", shuffled.getSize(), 78);

        shuffled.shuffle();

        boolean isShuffled = false;
        int i = 0;

        while(i < shuffled.getCards().size()
                && i < notshuffled.getCards().size()
                && !isShuffled) {

            if(shuffled.getCards().get(i) != notshuffled.getCards().get(i))
                isShuffled = true;

            i++;
        }

        assertTrue("Deck not shuffled !", isShuffled);
    }


}
