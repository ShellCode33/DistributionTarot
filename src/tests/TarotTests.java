package tests;

import fr.iut.etu.Card;
import fr.iut.etu.Deck;
import fr.iut.etu.Player;
import fr.iut.etu.Trump;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by sdupouy002 on 04/10/16.
 */
public class TarotTests {

    public ExpectedException exception = ExpectedException.none();


    @Test
    public void isCardCreated() throws Exception {
        Card card = new Card(Card.Type.CLOVER, 2);
        assertNotEquals("Card not created !", null, card);
        assertEquals("Wrong card type !", Card.Type.CLOVER, card.getType());
        assertEquals("Wrong card value !", 2, card.getValue());
    }

    @Test
    public void isTrumpCreated() throws Exception {
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
    public void isPlayerCreated() throws Exception {
        Player player = new Player();

        assertNotEquals("Player not created !", null, player);
    }

    @Test
    public void isDeckCreated() throws Exception {
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
}
