import fr.iut.etu.model.*;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Created by Sylvain DUPOUY on 04/10/16.
 */
public class TarotTests {

    public ExpectedException exception = ExpectedException.none();

    @Test
    public void canCreateCard() throws Exception {
        Card card = new Card(Card.Type.CLUB, 2);
        assertNotEquals("Card not created !", null, card);
        assertEquals("Wrong card type !", Card.Type.CLUB, card.getType());
        assertEquals("Wrong card value !", 2, card.getValue());
    }

    @Test(expected =  IllegalArgumentException.class)
    public void exceptionCardCreation() throws Exception {
        new Card(Card.Type.DIAMOND, 15);
        new Card(Card.Type.HEART, 0);
    }

    @Test
    public void canCreatetrump() throws Exception {
        Trump trump = new Trump(19);

        assertNotEquals("Trump not created !", null, trump);
        assertEquals("Wrong trump type !", Card.Type.TRUMP, trump.getType());
        assertEquals("Wrong trump value !", 19, trump.getValue());
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

        Player player2 = new Player("Patrick");
        assertNotEquals("Player not created !", null, player2);
    }

    @Test
    public void canCreateDeck() throws Exception {
        Deck deck = new Deck();

        assertNotEquals("Deck not created !", null, deck);
        assertEquals("Wrong deck size !", 0, deck.size());
    }

    @Test
    public void canRefillDeck() throws Exception {
        Deck deck = new Deck();

        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.size(), 0);

        deck.refill();
        assertEquals("Wrong deck size !", deck.size(), 78);
    }

    @Test
    public void doesDeckContains() throws Exception {
        Deck deck = new Deck();
        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.size(), 0);

        deck.refill();
        assertEquals("Wrong deck size !", deck.size(), 78);

        assertTrue("Deck.contains not working !", deck.contains(new Card(Card.Type.CLUB, 9)));
    }

    @Test
    public void canShuffleDeck() throws Exception {

        Deck notshuffled = new Deck();
        assertNotEquals("Deck not created !", notshuffled, null);
        assertEquals("Wrong deck size !", notshuffled.size(), 0);

        notshuffled.refill();
        assertEquals("Wrong deck size !", notshuffled.size(), 78);

        Deck shuffled = new Deck();
        assertNotEquals("Deck not created !", shuffled, null);
        assertEquals("Wrong deck size !", shuffled.size(), 0);

        shuffled.refill();
        assertEquals("Wrong deck size !", shuffled.size(), 78);

        shuffled.shuffle();

        boolean isShuffled = false;
        int i = 0;

        while(i < shuffled.getCards().size()
                && i < notshuffled.getCards().size()
                && !isShuffled) {

            if(!shuffled.getCard(i).equals(notshuffled.getCard(i)))
                isShuffled = true;

            i++;
        }

        assertTrue("Deck not shuffled !", isShuffled);
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void exceptionGetACardFromDeck() throws Exception {
        Deck deck = new Deck();
        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.size(), 0);

        deck.refill();
        assertEquals("Wrong deck size !", deck.size(), 78);

        assertTrue("Deck.contains not working !", deck.contains(new Card(Card.Type.CLUB, 9)));

        deck.getCard(-1);
        deck.getCard(78);
    }

    @Test
    public void canCutDeck() throws Exception {
        Deck deck = new Deck();
        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.size(), 0);

        deck.refill();
        assertEquals("Wrong deck size !", deck.size(), 78);

        assertTrue("Deck.contains not working !", deck.contains(new Card(Card.Type.CLUB, 9)));

        Card cardAtCut = deck.getCard(24);
        Card cardAtBottom = deck.getCard(0);

        deck.cut(24);

        assertTrue("Deck not cuted !", deck.getCard(78 - 24).equals(cardAtBottom));
        assertTrue("Deck not cuted !", deck.getCard(0).equals(cardAtCut));
    }

    @Test(expected = NoSuchElementException.class)
    public void exceptionDealACard() throws Exception {
        Deck deck = new Deck();
        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.size(), 0);

        Player player = new Player();
        deck.deal(player);
    }

    @Test
    public void canDealACardToAHand() throws Exception {
        Deck deck = new Deck();
        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.size(), 0);

        deck.refill();
        assertEquals("Wrong deck size !", deck.size(), 78);

        assertTrue("Deck.contains not working !", deck.contains(new Card(Card.Type.CLUB, 9)));

        Card cardOnTop = deck.getCards().get(deck.size() - 1);

        Player player = new Player();
        deck.deal(player);

        assertFalse("Deck didn't deal !", deck.contains(cardOnTop));
        assertEquals("Deck size not reduced !", deck.size(), 77);

        assertTrue("Player didn't receive the card", player.getCards().contains(cardOnTop));
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionDealIndex() throws Exception {
        Deck deck = new Deck();
        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.size(), 0);

        deck.cut(24);
        deck.refill();
        assertEquals("Wrong deck size !", deck.size(), 78);
        deck.cut(2);
        deck.cut(76);
    }
}
